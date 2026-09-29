package gal.usc.etse.es.motorhome.model.dto;

public record User (
        String id,
        String name
) {
    public static User from(gal.usc.etse.es.motorhome.model.entity.User user) {
        return new User(
                user.getUsername(),
                user.getName()
        );
    }
}
