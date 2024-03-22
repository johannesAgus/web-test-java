package com.test.java.dto;

import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
public class MemberDto {

    private String id;

    private String name;

    private String password;

    private String phoneNumber;

    private String dateOfBirth;

    private String email;

    private String gender;

    private String idKTP;

    private String namePhoto;

    private String typePhoto;

    private byte[] dataPhoto;

    private ResponseEntity<byte[]> photo;

    private boolean deleteFlag;

    private String createdAt;

    private String updatedAt;

}
