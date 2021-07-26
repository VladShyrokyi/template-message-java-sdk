package template_message_sdk.block.interfaces;

import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.writer.TextWriterContract;

public interface TextBlockContract {
    TextWriterContract getWriter();

    void setWriter(TextWriterContract writer);

    TextEditorContract getEditor();

    void setEditor(TextEditorContract editor);

    TextBlockContract copy();

    String write();

    String writeWithEditor(TextEditorContract editor);

    String writeWithoutEditor();
}
