package com.test.java.service;

import com.test.java.base.utils.SearchUtils;
import com.test.java.base.utils.StringUtils;
import com.test.java.domain.UserAccount;
import com.test.java.domain.UserStatus;
import com.test.java.dto.LoginUserAccountDto;
import com.test.java.dto.UserAccountDto;
import com.test.java.dto.UserAccountSearchDto;
import com.test.java.repository.UserAccountRepository;
import com.test.java.service.impl.UserAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import javax.validation.ValidationException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class UserAccountServiceImpl implements UserAccountService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<UserAccount> findById(String id) {
        return userAccountRepository.findByIdAndUserStatus(id, "ACTIVE");
    }

    @Override
    public UserAccount save(UserAccountDto userAccountDto) {

        if (userAccountRepository.findByUsernameAndUserStatus(userAccountDto.getUsername(), "ACTIVE").isPresent()) {
            throw new RuntimeException("Username already registered");
        }

        UserAccount userAccount = new UserAccount();
        userAccount.setName(userAccountDto.getName());
        userAccount.setUsername(userAccountDto.getUsername());
        userAccount.setEmail(userAccountDto.getEmail());
        if (StringUtils.hasValue(userAccountDto.getPassword())) {
            userAccount.setPassword(bCryptPasswordEncoder.encode(userAccountDto.getPassword()));
        } else {
            userAccount.setPassword(bCryptPasswordEncoder.encode("password"));
        }
        userAccount.setUserStatus(UserStatus.ACTIVE.name());
        userAccount.setCreatedAt(OffsetDateTime.now());
        return userAccountRepository.save(userAccount);
    }

    @Override
    public UserAccount update(UserAccountDto userAccountDto) {

        UserAccount userAccount = userAccountRepository.findByIdAndUserStatus(userAccountDto.getId(), "ACTIVE").orElseThrow(() -> new RuntimeException("No User found with id : " + userAccountDto.getId()));
        userAccount.setName(userAccountDto.getName());
        if (userAccountRepository.findByUsernameAndUserStatus(userAccountDto.getUsername(), "ACTIVE").isPresent()) {
            throw new RuntimeException("Username already registered");
        }
        userAccount.setUsername(userAccountDto.getUsername());
        userAccount.setEmail(userAccountDto.getEmail());
        if (StringUtils.hasValue(userAccountDto.getPassword())) {
            userAccount.setPassword(bCryptPasswordEncoder.encode(userAccountDto.getPassword()));
        }
        userAccount.setUpdatedAt(OffsetDateTime.now());
        return userAccountRepository.save(userAccount);
    }

    @Override
    public void delete(String id) {
        UserAccount userAccount = userAccountRepository.findByIdAndUserStatus(id, "ACTIVE").orElseThrow(() -> new RuntimeException("No User found with id : " + id));
        userAccount.setUpdatedAt(OffsetDateTime.now());
        userAccount.setUserStatus(UserStatus.INACTIVE.name());
        userAccountRepository.save(userAccount);
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        return userAccountRepository.findByUsernameAndUserStatus(username, "ACTIVE");
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        return userAccountRepository.findByEmailAndUserStatus(email, "ACTIVE");
    }

    @Override
    public UserAccount login(LoginUserAccountDto loginUserAccountDto) {

        UserAccount userAccount;
        if (null != loginUserAccountDto.getUsername() && null != loginUserAccountDto.getPassword()) {
            userAccount = userAccountRepository.findByUsernameAndUserStatus(loginUserAccountDto.getUsername(), "ACTIVE").orElseThrow(() -> new ValidationException("Username or Password doesn't match/exist"));
        } else {
            throw new RuntimeException("Please enter the Username/Email or Password");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean passwordMatch = passwordEncoder.matches(loginUserAccountDto.getPassword(), userAccount.getPassword());
        if (!passwordMatch) {
            throw new RuntimeException("Username or Password doesn't match/exist");
        }

        return userAccount;
    }

    @Override
    public Page<UserAccount> findSearch(UserAccountSearchDto userAccountSearchDto) {
        return userAccountRepository.findAll((Specification<UserAccount>) (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicateList = new ArrayList<>();

            if (StringUtils.hasValue(userAccountSearchDto.getUsername())) {
                predicateList.add(criteriaBuilder.like(root.get("username"), "%" + userAccountSearchDto.getUsername() + "%"));
            }

            predicateList.add(criteriaBuilder.equal(root.get("userStatus"), "ACTIVE"));

            criteriaQuery.distinct(true);

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, SearchUtils.toPageable(userAccountSearchDto));
    }
}
