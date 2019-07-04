package com.nikita.development.rf.security.model;


public class TokenResponse {
	
	String accessToken;
	String refreshToken;
	
	public TokenResponse(String s, String r){
		accessToken = s;
		refreshToken = r;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(String s) {
		accessToken = s;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}
	
	public void setRefreshToken(String s) {
		refreshToken = s;
	}


}
