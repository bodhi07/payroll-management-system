package com.payroll.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ============================================================================
 * Paginated API Response Wrapper
 * ============================================================================
 * 
 * Why This File Exists:
 * --------------------
 * Wraps paginated query results with pagination metadata (page number, page size,
 * total elements, total pages, last page boolean) for search/filter endpoints.
 * 
 * OOP Concepts Used:
 * --------------------
 * - Generics: List{@code <T>} enables reusable pagination over any entity/DTO.
 * - Encapsulation: Protects pagination meta fields and provides unified getters.
 * 
 * Design Patterns Used:
 * --------------------
 * - Data Transfer Object (DTO) Pattern: Encapsulates page metadata.
 * - Builder Pattern: Constructed using {@code @Builder}.
 * 
 * @param <T> Element type in content list
 * @author Senior Java Software Architect
 * @version 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    /** List of records contained within the current page. */
    private List<T> content;

    /** Current zero-indexed page number. */
    private int pageNo;

    /** Maximum records per page. */
    private int pageSize;

    /** Total count of matching records in database. */
    private long totalElements;

    /** Total calculated pages based on total elements and page size. */
    private int totalPages;

    /** True if current page is the final page. */
    private boolean last;
}
