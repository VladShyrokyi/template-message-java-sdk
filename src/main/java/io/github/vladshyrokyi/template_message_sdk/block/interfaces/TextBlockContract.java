package io.github.vladshyrokyi.template_message_sdk.block.interfaces;

import io.github.vladshyrokyi.template_message_sdk.editor.TextEditorContract;
import io.github.vladshyrokyi.template_message_sdk.writer.TextWriterContract;

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
