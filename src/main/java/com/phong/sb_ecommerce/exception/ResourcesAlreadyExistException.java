package com.phong.sb_ecommerce.exception;

public class ResourcesAlreadyExistException extends RuntimeException {
    private String resourceName;
    private String field;
    private String fieldName;
    private Long fieldId;

    public ResourcesAlreadyExistException(String message, String resourceName, String field, String fieldName) {
        super(String.format("%s already exists with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourcesAlreadyExistException( String resourceName, String field, String fieldName) {
        super(String.format("%s already exists with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }


}
