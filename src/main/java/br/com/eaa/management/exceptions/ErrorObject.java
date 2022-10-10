package br.com.eaa.management.exceptions;

import lombok.Data;

@Data
public class ErrorObject {
    private String error;
    private String code;
}
