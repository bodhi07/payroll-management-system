package com.payroll.member4.dsa;

import com.payroll.member4.entity.LeaveRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * MEMBER 04 DATA STRUCTURE: Custom Leave Max-Heap Priority Queue
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Prioritizes pending employee leave applications based on medical
 *   urgency and submission timestamp so HR reviews critical leaves first:
 *     * MEDICAL Leave -> Highest Priority (Weight = 300)
 *     * CASUAL Leave  -> Medium Priority (Weight = 200)
 *     * ANNUAL Leave  -> Standard Priority (Weight = 100)
 * - Implementation: Binary Max-Heap represented as an ArrayList.
 * - Time Complexity:
 *     * insert() [New Leave Application]:  O(log N) heapify-up.
 *     * extractMax() [Review Top Leave]:   O(log N) heapify-down.
 *     * peek() [Inspect Top Urgent Leave]: O(1) constant time.
 * - Space Complexity: O(N).
 */
public class LeavePriorityQueue {

    private static class PrioritizedLeave {
        LeaveRequest leave;
        int priorityWeight;

        public PrioritizedLeave(LeaveRequest leave) {
            this.leave = leave;
            this.priorityWeight = calculateWeight(leave);
        }

        private int calculateWeight(LeaveRequest l) {
            if (l == null || l.getLeaveType() == null) return 0;
            String type = l.getLeaveType().toUpperCase();
            if (type.contains("MEDICAL")) return 300;
            if (type.contains("CASUAL")) return 200;
            if (type.contains("ANNUAL")) return 100;
            return 50;
        }
    }

    private List<PrioritizedLeave> heap;

    public LeavePriorityQueue() {
        this.heap = new ArrayList<>();
    }

    public synchronized void insert(LeaveRequest leave) {
        if (leave == null) return;
        PrioritizedLeave node = new PrioritizedLeave(leave);
        heap.add(node);
        heapifyUp(heap.size() - 1);
    }

    public synchronized LeaveRequest extractMax() {
        if (heap.isEmpty()) return null;

        LeaveRequest maxLeave = heap.get(0).leave;
        PrioritizedLeave lastNode = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, lastNode);
            heapifyDown(0);
        }

        return maxLeave;
    }

    public synchronized LeaveRequest peek() {
        if (heap.isEmpty()) return null;
        return heap.get(0).leave;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    private void heapifyUp(int index) {
        int parent = (index - 1) / 2;
        while (index > 0 && heap.get(index).priorityWeight > heap.get(parent).priorityWeight) {
            swap(index, parent);
            index = parent;
            parent = (index - 1) / 2;
        }
    }

    private void heapifyDown(int index) {
        int maxIndex = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;

        if (leftChild < heap.size() && heap.get(leftChild).priorityWeight > heap.get(maxIndex).priorityWeight) {
            maxIndex = leftChild;
        }

        if (rightChild < heap.size() && heap.get(rightChild).priorityWeight > heap.get(maxIndex).priorityWeight) {
            maxIndex = rightChild;
        }

        if (index != maxIndex) {
            swap(index, maxIndex);
            heapifyDown(maxIndex);
        }
    }

    private void swap(int i, int j) {
        PrioritizedLeave temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    public List<LeaveRequest> getPrioritizedList() {
        LeavePriorityQueue copy = new LeavePriorityQueue();
        for (PrioritizedLeave pl : heap) {
            copy.insert(pl.leave);
        }
        List<LeaveRequest> sorted = new ArrayList<>();
        while (!copy.isEmpty()) {
            sorted.add(copy.extractMax());
        }
        return sorted;
    }
}
