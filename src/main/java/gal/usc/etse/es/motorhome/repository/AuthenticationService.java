package gal.usc.etse.es.motorhome.repository;

import gal.usc.etse.es.motorhome.exception.InvalidRefreshTokenException;
import gal.usc.etse.es.motorhome.model.dto.User;
import gal.usc.etse.es.motorhome.model.entity.RefreshToken;
// roles removed: no RoleRepository dependency
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.resilience.annotation.ConcurrencyLimit;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

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

    @Retryable(maxRetries = 3, delay = 300, jitter = 50, timeUnit = TimeUnit.MILLISECONDS, multiplier = 2)
    @ConcurrencyLimit(10)
    public gal.usc.etse.es.motorhome.model.dto.User login(gal.usc.etse.es.motorhome.model.entity.User user)
            throws AuthenticationException {
        Authentication auth = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(user.getUsername(), user.getPassword()));

        List<String> roles = auth.getAuthorities()
                .stream()
                .filter(authority -> authority instanceof SimpleGrantedAuthority)
                .map(GrantedAuthority::getAuthority)
                .toList();

        String token = Jwts.builder()
                .subject(auth.getName())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(tokenTTL)))
                .notBefore(Date.from(Instant.now()))
                .claim("roles", roles)
                .signWith(keyPair.getPrivate())
                .compact();

        return new gal.usc.etse.es.motorhome.model.dto.User(user.getUsername(), token,
                user.getName());
    }

    public User login(String refreshToken) throws InvalidRefreshTokenException {
        Optional<RefreshToken> token = refreshTokenRepository.findByToken(refreshToken);

        if (token.isPresent()) {
            var user = userRepository.findByUsername(token.get().getUser())
                    .orElseThrow(() -> new UsernameNotFoundException(token.get().getUser()));

            return login(user);
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
