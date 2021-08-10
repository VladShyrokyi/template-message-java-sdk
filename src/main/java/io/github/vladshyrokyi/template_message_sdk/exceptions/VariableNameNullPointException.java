package io.github.vladshyrokyi.template_message_sdk.exceptions;

public class VariableNameNullPointException extends NullPointerException {
    public VariableNameNullPointException(Object context) {
        super("Variable name can not be null! Exception in " + context);
    }
}
