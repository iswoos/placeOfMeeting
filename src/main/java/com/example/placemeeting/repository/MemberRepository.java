package com.example.placemeeting.repository;


import com.example.placemeeting.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByuserId(String userId);
}
