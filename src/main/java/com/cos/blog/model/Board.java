package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder 
@Entity
public class Board {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob // 대용량 데이터 
	private String content; // 섬머노트 라이브러리 사용 예정. <html> 태그가 섞여서 디자인이 됨. 
	
	@ColumnDefault("0") // 숫자이기 때문에 추가로 홑따옴표가 필요하지 않다. 
	private int count; // 조회수 
	
	@ManyToOne(fetch = FetchType.EAGER) // Many = Board, User = One 한명의 사용자는 여러 게시글을 올릴 수 있음. 
	@JoinColumn(name="userId")
	private User user; // DB는 오브젝트를 저장할 수 없다. FK, 자바는 오브젝트를 저장할 수 있다. 
	
	// mappedBy = 연관관계의 주인이 아니므로 (FK 아님) DB에 칼럼을 만들지 말라는 의미. 
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER) 
	private List<Reply> reply; 
	
	@CreationTimestamp
	private Timestamp createDate;
}
