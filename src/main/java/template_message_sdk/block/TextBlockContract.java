package template_message_sdk.block;

import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.writer.TextWriterContract;

public interface TextBlockContract {
    TextBlockContract copy();

    TextWriterContract getWriter();

    void setWriter(TextWriterContract writer);

    TextEditorContract getEditor();

    void setEditor(TextEditorContract editor);

    String write();

    String writeWithEditor(TextEditorContract editor);

    String writeWithoutEditor();
}
