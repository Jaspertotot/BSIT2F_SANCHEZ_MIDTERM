package com.library.dao;

/** Wraps SQLException so the UI layer never needs java.sql. */
public class DaoException extends RuntimeException {
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }
}
