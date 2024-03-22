package com.test.java.service.impl;

import com.test.java.domain.Member;
import com.test.java.domain.UserAccount;
import com.test.java.dto.LoginMemberAccountDto;
import com.test.java.dto.LoginUserAccountDto;
import com.test.java.dto.MemberDto;
import com.test.java.dto.MemberSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MemberService {

    Member save (MemberDto memberDto, MultipartFile multipartFile) throws IOException;

    Member update (MemberDto memberDto, MultipartFile multipartFile) throws IOException;

    void delete (String id);

    List<Member> findList();

    Optional<Member> findById(String id);

    byte[] getFilesPhoto(String namePhoto);

    Page<Member> findSearch(MemberSearchDto memberSearchDto);

    Member login(LoginMemberAccountDto loginMemberAccountDto);

}
