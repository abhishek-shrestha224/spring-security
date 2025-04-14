package com.example.bookstore;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/oauth/callback")
@RequiredArgsConstructor
public class OAuthController {
  @Value("${oauth2.provider.google.client-id}")
  private String CLIENT_ID;

  @Value("${oauth2.provider.google.client-secret}")
  private String CLIENT_SECRET;

  @Value("${oauth2.provider.google.grant-type}")
  private String GRANT_TYPE;

  @Value("${oauth2.provider.google.uri.token-endpoint}")
  private String TOKEN_ENDPOINT;

  @Value("${oauth2.provider.google.uri.redirect-uri}")
  private String REDIRECT_URI;

  @Value("${oauth2.provider.google.uri.user-info-endpoint}")
  private String USER_INFO_ENDPOINT;

  private final RestClient restClient;

  @GetMapping("google")
  public ResponseEntity<?> googleCallback(@RequestParam("code") String code) {
    try {
      System.out.println(code);
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      System.out.println(params);

      params.add("grant_type", GRANT_TYPE);
      params.add("code", code);
      params.add("client_id", CLIENT_ID);
      params.add("client_secret", CLIENT_SECRET);
      params.add("redirect_uri", REDIRECT_URI);

      ResponseEntity<Map> tokenResponse =
          restClient
              .post()
              .uri(TOKEN_ENDPOINT)
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .body(params)
              .retrieve()
              .toEntity(Map.class);

      System.out.print(tokenResponse);
      String idToken = (String) tokenResponse.getBody().get("id_token");

      ResponseEntity<Map> userInfoResponse =
          restClient.get().uri(USER_INFO_ENDPOINT + idToken).retrieve().toEntity(Map.class);

      System.out.println(userInfoResponse);

      return ResponseEntity.ok().body(userInfoResponse.getBody().get("email"));

      /*TODO: Here add other functionalities like check db create user if not exists, issue your own token add to security context etc.*/

    } catch (Exception ex) {
      return ResponseEntity.internalServerError().body("Something Went Wrong");
    }
  }
}