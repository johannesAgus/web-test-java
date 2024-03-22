package com.test.java.service;

import com.test.java.base.utils.ImageUtils;
import com.test.java.base.utils.SearchUtils;
import com.test.java.base.utils.StringUtils;
import com.test.java.domain.Member;
import com.test.java.dto.LoginMemberAccountDto;
import com.test.java.dto.MemberDto;
import com.test.java.dto.MemberSearchDto;
import com.test.java.repository.MemberRepository;
import com.test.java.service.impl.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Slf4j
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Member save(MemberDto memberDto, MultipartFile multipartFile) throws IOException {

        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]+";

        if (!StringUtils.hasValue(memberDto.getEmail())) {
            throw new RuntimeException("Please enter email");
        }

        Member member = new Member();
        member.setName(memberDto.getName());
        if (StringUtils.hasValue(memberDto.getPassword())) {
            member.setPassword(bCryptPasswordEncoder.encode(memberDto.getPassword()));
        } else {
            member.setPassword(bCryptPasswordEncoder.encode("password"));
        }
        member.setPhoneNumber(memberDto.getPhoneNumber());
        member.setDateOfBirth(memberDto.getDateOfBirth());
        if (!Pattern.matches(emailRegex, memberDto.getEmail())) {
            throw new RuntimeException("Invalid Email Format");
        }
        member.setEmail(memberDto.getEmail());
        member.setGender(memberDto.getGender());
        member.setIdKTP(memberDto.getIdKTP());
        member.setEmail(memberDto.getEmail());
        member.setNamePhoto(multipartFile.getOriginalFilename());
        member.setTypePhoto(multipartFile.getContentType());
        member.setDataPhoto(multipartFile.getBytes());
        member.setCreatedAt(OffsetDateTime.now());

        return memberRepository.save(member);
    }

    @Override
    public Member update(MemberDto memberDto, MultipartFile multipartFile) throws IOException {

        Member member = memberRepository.findByIdAndDeleteFlagFalse(memberDto.getId()).orElseThrow(() -> new RuntimeException("No Member Id found with id : " + memberDto.getId()));

        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]+";

        if (!StringUtils.hasValue(memberDto.getEmail())) {
            throw new RuntimeException("Please enter email");
        }
        if (!member.getEmail().equalsIgnoreCase(memberDto.getEmail())) {
            if (memberRepository.findByEmailAndDeleteFlagFalse(memberDto.getEmail()).isPresent()) {
                throw new RuntimeException("Please choose another email address");
            } else {

                if (!Pattern.matches(emailRegex, memberDto.getEmail())) {
                    throw new RuntimeException("Invalid Email Format");
                }
                member.setEmail(memberDto.getEmail());
            }
        }
        member.setEmail(memberDto.getEmail());
        member.setName(memberDto.getName());
        member.setPhoneNumber(memberDto.getPhoneNumber());
        member.setDateOfBirth(memberDto.getDateOfBirth());
        member.setGender(memberDto.getGender());
        member.setIdKTP(memberDto.getIdKTP());
        member.setEmail(memberDto.getEmail());
        member.setNamePhoto(multipartFile.getOriginalFilename());
        member.setTypePhoto(multipartFile.getContentType());
        member.setDataPhoto(multipartFile.getBytes());
        member.setUpdatedAt(OffsetDateTime.now());

        return memberRepository.save(member);
    }

    @Override
    public void delete(String id) {
        Member member = memberRepository.findByIdAndDeleteFlagFalse(id).orElseThrow(() -> new RuntimeException("No Member Id found with id : " + id));
        member.setDeleteFlag(true);
        member.setUpdatedAt(OffsetDateTime.now());
        memberRepository.save(member);
    }

    @Override
    public List<Member> findList() {
        return memberRepository.findByDeleteFlagFalseOrderByNameAsc();
    }

    @Override
    public Optional<Member> findById(String id) {
        return memberRepository.findByIdAndDeleteFlagFalse(id);
    }

    @Override
    public byte[] getFilesPhoto(String namePhoto) {
        return memberRepository.findByNamePhoto(namePhoto).getDataPhoto();
    }

    @Override
    public Page<Member> findSearch(MemberSearchDto memberSearchDto) {
        return memberRepository.findAll((Specification<Member>) (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.hasValue(memberSearchDto.getId())) {
                predicateList.add(criteriaBuilder.like(root.get("id"), "%" + memberSearchDto.getId() + "%"));
            }
            if (StringUtils.hasValue(memberSearchDto.getName())) {
                predicateList.add(criteriaBuilder.like(root.get("name"), "%" + memberSearchDto.getName() + "%"));
            }
            predicateList.add(criteriaBuilder.isFalse(root.get("deleteFlag")));
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get("name")));
            return criteriaBuilder.and(criteriaBuilder.and(predicateList.toArray(new Predicate[0])));
        }, SearchUtils.toPageable(memberSearchDto));
    }

    @Override
    public Member login(LoginMemberAccountDto loginMemberAccountDto) {

        Member member;

        if (null != loginMemberAccountDto.getEmail() && null != loginMemberAccountDto.getPassword()){

            member = memberRepository.findByEmailAndDeleteFlagFalse(loginMemberAccountDto.getEmail()).orElseThrow(() -> new ValidationException("Email or Password doesn't match/exist"));

        } else {
            throw new RuntimeException("Please enter the Username/Email or Password");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean passwordMatch = passwordEncoder.matches(loginMemberAccountDto.getPassword(), member.getPassword());
        if (!passwordMatch) {
            throw new RuntimeException("Username or Password doesn't match/exist");
        }

        return member;
    }
}
