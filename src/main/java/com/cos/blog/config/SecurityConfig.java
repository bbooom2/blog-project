package com.cos.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

	@Configuration // IoC
	public class SecurityConfig {
	
	 @Bean // IoC가 된다.
	 BCryptPasswordEncoder encode() { // 시큐리티가 들고 있는 함수. 
	  return new BCryptPasswordEncoder();
	 }

	 @Bean
	 SecurityFilterChain configure(HttpSecurity http) throws Exception {
	  http
	  	.csrf().disable() // csrf 토큰 비활성화 (테스트시 걸어두는 게 좋음) 
	    .authorizeRequests()
	   		.antMatchers("/","/auth/**", "/js/**", "/css/**", "image/**")
	   		.permitAll()
	   		.anyRequest()
	       .authenticated()
	    .and()
	       .formLogin()
	       .loginPage("/auth/loginForm")
	  	   .loginProcessingUrl("auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청 오는 로그인을 가로채서 대신 로그인 해준다.
	  	   .defaultSuccessUrl("/");
	  return http.build();
	 }
	}