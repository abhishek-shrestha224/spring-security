package com.example.bookstore;

import jakarta.servlet.http.HttpSession;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/oauth/callback")
@RequiredArgsConstructor
public class OAuthController {
  private final ClientRegistrationRepository clientRepository;
  private final RestClient restClient;

  @GetMapping("/login/oauth2/callback/{registrationId}")
  public ResponseEntity<Object> handleCallback(
      @PathVariable final String registrationId,
      @RequestParam("code") final String authorizationCode,
      @RequestParam("state") final String state,
      HttpSession session) {
    String originalState = (String) session.getAttribute("OAUTH2_STATE");
    session.removeAttribute("OAUTH2_STATE");

    if (!state.equals(originalState)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid state parameter");
    }

    final String grantType =
        clientRepository
            .findByRegistrationId(registrationId)
            .getAuthorizationGrantType()
            .toString();
    final String clientId = clientRepository.findByRegistrationId(registrationId).getClientId();
    final String clientSecret =
        clientRepository.findByRegistrationId(registrationId).getClientSecret();
    final String redirectUri =
        clientRepository.findByRegistrationId(registrationId).getRedirectUri();
    final String tokenUri =
        clientRepository.findByRegistrationId(registrationId).getProviderDetails().getTokenUri();
    final String userInfoUri =
        clientRepository
            .findByRegistrationId(registrationId)
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUri();
    try {
      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("grant_type", grantType);
      params.add("code", authorizationCode);
      params.add("client_id", clientId);
      params.add("client_secret", clientSecret);
      params.add("redirect_uri", redirectUri);

      ResponseEntity<Map> tokenResponse =
          restClient
              .post()
              .uri(tokenUri)
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .body(params)
              .retrieve()
              .toEntity(Map.class);

      System.out.print(tokenResponse);
      assert tokenResponse.getBody() != null;
      String idToken = (String) tokenResponse.getBody().get("id_token");

      ResponseEntity<Map> userInfoResponse =
          restClient.get().uri(userInfoUri + idToken).retrieve().toEntity(Map.class);

      System.out.println(userInfoResponse);

      assert userInfoResponse.getBody() != null;
      return ResponseEntity.ok().body(userInfoResponse.getBody().get("email"));

      /*TODO: Here add other functionalities like check db create user if not exists, issue your own token add to security context etc.*/

    } catch (Exception ex) {
      return ResponseEntity.internalServerError().body("Something Went Wrong");
    }
  }
}