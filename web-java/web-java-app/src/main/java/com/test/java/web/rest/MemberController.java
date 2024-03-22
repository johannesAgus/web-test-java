package com.test.java.web.rest;

import com.test.java.domain.Member;
import com.test.java.dto.MemberDto;
import com.test.java.dto.MemberSearchDto;
import com.test.java.service.impl.MemberService;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/member/")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @GetMapping("")
    public Page<MemberDto> findSearch(MemberSearchDto memberSearchDto) {
        return memberService.findSearch(memberSearchDto).map(this::toDto);
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MemberDto save(@RequestPart MemberDto memberDto, MultipartFile multipartFile) throws IOException {
        return toDto(memberService.save(memberDto, multipartFile));
    }

    @PatchMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MemberDto update(@RequestPart MemberDto memberDto, MultipartFile multipartFile) throws IOException {
        return toDto(memberService.update(memberDto, multipartFile));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable(name = "id") String id) {
        memberService.delete(id);
    }

    @GetMapping("{id}")
    public MemberDto findById(@PathVariable(name = "id") String id) {
        return toDto(memberService.findById(id)
                .orElseThrow(() -> new RuntimeException("No Member Id found with Id : " + id)));
    }

    @GetMapping("list")
    public List<MemberDto> findList() {
        return memberService.findList().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/getFileByName/{fileName}")
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        byte[] imageData = memberService.getFilesPhoto(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(imageData);

    }

    private MemberDto toDto(Member member) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(member.getId());
        memberDto.setIdKTP(member.getIdKTP());
        memberDto.setName(member.getName());
        memberDto.setPhoneNumber(member.getPhoneNumber());
        memberDto.setDateOfBirth(member.getDateOfBirth());
        memberDto.setEmail(member.getEmail());
        memberDto.setGender(member.getGender());
        memberDto.setPassword(member.getPassword());
        memberDto.setNamePhoto(member.getNamePhoto());
        memberDto.setTypePhoto(member.getTypePhoto());
//        memberDto.setDataPhoto(member.getDataPhoto());


        memberDto.setPhoto(getImage(member.getNamePhoto()));
        memberDto.setCreatedAt(member.getCreatedAt().toString());
        if (null != member.getUpdatedAt()) {
            memberDto.setUpdatedAt(member.getUpdatedAt().toString());
        }
        return memberDto;
    }


}
