package template_message_sdk.block.interfaces;

public interface TextBlockWithVariablesContract extends TextBlockContract {
    TextBlockContract getVariable(String name);

    void putVariable(String name, TextBlockContract variable);
}
