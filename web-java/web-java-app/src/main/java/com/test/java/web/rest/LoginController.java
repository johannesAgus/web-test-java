package com.test.java.web.rest;

import com.test.java.domain.Member;
import com.test.java.domain.UserAccount;
import com.test.java.dto.LoginMemberAccountDto;
import com.test.java.dto.LoginUserAccountDto;
import com.test.java.dto.MemberInformationDto;
import com.test.java.dto.UserInformationDto;
import com.test.java.service.impl.MemberService;
import com.test.java.service.impl.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login/")
public class LoginController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private UserAccountService userAccountService;

    @PostMapping("member")
    public MemberInformationDto login(@RequestBody LoginMemberAccountDto loginMemberAccountDto) {
        return toUserContextMember(memberService.login(loginMemberAccountDto));
    }

    @PostMapping("user")
    public UserInformationDto login(@RequestBody LoginUserAccountDto loginUserAccountDto) {
        return toUserContextUser(userAccountService.login(loginUserAccountDto));
    }

    private MemberInformationDto toUserContextMember(Member member) {

        MemberInformationDto memberInformationDto = new MemberInformationDto();
        memberInformationDto.setId(member.getId());
        memberInformationDto.setName(member.getName());
        memberInformationDto.setEmail(member.getEmail());
        return memberInformationDto;
    }

    private UserInformationDto toUserContextUser(UserAccount userAccount) {

        UserInformationDto userInformationDto = new UserInformationDto();
        userInformationDto.setId(userAccount.getId());
        userInformationDto.setUsername(userAccount.getUsername());
        userInformationDto.setName(userAccount.getName());
        userInformationDto.setEmail(userAccount.getEmail());
        return userInformationDto;
    }

}
