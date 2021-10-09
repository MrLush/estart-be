package com.epam.estart.security;

import com.epam.estart.entity.UserEntity;
import com.epam.estart.exception.SecurityException;
import com.epam.estart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static com.epam.estart.dto.error.ServiceError.NO_SUCH_ENTITY;

@RequiredArgsConstructor
@Component
public class JwtFilter extends GenericFilterBean {

  private final JwtProvider jwtProvider;
  private final UserRepository userRepository;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response,
                       FilterChain chain) throws IOException, ServletException {
    String token = jwtProvider.getTokenFromRequest((HttpServletRequest) request).get();
    if (jwtProvider.validateToken(token)) {
      String email = jwtProvider.getEmailFromToken(token);
      UserEntity userEntity = userRepository.findByEmailIgnoreCase(email)
          .orElseThrow(() -> new SecurityException(NO_SUCH_ENTITY, "Entity with email " + email + " does not exist."));
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
          new AuthorisedUser(userEntity.getId(), userEntity.getEmail()), null, null);
      SecurityContextHolder.getContext().setAuthentication(auth);
    }
    chain.doFilter(request, response);
  }
}
