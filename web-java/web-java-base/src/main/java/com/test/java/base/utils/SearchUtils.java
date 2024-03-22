package com.test.java.base.utils;

import com.test.java.base.dto.SearchDto;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class SearchUtils {

    private static final int DEFAULT_PAGE_SIZE = 20;

    public static Pageable toPageable(SearchDto searchDto) {
        int page = searchDto.getPage() > 0 ? searchDto.getPage() - 1 : 0;
        int size = searchDto.getSize() > 0 ? searchDto.getSize() : DEFAULT_PAGE_SIZE;
        if (!StringUtils.hasText(searchDto.getSort())) {
            return PageRequest.of(page, size);
        } else {
            String[] sortValues = searchDto.getSort().split(",");
            List<Sort.Order> sortOrderList = new ArrayList<>();

            for (String sortValue : sortValues) {
                char sortDirection = sortValue.charAt(0);
                String sortField = sortValue.substring(1);
                sortOrderList.add(new Sort.Order(getSortDirection(sortDirection), sortField));
            }

            return PageRequest.of(page, size, Sort.by(sortOrderList));
        }
    }

    private static Sort.Direction getSortDirection(char sortDirection) {
        if (sortDirection == '+') {
            return Sort.Direction.ASC;
        } else if (sortDirection == '-') {
            return Sort.Direction.DESC;
        } else {
            throw new IllegalArgumentException("Allowed sort direction only (+,-)");
        }
    }
}
