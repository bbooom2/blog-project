package com.cos.blog.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

// 스프링이 컴포넌트 스캔을 통해 Bean에 등록을 해줌. IoC를 해준다. 
@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
		
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional
	public User 회원찾기(String username) {
		User user =userRepository.findByUsername(username).orElseGet(()->{ // 만약에 회원을 찾았는데 없으면 빈 객체를 리턴한다. 
			return new User();
		});
		return user;
	}
	
	@Transactional
	public void 회원가입(User user) {
		String rawPassword  = user.getPassword(); // 1234 원문
		String endPassword = encoder.encode(rawPassword); // 해쉬
		user.setPassword(endPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}
	
	@Transactional
	public void 회원수정(User user) { // 외부로부터 받은 user 
		User persistance = userRepository.findById(user.getId()).orElseThrow(()-> {
			return new IllegalArgumentException("회원 찾기 실패");
		});
		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		persistance.setPassword(encPassword);
		persistance.setEmail(user.getEmail());
	}
}
