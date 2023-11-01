package com.cos.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// dto 사용 예를 보기 위해 만든 클래스입니다. 프로젝트와 무관한 클래스입니다.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReplySaveRequestDto {
	private int userId;
	private int boardId;
	private String content;
}
