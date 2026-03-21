package com.kanifol.musicserver.service.exc;

public class NoSuchGenreException extends CustomNoSuchElementException {
    private final String name;

    public NoSuchGenreException(String name) {
        this.name = name;
    }

    @Override
    public String getCode() {
        return "exception.no-such-genre-message";
    }

    @Override
    public Object getDetails() {
        return name;
    }
}
