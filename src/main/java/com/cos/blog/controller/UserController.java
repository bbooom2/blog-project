package com.cos.blog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class UserController {
	
	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	
	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) { // Data를 리턴해주는 컨트롤러 함수 
		// POST 방식으로 key=value 데이터를 요청해야 한다. (카카오쪽으로) 
		// a 태그를 활용하는 건 무조건 GET 방식 
		// 이때 필요한 게 RestTemplate 
		RestTemplate rt = new RestTemplate();
		
		//HttpHeader 오브젝트 생성 
		HttpHeaders headers = new HttpHeaders();
		headers.add("ContentType", "application/x-www-form-urlencoded;charset=utf-8"); // 내가 전송한 바디 데이터가 키 밸류 형태의 데이터라는 것을 알려주는 것 
		
		// HttpBody 오브젝트 생성 
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		
		params.add("grant_type", "authorization_code");
		params.add("client_id", "532b578db97f0e167c55932799b2cc3b");
		params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
		params.add("code", code);
		
		// HttpHeader와 HttpBodey를 하나의 오브젝트에 담기 (exchange가 HttpEntity를 받게 되어 있기 때문)
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
				new HttpEntity<>(params, headers);
		
		// Http 요청하기 - Post 방식으로 - 그리고 response 변수의 응답 받음 
		ResponseEntity<String> response = rt.exchange(
				
				"https://kauth.kakao.com/oauth/token",
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		
		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		OAuthToken oauthToken  = null;
		
		try {
			oauthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		//System.out.println("카카오 엑세스 토큰 : " + oauthToken.getAccess_token());
		
		RestTemplate rt2 = new RestTemplate();
		
		//HttpHeader 오브젝트 생성 
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + oauthToken.getAccess_token());
		headers2.add("ContentType", "application/x-www-form-urlencoded;charset=utf-8"); // 내가 전송한 바디 데이터가 키 밸류 형태의 데이터라는 것을 알려주는 것 

		
		// HttpHeader와 HttpBodey를 하나의 오브젝트에 담기 (exchange가 HttpEntity를 받게 되어 있기 때문)
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 =
				new HttpEntity<>(headers2); // 오버로딩 되기 때문에 params, headers 두개 다 넣어도 되고 headers만 넣어도 된다. 
		
		// Http 요청하기 - Post 방식으로 - 그리고 response 변수의 응답 받음 
		ResponseEntity<String> response2 = rt2.exchange(
				
				"https://kapi.kakao.com/v2/user/me",
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
		);
		
		
		ObjectMapper objectMapper2 = new ObjectMapper();
		KakaoProfile kakaoProfile  = null;
		
		try {
			kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		
		System.out.println("카카오 아이디(번호) : "  + kakaoProfile.getId());
	    System.out.println("카카오 이메일 : "  + kakaoProfile.getKakao_account().getEmail());
	    
	    System.out.println("블로그 서버 유저네임 : "  + kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId());
	    System.out.println("블로그 서버 이메일 : "  + kakaoProfile.getKakao_account().getEmail());
	    
	    // UUID란 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘 -> 이걸 쓰면 로그인 할 때마다 바뀌어있는 패스워드를 확인할 수 없기 때문에 로그인 불가능 
	    System.out.println("블로그 서버 패스워드 : "  + cosKey);
	    
	 // User 오브젝트 : username, password, email
	    User kakaoUser = User.builder()
	    		.username(kakaoProfile.getKakao_account().getEmail()+"_"+kakaoProfile.getId())
	    		.password("cos1234")
	    		.email(kakaoProfile.getKakao_account().getEmail())
	    		.build();
	    
	    
		// 가입자 혹은 비가입자 체크해서 처리 
	    User originUser = userService.회원찾기(kakaoUser.getUsername());	    	
	    
	    if(originUser.getUsername() == null) {
	    	System.out.println("기존 회원이 아니기에 자동 회원가입을 진행합니다.");
	    	userService.회원가입(kakaoUser);
	    }
	    
	    System.out.println("자동 로그인을 진행합니다.");
	    // 로그인 처리 
	    Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), kakaoUser.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	    
		return "redirect:/";
	}
	
	@GetMapping("/user/updateForm")
	public String updateForm() {
		return "user/updateForm";
	}

}
