package com.modernized.utils;

import com.modernized.dto.PagedResponse;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.stream.Collectors;
import java.util.function.Function;

public class PaginationUtils {

    public static <T, R> PagedResponse<R> buildPagedResponse(Page<T> page, Function<T, R> mapper) {
        List<R> content = page.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());
        
        return new PagedResponse<>(
                content,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements()
        );
    }
}
