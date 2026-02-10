package com.smartcommerce.dtos.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic paginated response wrapper
 * @param <T> Type of data being returned
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {
    private List<T> content;           // The actual data
    private int pageNumber;            // Current page (0-indexed)
    private int pageSize;              // Items per page
    private long totalElements;        // Total number of items across all pages
    private int totalPages;            // Total number of pages
    private boolean first;             // Is this the first page?
    private boolean last;              // Is this the last page?
    private boolean empty;             // Is the content empty?
    private String sortBy;             // Field used for sorting
    private String sortDirection;      // ASC or DESC

    /**
     * Constructor for creating paginated response
     */
    public PagedResponse(List<T> content, int pageNumber, int pageSize, long totalElements,
                        String sortBy, String sortDirection) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / pageSize);
        this.first = pageNumber == 0;
        this.last = pageNumber >= totalPages - 1;
        this.empty = content.isEmpty();
        this.sortBy = sortBy;
        this.sortDirection = sortDirection;
    }
}