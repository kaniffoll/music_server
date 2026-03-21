package com.kanifol.musicserver.service.exc;

public class NoSuchAlbumException extends CustomNoSuchElementException {
    private Long id;
    private String title;
    public NoSuchAlbumException(Long id) {
        this.id = id;
    }
    public NoSuchAlbumException(String title) {
        this.title = title;
    }
    @Override
    public String getCode() {
        return "exception.no-such-album-message";
    }
    @Override
    public Object getDetails() {
        return id == null ? title : id;
    }
}
