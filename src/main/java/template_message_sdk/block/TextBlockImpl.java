package template_message_sdk.block;

import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.writer.TextWriterContract;

import java.util.Map;

public class TextBlockImpl implements TextBlockContract {
    private TextWriterContract writer;
    private TextEditorContract editor;

    private String variable;

    public TextBlockImpl(TextWriterContract writer, TextEditorContract editor) {
        this.writer = writer;
        this.editor = editor;
    }

    public TextBlockImpl(TextBlockImpl block) {
        this(block.getWriter(), block.getEditor());
        variable = block.getVariable();
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    @Override
    public TextWriterContract getWriter() {
        return writer;
    }

    @Override
    public void setWriter(TextWriterContract writer) {
        this.writer = writer;
    }

    @Override
    public TextEditorContract getEditor() {
        return editor;
    }

    @Override
    public void setEditor(TextEditorContract editor) {
        this.editor = editor;
    }

    @Override
    public TextBlockContract copy() {
        return new TextBlockImpl(this);
    }

    @Override
    public String write() {
        return editor.toEditing(writer.toWriting(Map.of(), variable));
    }

    @Override
    public String writeWithEditor(TextEditorContract editor) {
        return editor.toEditing(writer.toWriting(Map.of(), variable));
    }

    @Override
    public String writeWithoutEditor() {
        return editor.toEditing(writer.toWriting(Map.of(), variable));
    }
}
