package com.nikita.development.rf.security.filter;

public class Constants {

	public static final long REFRESH_TOKEN_VALIDITY_SECONDS = 30*24*60*60;
    public static final long ACCESS_TOKEN_VALIDITY_SECONDS = 20;//5*60*60;
    public static final String SIGNING_KEY = "signinngkey";
    public static final String SIGNING_REFRESH_KEY = "signinngrefreshkey";
    public static final String TOKEN_PARAM = "auth_token";
    public static final String homeUrl = "http://localhost:8080/home";
}
