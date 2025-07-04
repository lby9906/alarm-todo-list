package com.spring.alarm_todo_list.util;

import lombok.Getter;

import java.util.List;

@Getter
public class PaginationResponse<T> {

    private final List<T> contents;
    private final long page;
    private final long size;
    private final long totalElements;
    private final int totalPages;

    public PaginationResponse(List<T> contents, long page, long size, long totalElements) {
        this.contents = contents;
        this.page = page;
        this.size = size;
        this.totalElements = totalElements;
        this.totalPages = (int) Math.ceil((double) totalElements / size);
    }

    public static <T> PaginationResponse<T> of(List<T> contents, long page, long size, long totalElements){
        return new PaginationResponse<>(contents, page, size, totalElements);
    }
}
