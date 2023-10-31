package com.cos.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class KakaoProfile {
	public Long id;
	public String connected_at;
	public Properties properties;
	public KakaoAccount kakao_account;

	@Data
	@JsonIgnoreProperties(ignoreUnknown=true)
	public class Properties {
		public String nickname;
	}

	@Data
	@JsonIgnoreProperties(ignoreUnknown=true)
	public class KakaoAccount {
		public Boolean profile_nickname_needs_agreement;
		public Profile profile;
		public Boolean has_email;
		public Boolean email_needs_agreement;
		public String email;

		@Data
		@JsonIgnoreProperties(ignoreUnknown=true)
		public class Profile {
			public String nickname;
			
		}
	}
}
