package template_message_sdk.editor;

public interface TextEditorContract {
    TextEditorContract copy();

    String toEditing(String text);
}
