package com.example.placemeeting.repository;

import com.example.placemeeting.domain.Heart;
import com.example.placemeeting.domain.Member;
import com.example.placemeeting.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRepository extends JpaRepository<Heart, Long> {

    Heart findByPostAndMember(Post post, Member member);
}
