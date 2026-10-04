package ru.vsu.cs.SiACOD.zavalenkov;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Benchmark {

    // Volatile-переменная предотвращает оптимизацию и удаление "неиспользуемого" кода (Dead Code Elimination)
    private static volatile long g_sink = 0;

    // Включение результатов в accumulator, чтобы JIT-компилятор точно выполнил метод
    private static void consume(Set<String> result) {
        long acc = 0;
        for (String s : result) {
            acc += s.hashCode();
        }
        g_sink += acc;
    }

    private static long median(List<Long> samples) {
        Collections.sort(samples);
        return samples.get(samples.size() / 2);
    }

    // Генерация случайных строк (с повторениями)
    private static List<String> generateRandomStrings(int count, int maxLength, long seed) {
        Random random = new Random(seed);
        List<String> list = new ArrayList<>(count);
        char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();

        for (int i = 0; i < count; i++) {
            int len = 1 + random.nextInt(maxLength);
            StringBuilder sb = new StringBuilder(len);
            for (int j = 0; j < len; j++) {
                sb.append(chars[random.nextInt(chars.length)]);
            }
            list.add(sb.toString());
        }
        return list;
    }

    // --- ПРОГРЕВ (Warmup) ---
    // Нужен для того, чтобы JIT-компилятор перевёл байт-код в оптимизированный машинный код (C1/C2)
    private static void warmup(int count, int maxLength, int iterations) {
        System.out.println("Прогрев JVM (" + iterations + " итераций)...");
        List<String> data = generateRandomStrings(Math.min(count, 1000), maxLength, 12345);

        for (int i = 0; i < iterations; i++) {
            consume(FindDuplicate.findPoparno(data));
            consume(FindDuplicate.findSortPoparno(data));
            consume(FindDuplicate.findHashSet(data));
        }
        System.out.println("Прогрев завершён.\n");
    }

    public static void runBenchmark(int count, int maxLength, int repeats, int warmupIterations) {
        // 1. Выполняем прогрев JVM
        warmup(count, maxLength, warmupIterations);

        List<Long> poparnoTimes = new ArrayList<>(repeats);
        List<Long> sortPoparnoTimes = new ArrayList<>(repeats);
        List<Long> hashSetTimes = new ArrayList<>(repeats);

        // 2. Основной цикл замеров
        for (int r = 0; r < repeats; r++) {
            List<String> source = generateRandomStrings(count, maxLength, r);

            // Рекомендация подтолкнуть сборщик мусора перед замером, чтобы минимизировать паузы GC внутри метода
            System.gc();

            // --- Замер findPoparno O(N^2) ---
            // Пропускаем на слишком больших объемах, чтобы не ждать долго
            long t1 = System.nanoTime();
            Set<String> res1 = FindDuplicate.findPoparno(source);
            long t2 = System.nanoTime();
            consume(res1);
            poparnoTimes.add(t2 - t1);


            // --- Замер findSortPoparno O(N log N) ---
            long t3 = System.nanoTime();
            Set<String> res2 = FindDuplicate.findSortPoparno(source);
            long t4 = System.nanoTime();
            consume(res2);
            sortPoparnoTimes.add(t4 - t3);

            // --- Замер findHashSet O(N) ---
            long t5 = System.nanoTime();
            Set<String> res3 = FindDuplicate.findHashSet(source);
            long t6 = System.nanoTime();
            consume(res3);
            hashSetTimes.add(t6 - t5);
        }

        // 3. Вывод медианных результатов
        System.out.printf("=== Результаты замеров (N=%d, Повторов=%d) ===%n", count, repeats);
        if (!poparnoTimes.isEmpty()) {
            long medPoparno = median(poparnoTimes);
            System.out.printf("findPoparno     (O(N^2))    : медиана = %d нс (%.2f ms)%n",
                    medPoparno, medPoparno / 1000000.0);
        }

        long medSort = median(sortPoparnoTimes);
        System.out.printf("findSortPoparno (O(N log N)): медиана = %d нс (%.2f ms)%n",
                medSort, medSort / 1000000.0);

        long medHash = median(hashSetTimes);
        System.out.printf("findHashSet     (O(N))      : медиана = %d нс (%.2f ms)%n",
                medHash, medHash / 1000000.0);
    }

    public static void main(String[] args) {
        // Запускаем несколько тестов с разными параметрами
        System.out.println("########## ТЕСТ 1 ##########");
        runBenchmark(1000, 20, 30, 2000);
        System.out.println();

        System.out.println("########## ТЕСТ 2 ##########");
        runBenchmark(3000, 20, 30, 2000);
        System.out.println();

        System.out.println("########## ТЕСТ 3 ##########");
        runBenchmark(5000, 20, 30, 2000);
        System.out.println();

        System.out.println("########## ТЕСТ 4 ##########");
        runBenchmark(10000, 20, 30, 2000);
        System.out.println();

        System.out.println("########## ТЕСТ 5 ##########");
        runBenchmark(20000, 20, 30, 2000);
    }
}