package com.att.tdp.popcorn_palace.exception;

public class ShowtimeOverlapException extends RuntimeException {
    public ShowtimeOverlapException(String message) {
        super(message);
    }
}