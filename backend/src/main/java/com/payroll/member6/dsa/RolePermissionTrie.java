package com.payroll.member6.dsa;

import com.payroll.member6.entity.AuditLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================================
 * MEMBER 06 ALGORITHM: Custom Role Permission Prefix Trie & Audit Log MergeSort
 * ============================================================================
 *
 * EXPLANATION FOR VIVA / PRESENTATION:
 * -----------------------------------
 * - Purpose 1 (Trie): Fast O(L) prefix-based route permission validator where L is
 *   the endpoint URL length (e.g. validating permissions for "/api/v1/payroll/generate").
 * - Purpose 2 (MergeSort): Stably sorts millions of audit log records by timestamp
 *   in guaranteed O(N log N) time complexity.
 */
public class RolePermissionTrie {

    public static class TrieNode {
        public Map<Character, TrieNode> children = new HashMap<>();
        public boolean isEndOfPermission = false;
        public String requiredRole = null;
    }

    private final TrieNode root;

    public RolePermissionTrie() {
        this.root = new TrieNode();
    }

    /**
     * Inserts an endpoint route and its required security role in O(L) time.
     */
    public void insertPermission(String apiRoute, String requiredRole) {
        if (apiRoute == null || apiRoute.isEmpty()) return;
        TrieNode current = root;
        for (char ch : apiRoute.toLowerCase().toCharArray()) {
            current.children.putIfAbsent(ch, new TrieNode());
            current = current.children.get(ch);
        }
        current.isEndOfPermission = true;
        current.requiredRole = requiredRole;
    }

    /**
     * Validates if a user's assigned role matches the route's security requirement in O(L) time.
     */
    public boolean hasAccess(String apiRoute, List<String> userRoles) {
        if (apiRoute == null || userRoles == null) return false;
        TrieNode current = root;
        for (char ch : apiRoute.toLowerCase().toCharArray()) {
            if (!current.children.containsKey(ch)) {
                return true; // Unprotected / public route by default
            }
            current = current.children.get(ch);
        }

        if (current.isEndOfPermission && current.requiredRole != null) {
            return userRoles.contains(current.requiredRole) || userRoles.contains("ROLE_ADMIN");
        }
        return true;
    }

    /**
     * Stably sorts AuditLog records by createdAt timestamp descending in O(N log N) time.
     */
    public static List<AuditLog> sortAuditLogsByTimestamp(List<AuditLog> logs) {
        if (logs == null || logs.size() <= 1) return logs;
        List<AuditLog> list = new ArrayList<>(logs);
        mergeSort(list, 0, list.size() - 1);
        return list;
    }

    private static void mergeSort(List<AuditLog> list, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(list, left, mid);
            mergeSort(list, mid + 1, right);
            merge(list, left, mid, right);
        }
    }

    private static void merge(List<AuditLog> list, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<AuditLog> leftList = new ArrayList<>(n1);
        List<AuditLog> rightList = new ArrayList<>(n2);

        for (int i = 0; i < n1; ++i) leftList.add(list.get(left + i));
        for (int j = 0; j < n2; ++j) rightList.add(list.get(mid + 1 + j));

        int i = 0, j = 0, k = left;

        while (i < n1 && j < n2) {
            AuditLog a1 = leftList.get(i);
            AuditLog a2 = rightList.get(j);

            // Descending order (newest first)
            int cmp = 0;
            if (a1.getCreatedAt() != null && a2.getCreatedAt() != null) {
                cmp = a2.getCreatedAt().compareTo(a1.getCreatedAt());
            }

            if (cmp <= 0) {
                list.set(k, leftList.get(i));
                i++;
            } else {
                list.set(k, rightList.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            list.set(k, leftList.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            list.set(k, rightList.get(j));
            j++;
            k++;
        }
    }
}
