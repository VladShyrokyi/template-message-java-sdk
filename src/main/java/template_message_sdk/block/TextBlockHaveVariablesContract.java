package template_message_sdk.block;

public interface TextBlockHaveVariablesContract<T> {
    T getVariable(String name);

    void putVariable(String name, T variable);
}
