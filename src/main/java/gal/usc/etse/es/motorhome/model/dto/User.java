package gal.usc.etse.es.motorhome.model.dto;

import java.util.Set;

public record User(
        String id,
        String token,
        Set<String> roles,
        String name) {
    public static User from(gal.usc.etse.es.motorhome.model.entity.User user) {
        return new User(
                user.getUsername(),
                null,
                java.util.Collections.emptySet(),
                user.getName());
    }
}
