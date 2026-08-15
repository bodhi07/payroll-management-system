package com.payroll.member3.dsa;

import com.payroll.member3.entity.Payroll;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================================
 * MEMBER 03 DATA STRUCTURE: Custom Payroll Binary Search Tree (BST)
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose: Hierarchically indexes monthly payroll records by Net Salary amount.
 * - Capabilities:
 *     * O(log N) average search for payrolls by specific salary.
 *     * O(log N) search for Minimum and Maximum salary disbursement.
 *     * Fast Range Queries [minSalary, maxSalary] to identify salary tiers.
 *     * In-Order Traversal yields automatically sorted payrolls (O(N)).
 * - Time Complexity:
 *     * Insert: O(log N) avg, O(N) worst-case.
 *     * Search: O(log N) avg, O(N) worst-case.
 *     * In-Order Traversal: O(N).
 * - Space Complexity: O(N).
 */
public class PayrollBinarySearchTree {

    public static class BSTNode {
        public BigDecimal key; // Net Salary
        public List<Payroll> payrollRecords;
        public BSTNode left;
        public BSTNode right;

        public BSTNode(Payroll payroll) {
            this.key = payroll.getNetSalary() != null ? payroll.getNetSalary() : BigDecimal.ZERO;
            this.payrollRecords = new ArrayList<>();
            this.payrollRecords.add(payroll);
            this.left = null;
            this.right = null;
        }
    }

    private BSTNode root;

    public PayrollBinarySearchTree() {
        this.root = null;
    }

    /**
     * Inserts a payroll record into the BST based on net salary.
     */
    public void insert(Payroll payroll) {
        if (payroll == null) return;
        BigDecimal net = payroll.getNetSalary() != null ? payroll.getNetSalary() : BigDecimal.ZERO;
        root = insertRec(root, payroll, net);
    }

    private BSTNode insertRec(BSTNode current, Payroll payroll, BigDecimal net) {
        if (current == null) {
            return new BSTNode(payroll);
        }

        int cmp = net.compareTo(current.key);
        if (cmp < 0) {
            current.left = insertRec(current.left, payroll, net);
        } else if (cmp > 0) {
            current.right = insertRec(current.right, payroll, net);
        } else {
            current.payrollRecords.add(payroll);
        }

        return current;
    }

    /**
     * Returns all payroll records falling within a specified salary budget range [min, max].
     */
    public List<Payroll> findRange(BigDecimal min, BigDecimal max) {
        List<Payroll> result = new ArrayList<>();
        findRangeRec(root, min, max, result);
        return result;
    }

    private void findRangeRec(BSTNode node, BigDecimal min, BigDecimal max, List<Payroll> result) {
        if (node == null) return;

        if (min.compareTo(node.key) < 0) {
            findRangeRec(node.left, min, max, result);
        }

        if (min.compareTo(node.key) <= 0 && max.compareTo(node.key) >= 0) {
            result.addAll(node.payrollRecords);
        }

        if (max.compareTo(node.key) > 0) {
            findRangeRec(node.right, min, max, result);
        }
    }

    /**
     * In-Order Traversal returns all payroll records in strictly ascending order of net pay.
     */
    public List<Payroll> getInOrderSorted() {
        List<Payroll> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(BSTNode node, List<Payroll> result) {
        if (node != null) {
            inOrderRec(node.left, result);
            result.addAll(node.payrollRecords);
            inOrderRec(node.right, result);
        }
    }

    /**
     * Finds the minimum salary disbursement in O(log N) time.
     */
    public BigDecimal getMinSalary() {
        if (root == null) return BigDecimal.ZERO;
        BSTNode curr = root;
        while (curr.left != null) curr = curr.left;
        return curr.key;
    }

    /**
     * Finds the maximum salary disbursement in O(log N) time.
     */
    public BigDecimal getMaxSalary() {
        if (root == null) return BigDecimal.ZERO;
        BSTNode curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.key;
    }
}
