package com.example.securityapplication.config;

import com.example.securityapplication.model.enums.UserRoleEnum;
import com.example.securityapplication.repository.UserRepository;
import com.example.securityapplication.service.AppUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {


    //We have to expose 3 things
    //1.PasswordEncoder
    //2.SecurityFilterChain
    //3. UserDetailsService


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                //define which requests are allowed and which not
                .authorizeRequests()
                //everyone can download static resources(css, js, images)
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                //everyone can login and register
                .antMatchers("/", "/users/login", "/users/register").permitAll()
                //page available for moderators and admins
                .antMatchers("/pages/moderators").hasRole(UserRoleEnum.MODERATOR.name())
                //page available for admins
                .antMatchers("/pages/admins").hasRole(UserRoleEnum.ADMIN.name())
                //all other pages are available for logged users
                .anyRequest().authenticated()
        .and()
                //configuration of the login form (custom)
                .formLogin().loginPage("/users/login")
                //username form field name
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                //password form field name
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/")
                //logout must be post request
                .failureForwardUrl("/users/login-error")
        .and()
                .logout()
                .logoutUrl("/users/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID");


        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new AppUserDetailsService(userRepository);
    }

}
