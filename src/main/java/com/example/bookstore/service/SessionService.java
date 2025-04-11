package com.example.bookstore.service;

import com.example.bookstore.entity.Session;
import com.example.bookstore.entity.User;
import com.example.bookstore.repository.SessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionService {
  private final SessionRepository sessionRepository;

  public void startSession(User user) {
    if (null == user.getSession()) {
      final Session session = new Session(null, user, false);
      sessionRepository.save(session);
      return;
    }
    final Session existingSession =
        sessionRepository.findById(user.getSession().getId()).orElse(null);
    assert existingSession != null;
    existingSession.setRevoked(false);
  }

  public boolean sessionInactive(User user) {
    if (user.getSession() == null) throw new RuntimeException("Session was never created.");
    final Session existingSession =
        sessionRepository.findById(user.getSession().getId()).orElse(null);
    assert existingSession != null;
    return existingSession.getRevoked();
  }

  @Transactional
  public void endSession(User user) {
    if (user.getSession() == null) throw new RuntimeException("Session was never created.");
    final Session existingSession =
        sessionRepository.findById(user.getSession().getId()).orElse(null);
    assert existingSession != null;
    System.out.println(existingSession.getRevoked());
    existingSession.setRevoked(true);
    sessionRepository.save(existingSession);
    System.out.println(existingSession.getRevoked());
  }
}
