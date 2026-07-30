package io.github.gabrielhe4.shop_api.exception;

public class ResourceAlreadyExistsException extends RuntimeException {

    String resourceName;
    String field;

    public ResourceAlreadyExistsException() {
    }

    public ResourceAlreadyExistsException(String resourceName, String field) {
        super(String.format("The field with name: %s already exists in %s", field, resourceName));
        this.resourceName = resourceName;
        this.field = field;
    }

}
