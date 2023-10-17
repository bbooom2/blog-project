package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
//@AllArgsConstructor // 전체생성자 
@NoArgsConstructor // bean 생성자 
public class Member { // final은 불변성을 위해서 사용 
	private int id;
	private String username;
	private String password;
	private String email;
	
	@Builder 
	public Member(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}

	
	
}
