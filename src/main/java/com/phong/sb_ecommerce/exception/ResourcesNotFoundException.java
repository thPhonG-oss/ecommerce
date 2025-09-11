package com.phong.sb_ecommerce.exception;

public class ResourcesNotFoundException extends RuntimeException {
//    public ResourcesNotFoundException(String message) {
//        super(message);
//    }
    private String resourceName;
    private String field;
    private String fieldName;
    private Long fieldId;

    public ResourcesNotFoundException(String message, String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s : %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourcesNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s : %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourcesNotFoundException(String resourceName, String field, Long fieldId) {
        super(String.format("%s does not exist with %s : %d", resourceName, field, fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }
}
