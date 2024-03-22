package com.test.java.repository;

import com.test.java.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, JpaSpecificationExecutor<Member> {

    Optional<Member> findByIdAndDeleteFlagFalse (String id);

    Optional<Member> findByEmailAndDeleteFlagFalse(String email);

    Optional<Member> findByNameAndDeleteFlagFalse (String email);

    List<Member> findByDeleteFlagFalseOrderByNameAsc();

    Member findByNamePhoto(String namePhoto);

}
