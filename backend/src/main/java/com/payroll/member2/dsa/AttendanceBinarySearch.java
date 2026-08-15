package com.payroll.member2.dsa;

import com.payroll.member2.entity.Attendance;
import java.time.LocalDate;
import java.util.List;

/**
 * ============================================================================
 * MEMBER 02 ALGORITHM: Custom Attendance Binary Search (Divide & Conquer)
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Searches for an employee's shift attendance log on a specific date
 *   within a pre-sorted attendance ledger in O(log N) logarithmic time.
 * - Strategy: Compares target date with middle element, eliminating half of
 *   the search space in every iteration.
 * - Time Complexity:
 *     * Best Case:    O(1) (target date is in the exact middle).
 *     * Average Case: O(log N).
 *     * Worst Case:   O(log N).
 * - Space Complexity: O(1) iterative space.
 */
public class AttendanceBinarySearch {

    /**
     * Finds the index of an attendance record matching target date in O(log N) time.
     * Pre-requisite: The attendance list must be sorted by date ascending.
     */
    public static int searchByDate(List<Attendance> sortedList, LocalDate targetDate) {
        if (sortedList == null || sortedList.isEmpty() || targetDate == null) {
            return -1;
        }

        int low = 0;
        int high = sortedList.size() - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            Attendance midRecord = sortedList.get(mid);
            LocalDate midDate = midRecord.getDate();

            if (midDate == null) {
                high = mid - 1;
                continue;
            }

            int comparison = midDate.compareTo(targetDate);

            if (comparison == 0) {
                return mid; // Match found at index 'mid'
            } else if (comparison < 0) {
                low = mid + 1; // Target date is in right half
            } else {
                high = mid - 1; // Target date is in left half
            }
        }

        return -1; // Date not found
    }

    /**
     * Custom QuickSort implementation to ensure attendance lists are sorted by Date
     * prior to performing binary searches (O(N log N)).
     */
    public static void sortAttendanceByDate(List<Attendance> list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            sortAttendanceByDate(list, low, pi - 1);
            sortAttendanceByDate(list, pi + 1, high);
        }
    }

    private static int partition(List<Attendance> list, int low, int high) {
        LocalDate pivot = list.get(high).getDate();
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            LocalDate current = list.get(j).getDate();
            if (current != null && pivot != null && current.compareTo(pivot) <= 0) {
                i++;
                Attendance temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        Attendance temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }
}
