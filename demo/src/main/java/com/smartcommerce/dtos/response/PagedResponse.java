package com.smartcommerce.dtos.response;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Paginated response wrapper with sorting and pagination metadata")
public class PagedResponse<T> {

    @Schema(description = "List of items on the current page")
    private List<T> content;

    @Schema(description = "Current page number (0-indexed)", example = "0")
    private int pageNumber;

    @Schema(description = "Number of items per page", example = "10")
    private int pageSize;

    @Schema(description = "Total number of items across all pages", example = "42")
    private long totalElements;

    @Schema(description = "Total number of pages", example = "5")
    private int totalPages;

    @Schema(description = "Whether this is the first page", example = "true")
    private boolean first;

    @Schema(description = "Whether this is the last page", example = "false")
    private boolean last;

    @Schema(description = "Whether the content list is empty", example = "false")
    private boolean empty;

    @Schema(description = "Field used for sorting", example = "productId")
    private String sortBy;

    @Schema(description = "Sort direction", example = "ASC", allowableValues = {"ASC", "DESC"})
    private String sortDirection;

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