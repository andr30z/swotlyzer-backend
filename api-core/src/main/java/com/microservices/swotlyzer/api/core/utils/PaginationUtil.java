package com.microservices.swotlyzer.api.core.utils;

import com.microservices.swotlyzer.api.core.dtos.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public class PaginationUtil<T, K extends MongoRepository<T, String>> {


    public PaginationResponse<T> buildResponse(Page<T> page) {
        PaginationResponse<T> paginationResponse = new PaginationResponse<T>();
        Pageable pageable = page.getPageable();
        paginationResponse.setData(page.getContent());
        paginationResponse.setTotalPages(page.getTotalPages());
        paginationResponse.setPerPage(pageable.getPageSize());
        paginationResponse.setOffset(pageable.getOffset());
        paginationResponse.setCurrentPage(pageable.getPageNumber() + 1);
        paginationResponse.setTotalItems(page.getTotalElements());
        return paginationResponse;
    }

    public PaginationResponse<T> paginateAll(int currentPage, int perPage, K repository) {
        Pageable paging = PageRequest.of(currentPage, perPage);
        Page<T> page = repository.findAll(paging);
        return this.buildResponse(page);
    }
}
