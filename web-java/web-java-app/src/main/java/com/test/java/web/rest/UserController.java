package com.test.java.web.rest;

import com.test.java.domain.UserAccount;
import com.test.java.dto.UserAccountDto;
import com.test.java.dto.UserAccountSearchDto;
import com.test.java.service.impl.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/user/")
public class UserController {

    @Autowired
    private UserAccountService userAccountService;

    @GetMapping("")
    public Page<UserAccountDto> findSearch(UserAccountSearchDto userAccountSearchDto) {
        return userAccountService.findSearch(userAccountSearchDto).map(this::toUserAccountDto);
    }

    @GetMapping("{id}")
    public UserAccountDto findById(@PathVariable(name = "id") String id) {
        return toUserAccountDto(userAccountService.findById(id).orElseThrow(() -> new RuntimeException("No User found with id : " + id)));
    }

    @PostMapping("")
    public UserAccountDto save(@RequestBody @Valid UserAccountDto userAccountDto) {
        return toUserAccountDto(userAccountService.save(userAccountDto));
    }

    @PatchMapping("")
    public UserAccountDto update(@RequestBody @Valid UserAccountDto userAccountDto) {
        return toUserAccountDto(userAccountService.update(userAccountDto));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable(name = "id") String id) {
        userAccountService.delete(id);
    }

    private UserAccountDto toUserAccountDto(UserAccount userAccount) {
        UserAccountDto userAccountDto = new UserAccountDto();
        userAccountDto.setId(userAccount.getId());
        userAccountDto.setName(userAccount.getName());
        userAccountDto.setUsername(userAccount.getUsername());
        userAccountDto.setPassword(userAccount.getPassword());
        userAccountDto.setEmail(userAccount.getEmail());
        userAccountDto.setUserStatus(userAccount.getUserStatus());
        userAccountDto.setCreatedAt(userAccount.getCreatedAt().toString());
        if (null != userAccount.getUpdatedAt()){
            userAccountDto.setUpdatedAt(userAccount.getUpdatedAt().toString());
        }
        return userAccountDto;
    }

}
