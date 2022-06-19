package com.microservices.swotlyzer.api.core.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaginationResponse<T> {
    private int currentPage;
    private int perPage;
    private int totalPages;
    private Long offset;
    private Long totalItems;

    private List<T> data;

}
