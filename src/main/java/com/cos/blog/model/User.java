package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
// ORM -> Java(다른언어) Object -> 테이블로 매핑하는 기술 
@Entity // User 클래스가 프로젝트가 시작될 때 자동으로 MySQL에 테이블이 생성 된다. 
public class User {
	
	@Id // Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다. 
	private int id; // 오라클로 얘기하면 시퀀스, MySQL에서는 auto_increment
	
	@Column(nullable=false, length=30)
	private String username; // 아이디 
	
	@Column(nullable=false, length=100) //123456이면 6글자밖에 안되지만 넉넉하게--> 해쉬(비밀번호 암호화) 하여 넣을 것이기 때문
	private String password; 
	
	@Column(nullable=false, length=50)
	private String email; 
	
	@ColumnDefault("'user'") // 회원 디폴트 지정, 문자라는 것을 알려야 하므로 쌍따옴표 안에 작은 따옴표를 사용해야 한다. 
	private String role; // Enum을 쓰는 게 좋음. (어느 데이터의 도메인을 만들어줄 수 있음)  // admin, user, manager  Enum을 쓰면 저 세개로 도메인을 정해서 진행시켜줄 수 있다는 뜻. 
	
	@CreationTimestamp // 시간이 자동 입력 
	private Timestamp createDate;
}
