package com.kanifol.musicserver.service.exc;

import java.util.NoSuchElementException;

public abstract class CustomNoSuchElementException extends NoSuchElementException {
    public abstract String getCode();
    public abstract Object getDetails();
}
