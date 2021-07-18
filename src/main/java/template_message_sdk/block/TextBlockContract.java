package template_message_sdk.block;

import template_message_sdk.editor.TextEditor;
import template_message_sdk.writer.TextWriter;

public interface TextBlockContract {
    TextBlockContract copy();

    TextWriter getWriter();

    void setWriter(TextWriter writer);

    TextEditor getEditor();

    void setEditor(TextEditor editor);

    String write();

    String writeWithEditor(TextEditor editor);

    String writeWithoutEditor();
}
