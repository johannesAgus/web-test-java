package com.test.java.repository;

import com.test.java.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, String>, JpaSpecificationExecutor<UserAccount> {

    Optional<UserAccount> findByIdAndUserStatus (String id, String userStatus);

    Optional<UserAccount> findByUsernameAndUserStatus (String username, String userStatus);

    Optional<UserAccount> findByEmailAndUserStatus (String email, String userStatus);

}
