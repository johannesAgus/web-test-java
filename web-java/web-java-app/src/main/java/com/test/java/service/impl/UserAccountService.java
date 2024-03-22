package com.test.java.service.impl;

import com.test.java.domain.UserAccount;
import com.test.java.dto.LoginUserAccountDto;
import com.test.java.dto.UserAccountDto;
import com.test.java.dto.UserAccountSearchDto;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface UserAccountService {

    Optional<UserAccount> findById(String id);

    UserAccount save(UserAccountDto userAccountDto);

    UserAccount update(UserAccountDto userAccountDto);

    void delete(String id);

    Optional<UserAccount> findByUsername(String username);

    Optional<UserAccount> findByEmail(String email);

    UserAccount login(LoginUserAccountDto loginUserAccountDto);

    Page<UserAccount> findSearch(UserAccountSearchDto userAccountSearchDto);

}
