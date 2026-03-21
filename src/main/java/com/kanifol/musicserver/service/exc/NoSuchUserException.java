package com.kanifol.musicserver.service.exc;

public class NoSuchUserException extends CustomNoSuchElementException {

    private String  username;
    private Long id;

    public NoSuchUserException(Long id) {
        this.id = id;
    }

    public NoSuchUserException(String username) {
        this.username = username;
    }

    @Override
    public String getCode() {
        return "exception.no-such-user-message";
    }

    @Override
    public Object getDetails() {
        return id == null ? username : id;
    }
}
