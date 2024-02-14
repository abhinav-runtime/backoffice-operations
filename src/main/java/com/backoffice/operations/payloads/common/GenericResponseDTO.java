package com.backoffice.operations.payloads.common;

import lombok.Data;

@Data
public class GenericResponseDTO<T> {

    private String status;
    private T data;
    private String message;

}