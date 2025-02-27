package com.service.apicomposition.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaginationResponse<T> {

    @JsonProperty("content")
    private final List<T> content;

    @JsonProperty("pageNumber")
    private final int pageNumber;

    @JsonProperty("pageSize")
    private final int pageSize;

    @JsonProperty("totalPages")
    private final int totalPages;

    @JsonProperty("totalElements")
    private final long totalElements;

    @JsonCreator
    public PaginationResponse(
            @JsonProperty("content") List<T> content,
            @JsonProperty("pageNumber") int pageNumber,
            @JsonProperty("pageSize") int pageSize,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("totalElements") long totalElements) {
        this.content = content;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
    }
}
