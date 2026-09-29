package gal.usc.etse.es.motorhome.service;

import gal.usc.etse.es.motorhome.model.dto.User;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthenticationService {

    public User parseJWT(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token JWT vacío");
        }

        return new User(
                "demo-user",
                "Demo User");
    }

    public RoleHierarchy loadRoleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER\nROLE_USER > ROLE_GUEST");
    }

    public List<String> extractRoles(String token) {
        return Arrays.asList("USER");
    }
}
