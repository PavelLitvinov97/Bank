package com.Faust.Bank;

import com.Faust.Bank.Entities.Issue;
import com.Faust.Bank.Entities.User;
import com.Faust.Bank.Repositories.IssueRepository;
import com.Faust.Bank.Repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;



@Component
@Slf4j
public class DemoData implements CommandLineRunner {



    @Autowired
    IssueRepository issueRepository;

    @Autowired
    UserRepository users;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        log.debug("initializing data...");
        Arrays.asList("issue1", "issue2").forEach(v -> this.issueRepository.saveAndFlush(Issue._builder().build()));

        this.users.save(User.builder()
                .username("jester")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER"))
                .build()
        );

        this.users.save(User.builder()
                .username("emperor")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER", "ROLE_EMPEROR"))
                .build()
        );

        this.users.save(User.builder()
                .username("treasurer")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER", "ROLE_TREASURER"))
                .build()
        );

        this.users.save(User.builder()
                .username("provider")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER", "ROLE_PROVIDER"))
                .build()
        );

    }
}