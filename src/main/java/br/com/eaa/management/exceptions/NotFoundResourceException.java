package br.com.eaa.management.exceptions;

public class NotFoundResourceException extends RuntimeException{
    public NotFoundResourceException(String message) {
        super("Not found resource with id "+message);
    }
}
