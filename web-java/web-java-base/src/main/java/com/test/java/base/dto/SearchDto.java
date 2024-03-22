package com.test.java.base.dto;

import lombok.Data;

@Data
public class SearchDto {

    private int page = 1;

    private int size;

    private String sort;
}
