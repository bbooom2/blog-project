package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder  // 빌더 패턴 
//ORM -> Java(다른언어) Object -> 테이블로 매핑하는 기술 
@Entity // User 클래스가 프로젝트가 시작될 때 자동으로 MySQL에 테이블이 생성 된다. 
@DynamicInsert // insert시 null인 필드를 제외시켜 준다. 
public class User {
	
	@Id  // Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다. 
	private int id; // 오라클로 얘기하면 시퀀스, MySQL에서는 auto_increment
	
	@Column(nullable = false, length = 30, unique = true)
	private String username; // 아이디 
	
	@Column(nullable = false, length = 100) //123456이면 6글자밖에 안되지만 넉넉하게--> 해쉬(비밀번호 암호화) 하여 넣을 것이기 때문
	private String password; 
	
	@Column(nullable = false, length = 50)
	private String email;
	
	//@ColumnDefault("user") 
	@Enumerated(EnumType.STRING)
	private RoleType role; // Enum을 쓰는 게 좋음. (어느 데이터의 도메인을 만들어줄 수 있음)  // ADMIN, USER
	
	@CreationTimestamp // 시간이 자동 입력 
	private Timestamp createDate;
}
