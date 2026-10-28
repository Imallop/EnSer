package gal.usc.etse.es.motorhome.service;

import gal.usc.etse.es.motorhome.exception.InvalidRefreshTokenException;
import gal.usc.etse.es.motorhome.model.entity.RefreshToken;
import gal.usc.etse.es.motorhome.model.dto.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final KeyPair keyPair;
    private final gal.usc.etse.es.motorhome.repository.UserRepository userRepository;
    private final gal.usc.etse.es.motorhome.repository.RefreshTokenRepository refreshTokenRepository;

    @Value("${auth.jwt.ttl:PT15M}")
    private Duration tokenTTL;

    @Value("${auth.refresh.ttl:PT72H}")
    private Duration refreshTTL;

    @Autowired
    public AuthenticationService(
            AuthenticationManager authenticationManager,
            KeyPair keyPair,
            gal.usc.etse.es.motorhome.repository.UserRepository userRepository,
            gal.usc.etse.es.motorhome.repository.RefreshTokenRepository refreshTokenRepository) {
        this.authenticationManager = authenticationManager;
        this.keyPair = keyPair;
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public User login(gal.usc.etse.es.motorhome.model.dto.LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()));
        var user = userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new UsernameNotFoundException(auth.getName()));
        return issueToken(user);
    }

    private User issueToken(gal.usc.etse.es.motorhome.model.entity.User user) {
        Instant now = Instant.now();
        String token = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(tokenTTL)))
                .notBefore(Date.from(now))
                .signWith(keyPair.getPrivate())
                .compact();
        return new User(user.getUsername(), token, user.getName());
    }

    public User login(String refreshToken) throws InvalidRefreshTokenException {
        Optional<RefreshToken> token = refreshTokenRepository.findById(refreshToken);

        if (token.isPresent()) {
            var user = userRepository.findByUsername(token.get().getUser())
                    .orElseThrow(() -> new UsernameNotFoundException(token.get().getUser()));

            return issueToken(user);
        }

        throw new InvalidRefreshTokenException(refreshToken);
    }

    public String regenerateRefreshToken(gal.usc.etse.es.motorhome.model.entity.User user) {
        UUID uuid = UUID.randomUUID();
        RefreshToken refreshToken = new RefreshToken(uuid.toString(), user.getUsername(), refreshTTL.toSeconds());
        refreshTokenRepository.deleteAllByUser(user.getUsername());
        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public void invalidateTokens(String username) {
        refreshTokenRepository.deleteAllByUser(username);
    }

    public User parseJWT(String token) throws JwtException {
        Claims claims = Jwts.parser()
                .verifyWith(keyPair.getPublic())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String username = claims.getSubject();
        var user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return User.from(user.get());
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }

    public RoleHierarchy loadRoleHierarchy() {
        // Roles removed: return empty hierarchy
        return RoleHierarchyImpl.withRolePrefix("").build();
    }
}
