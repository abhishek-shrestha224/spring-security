package com.example.bookstore;

import jakarta.servlet.http.HttpSession;
import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  private final ClientRegistrationRepository clientRegistrationRepository;
  private final HttpSession session;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/login/**", "/oauth2/**")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .oauth2Login(
            oauth2 ->
                oauth2.authorizationEndpoint(
                    authorization ->
                        authorization.authorizationRequestResolver(
                            authorizationRequestResolver(clientRegistrationRepository, session))))
        .build();
  }

  private OAuth2AuthorizationRequestResolver authorizationRequestResolver(
      final ClientRegistrationRepository clientRegistrationRepository, final HttpSession session) {

    DefaultOAuth2AuthorizationRequestResolver authorizationRequestResolver =
        new DefaultOAuth2AuthorizationRequestResolver(
            clientRegistrationRepository, "/oauth2/authorization");
    authorizationRequestResolver.setAuthorizationRequestCustomizer(
        authorizationRequestCustomizer(session));

    return authorizationRequestResolver;
  }

  private Consumer<OAuth2AuthorizationRequest.Builder> authorizationRequestCustomizer(
      HttpSession session) {
    return customizer -> {
      String randomState = generateState();
      customizer.state(randomState);
      customizer.redirectUri("http://localhost:8080/login/oauth2/callback/google");
      session.setAttribute("OAUTH2_STATE", randomState);
    };
  }

  private String generateState() {
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    StringBuilder sb = new StringBuilder(3);
    Random random = new SecureRandom();
    for (int i = 0; i < 3; i++) {
      sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
    }
    return sb.toString();
  }
}