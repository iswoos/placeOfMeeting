package com.example.placemeeting.security.user;

import com.example.placemeeting.domain.Member;
import com.example.placemeeting.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        Member account = accountRepository.findByuserName(userName).orElseThrow(
                () -> new RuntimeException("Not Found Account")
        );

        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setAccount(account);

        return userDetails;
    }
}
