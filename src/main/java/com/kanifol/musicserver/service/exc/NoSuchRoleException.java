package com.kanifol.musicserver.service.exc;

public class NoSuchRoleException extends CustomNoSuchElementException {

    private final String roleName;

    public NoSuchRoleException(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String getCode() {
        return "exception.no-such-role-message";
    }

    @Override
    public Object getDetails() {
        return roleName;
    }
}
