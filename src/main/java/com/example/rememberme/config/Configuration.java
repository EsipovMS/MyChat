package com.example.rememberme.config;

import com.example.rememberme.model.Person;
import com.example.rememberme.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import java.util.List;

@org.springframework.context.annotation.Configuration
@RequiredArgsConstructor
public class Configuration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .and()
                .rememberMe()
                .and()
                .logout()
                .logoutUrl("/logout");
    }


    @org.springframework.context.annotation.Configuration
    @RequiredArgsConstructor
    protected static class AuthenticationConfiguration extends
            GlobalAuthenticationConfigurerAdapter {
        private final PersonRepository personRepository;


        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {
            List<Person> personList = personRepository.findAll();
            auth
                    .inMemoryAuthentication()
                    .passwordEncoder(NoOpPasswordEncoder.getInstance())
                    .withUser(personList.get(0).getLogin()).password(personList.get(0).getPassword()).roles("USER")
                    .and()
                    .withUser(personList.get(1).getLogin()).password(personList.get(1).getPassword()).roles("USER");

        }

    }
}
