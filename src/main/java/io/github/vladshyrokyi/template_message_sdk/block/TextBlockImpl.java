package io.github.vladshyrokyi.template_message_sdk.block;

import io.github.vladshyrokyi.template_message_sdk.block.interfaces.TextBlockContract;
import io.github.vladshyrokyi.template_message_sdk.editor.TextEditorContract;
import io.github.vladshyrokyi.template_message_sdk.writer.TextWriterContract;

import java.util.Map;

public class TextBlockImpl implements TextBlockContract {
    private TextWriterContract writer;
    private TextEditorContract editor;

    private String variable = "";

    public TextBlockImpl(TextWriterContract writer, TextEditorContract editor, String variable) {
        if (writer == null) {
            throw new NullPointerException("Writer can not be null! Exception in " + this);
        }
        this.writer = writer;
        this.editor = editor;
        if (variable != null) {
            this.variable = variable;
        }
    }

    public TextBlockImpl(TextWriterContract writer, TextEditorContract editor) {
        this(writer, editor, null);
    }

    public TextBlockImpl(TextBlockImpl block) {
        this(block.getWriter(), block.getEditor(), block.getVariable());
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
        return editor != null
               ? editor.toEditing(writer.toWriting(Map.of(), variable))
               : writer.toWriting(Map.of(), variable);
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
