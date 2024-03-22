package com.test.java.dto;

import com.test.java.base.dto.SearchDto;
import lombok.Data;

@Data
public class UserAccountSearchDto extends SearchDto {

    private String id;

    private String username;

}
