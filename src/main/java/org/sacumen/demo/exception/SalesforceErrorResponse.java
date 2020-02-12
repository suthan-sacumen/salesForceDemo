package org.sacumen.demo.exception;

public class SalesforceErrorResponse extends  RuntimeException{

    public SalesforceErrorResponse(String message, Throwable cause) {
        super(message, cause);
    }

    public SalesforceErrorResponse(Throwable cause) {
        super(cause);
    }
}