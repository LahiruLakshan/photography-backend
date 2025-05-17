package com.skillshare.photography.oauth;

import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2UserInfo(attributes);
        }
        // Add other OAuth2 providers here as needed
        throw new OAuth2AuthenticationException("Sorry! Login with " + registrationId + " is not supported yet.");
    }
}