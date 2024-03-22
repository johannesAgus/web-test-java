package com.test.java.dto;

import com.test.java.base.dto.SearchDto;
import lombok.Data;

@Data
public class MemberSearchDto extends SearchDto {

    private String name;

    private String id;

}
