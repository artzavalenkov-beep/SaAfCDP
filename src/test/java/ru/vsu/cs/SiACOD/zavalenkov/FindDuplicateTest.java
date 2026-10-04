package ru.vsu.cs.SiACOD.zavalenkov;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FindDuplicateTest {

    @Test
    void testFindPoparnoWithDuplicates() {
        List<String> list = Arrays.asList("apple", "banana", "apple", "orange", "banana");
        Set<String> expected = Set.of("apple", "banana");

        assertEquals(expected, FindDuplicate.findPoparno(list));
    }

    @Test
    void testFindPoparnoNoDuplicates() {
        List<String> list = Arrays.asList("apple", "banana", "orange");
        assertTrue(FindDuplicate.findPoparno(list).isEmpty());
    }

    @Test
    void testFindSortPoparnoWithDuplicates() {
        List<String> list = Arrays.asList("cat", "dog", "cat", "bird", "dog");
        Set<String> expected = Set.of("cat", "dog");

        assertEquals(expected, FindDuplicate.findSortPoparno(list));
    }

    @Test
    void testFindSortPoparnoEmptyAndSingle() {
        assertTrue(FindDuplicate.findSortPoparno(Collections.emptyList()).isEmpty());
        assertTrue(FindDuplicate.findSortPoparno(Collections.singletonList("hello")).isEmpty());
    }

    @Test
    void testFindHashSetWithDuplicates() {
        List<String> list = Arrays.asList("java", "python", "java", "cpp", "python", "java");
        Set<String> expected = Set.of("java", "python");

        assertEquals(expected, FindDuplicate.findHashSet(list));
    }

    @Test
    void testAllMethodsReturnSameResults() {
        List<String> list = Arrays.asList("test", "code", "java", "test", "code", "debug");

        Set<String> res1 = FindDuplicate.findPoparno(list);
        Set<String> res2 = FindDuplicate.findSortPoparno(list);
        Set<String> res3 = FindDuplicate.findHashSet(list);

        assertEquals(res1, res2);
        assertEquals(res2, res3);
    }
}