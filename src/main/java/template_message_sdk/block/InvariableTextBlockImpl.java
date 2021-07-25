package template_message_sdk.block;

import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.writer.TextWriterContract;

import java.util.Map;

public class InvariableTextBlockImpl implements TextBlockContract {
    private TextWriterContract writer;
    private TextEditorContract editor;

    public InvariableTextBlockImpl(TextWriterContract writer, TextEditorContract editor) {
        this.writer = writer;
        this.editor = editor;
    }

    @Override
    public TextWriterContract getWriter() {
        return this.writer;
    }

    @Override
    public void setWriter(TextWriterContract writer) {
        this.writer = writer;
    }

    @Override
    public TextEditorContract getEditor() {
        return this.editor;
    }

    @Override
    public void setEditor(TextEditorContract editor) {
        this.editor = editor;
    }

    @Override
    public TextBlockContract copy() {
        return new InvariableTextBlockImpl(writer.copy(), editor.copy());
    }

    @Override
    public String write() {
        return editor.toEditing(writer.toWriting(Map.of(), ""));
    }

    @Override
    public String writeWithEditor(TextEditorContract editor) {
        return editor.toEditing(writer.toWriting(Map.of(), ""));
    }

    @Override
    public String writeWithoutEditor() {
        return writer.toWriting(Map.of(), "");
    }
}
