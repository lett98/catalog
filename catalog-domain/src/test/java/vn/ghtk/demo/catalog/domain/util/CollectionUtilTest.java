package vn.ghtk.demo.catalog.domain.util;

import org.junit.jupiter.api.Test;
import vn.ghtk.demo.catalog.common.utils.CollectionUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionUtilTest {

    @Test
    void equals_shouldReturnTrueForEqualLists() {
        // Given
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertTrue(result);
    }

    @Test
    void equals_shouldReturnTrueForListsWithDifferentOrder() {
        // Given
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(3, 2, 1);

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertTrue(result);
    }

    @Test
    void equals_shouldReturnFalseForDifferentLists() {
        // Given
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 4);

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnTrueForEmptyLists() {
        // Given
        List<Integer> list1 = Collections.emptyList();
        List<Integer> list2 = Collections.emptyList();

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertTrue(result);
    }

    @Test
    void equals_shouldReturnFalseWhenFirstListIsNull() {
        // Given
        List<Integer> list1 = null;
        List<Integer> list2 = Arrays.asList(1, 2, 3);

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenSecondListIsNull() {
        // Given
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = null;

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseWhenBothListsAreNull() {
        // Given
        List<Integer> list1 = null;
        List<Integer> list2 = null;

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertFalse(result);
    }

    @Test
    void equals_shouldReturnFalseForDifferentSizeLists() {
        // Given
        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2);

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertFalse(result);
    }

    @Test
    void equals_shouldWorkWithStrings() {
        // Given
        List<String> list1 = Arrays.asList("apple", "banana", "cherry");
        List<String> list2 = Arrays.asList("cherry", "apple", "banana");

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertTrue(result);
    }

    @Test
    void equals_shouldHandleDuplicateElements() {
        // Given
        List<Integer> list1 = Arrays.asList(1, 2, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);

        // When
        boolean result = CollectionUtil.equals(list1, list2);

        // Then
        assertTrue(result, "Sets ignore duplicates, so lists with different duplicate counts should be equal");
    }
}
