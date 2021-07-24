package template_message_sdk.exceptions;

public class VariableNullPointException extends NullPointerException {
    public VariableNullPointException(Object context) {
        super("Variable can not be null! Exception in " + context);
    }
}
