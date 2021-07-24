package template_message_sdk.block;

import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.VariableNameNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.writer.TextWriterContract;

import java.util.HashMap;
import java.util.Map;

public class SimpleTextBlockImpl implements TextBlockContract {
    private final Map<String, String> variables = new HashMap<>();

    private TextWriterContract writer;
    private TextEditorContract editor;

    public SimpleTextBlockImpl(TextWriterContract writer, TextEditorContract editor) {
        if (writer == null) {
            throw new NullPointerException("Writer can not be null!");
        }
        this.writer = writer;
        this.editor = editor;
    }

    public SimpleTextBlockImpl(SimpleTextBlockImpl block) {
        if (block == null) {
            throw new NullPointerException("Block can not be null!");
        }
        this.writer = block.getWriter().copy();
        if (block.getEditor() != null) {
            this.editor = block.getEditor().copy();
        }
        block.variables.forEach(this::putVariable);
    }

    public String getVariable(String name) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        return variables.get(name);
    }

    public void putVariable(String name, String variable) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (variable == null) {
            throw new VariableNullPointException(this);
        }
        variables.put(name, variable);
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
        return new SimpleTextBlockImpl(this);
    }

    @Override
    public String write() {
        return editor != null
               ? editor.toEditing(writer.toWriting(Map.copyOf(variables), ""))
               : writer.toWriting(Map.copyOf(variables), "");
    }

    @Override
    public String writeWithEditor(TextEditorContract editor) {
        return editor.toEditing(writer.toWriting(Map.copyOf(variables), ""));
    }

    @Override
    public String writeWithoutEditor() {
        return writer.toWriting(Map.copyOf(variables), "");
    }
}
