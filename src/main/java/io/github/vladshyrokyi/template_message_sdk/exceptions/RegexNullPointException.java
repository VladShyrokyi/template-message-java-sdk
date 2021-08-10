package io.github.vladshyrokyi.template_message_sdk.exceptions;

public class RegexNullPointException extends NullPointerException {
    public RegexNullPointException(Object context) {
        super("Regex can not be null! Exception in " + context);
    }
}
