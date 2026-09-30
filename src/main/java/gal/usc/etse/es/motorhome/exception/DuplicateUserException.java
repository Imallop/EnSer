package gal.usc.etse.es.motorhome.exception;

import gal.usc.etse.es.motorhome.model.entity.User;

public class DuplicateUserException extends Exception {
    private final User user;

    public DuplicateUserException(User user) {
        super("User already exists!");
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
