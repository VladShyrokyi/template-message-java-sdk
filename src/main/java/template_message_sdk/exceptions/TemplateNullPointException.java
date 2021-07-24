package template_message_sdk.exceptions;

public class TemplateNullPointException extends NullPointerException {
    public TemplateNullPointException(Object context) {
        super("Template can not be null! Exception in " + context);
    }
}
