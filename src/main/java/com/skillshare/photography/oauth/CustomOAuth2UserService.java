package com.skillshare.photography.oauth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.skillshare.photography.model.User;
import com.skillshare.photography.repository.UserRepository;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        
        // Process the OAuth2 user and either create or update local user record
        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(provider, oAuth2User.getAttributes());
        
        User user = userRepository.findByEmail(userInfo.getEmail())
                .orElseGet(() -> registerNewUser(provider, userInfo));
        
        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(String provider, OAuth2UserInfo userInfo) {
        User user = new User();
        user.setOauthProvider(provider);
        user.setOauthId(userInfo.getId());
        user.setUsername(userInfo.getName());
        user.setEmail(userInfo.getEmail());
        user.setProfilePictureUrl(userInfo.getImageUrl());
        return userRepository.save(user);
    }
}
