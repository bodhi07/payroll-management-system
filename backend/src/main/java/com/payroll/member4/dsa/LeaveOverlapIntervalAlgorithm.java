package com.payroll.member4.dsa;

import com.payroll.member4.entity.LeaveRequest;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * ============================================================================
 * MEMBER 04 ALGORITHM: Custom Leave Interval Overlap Detection Algorithm
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Detects date overlaps/conflicts between a newly applied leave interval
 *   [startDate, endDate] and an employee's existing approved leaves.
 * - Technique: Interval Scheduling & Sweep-line logic.
 * - Math Definition:
 *     Two intervals [S1, E1] and [S2, E2] overlap IF AND ONLY IF:
 *     max(S1, S2) <= min(E1, E2)
 * - Time Complexity:
 *     * Single check against N existing leaves: O(N) linear time.
 *     * Multi-interval validation across sorted list: O(N log N) sorting + O(N) sweep.
 * - Space Complexity: O(1).
 */
public class LeaveOverlapIntervalAlgorithm {

    public static class IntervalResult {
        private boolean hasConflict;
        private LeaveRequest conflictingLeave;

        public IntervalResult(boolean hasConflict, LeaveRequest conflictingLeave) {
            this.hasConflict = hasConflict;
            this.conflictingLeave = conflictingLeave;
        }

        public boolean isHasConflict() { return hasConflict; }
        public LeaveRequest getConflictingLeave() { return conflictingLeave; }
    }

    /**
     * Checks if a new date interval conflicts with any existing leaves.
     */
    public static IntervalResult checkOverlap(List<LeaveRequest> existingLeaves, LocalDate newStart, LocalDate newEnd) {
        if (existingLeaves == null || existingLeaves.isEmpty() || newStart == null || newEnd == null) {
            return new IntervalResult(false, null);
        }

        for (LeaveRequest existing : existingLeaves) {
            LocalDate start = existing.getStartDate();
            LocalDate end = existing.getEndDate();

            if (start == null || end == null) continue;

            // Overlap condition: (newStart <= existing.end) AND (newEnd >= existing.start)
            boolean overlaps = !newStart.isAfter(end) && !newEnd.isBefore(start);

            if (overlaps) {
                return new IntervalResult(true, existing);
            }
        }

        return new IntervalResult(false, null);
    }

    /**
     * Stably sorts a list of leave intervals chronologically by start date (O(N log N)).
     */
    public static void sortIntervalsChronologically(List<LeaveRequest> leaves) {
        if (leaves == null || leaves.size() <= 1) return;
        Collections.sort(leaves, new Comparator<LeaveRequest>() {
            @Override
            public int compare(LeaveRequest o1, LeaveRequest o2) {
                if (o1.getStartDate() == null) return 1;
                if (o2.getStartDate() == null) return -1;
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
    }
}
