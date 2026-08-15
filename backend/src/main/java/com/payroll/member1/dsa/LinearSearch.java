package com.payroll.member1.dsa;

import java.util.List;

/**
 * ============================================================================
 * Member 01 Algorithm: Linear Search
 * ============================================================================
 */
public class LinearSearch {

    public static <T> int search(final T[] array, final T target) {
        if (array == null || target == null) return -1;
        for (int i = 0; i < array.length; i++) {
            if (target.equals(array[i])) return i;
        }
        return -1;
    }

    public static <T> int search(final List<T> list, final T target) {
        if (list == null || target == null) return -1;
        for (int i = 0; i < list.size(); i++) {
            if (target.equals(list.get(i))) return i;
        }
        return -1;
    }
}
