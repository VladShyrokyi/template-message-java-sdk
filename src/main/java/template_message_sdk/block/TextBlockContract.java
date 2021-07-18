package template_message_sdk.block;

import template_message_sdk.editor.TextEditor;

public interface TextBlockContract {
    TextBlockContract copy();

    TextEditor getEditor();

    void setEditor(TextEditor editor);

    String write();

    String writeWithEditor(TextEditor editor);

    String writeWithoutEditor();
}
