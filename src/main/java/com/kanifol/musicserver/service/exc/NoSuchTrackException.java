package com.kanifol.musicserver.service.exc;

public class NoSuchTrackException extends CustomNoSuchElementException {
    private Long trackId;
    private String title;
    public NoSuchTrackException(Long trackId) {
        this.trackId = trackId;
    }
    public NoSuchTrackException(String title) {
        this.title = title;
    }
    @Override
    public String getCode() {
        return "exception.no-such-track-message";
    }
    @Override
    public Object getDetails() {
        return trackId == null ? title : trackId;
    }
}
