package template_message_sdk.block;

public interface TextBlockHaveVariablesContract extends TextBlockContract {
    TextBlockContract getVariable(String name);

    void putVariable(String name, TextBlockContract variable);
}
