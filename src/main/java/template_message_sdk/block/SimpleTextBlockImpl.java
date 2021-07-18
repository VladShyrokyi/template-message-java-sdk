package template_message_sdk.block;

import template_message_sdk.editor.TextEditor;
import template_message_sdk.writer.TextWriter;

import java.util.HashMap;
import java.util.Map;

public class SimpleTextBlockImpl implements TextBlockContract {
    private final Map<String, String> variables = new HashMap<>();

    private TextWriter writer;
    private TextEditor editor;

    public SimpleTextBlockImpl(TextWriter writer, TextEditor editor) {
        this.writer = writer;
        this.editor = editor;
    }

    public SimpleTextBlockImpl(SimpleTextBlockImpl block) {
        this(block.getWriter().copy(), block.getEditor().copy());
        block.variables.forEach(this::putVariable);
    }

    public String getVariable(String name) {
        if (name == null) {
            return null;
        }
        return variables.get(name);
    }

    public void putVariable(String name, String variable) {
        if (name == null) {
            throw new NullPointerException("Variable name can not be null!");
        }
        if (variable == null) {
            throw new NullPointerException("Variable can not be null");
        }
        variables.put(name, variable);
    }

    @Override
    public TextBlockContract copy() {
        return new SimpleTextBlockImpl(this);
    }

    @Override
    public TextWriter getWriter() {
        return writer;
    }

    @Override
    public void setWriter(TextWriter writer) {
        this.writer = writer;
    }

    @Override
    public TextEditor getEditor() {
        return editor;
    }

    @Override
    public void setEditor(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public String write() {
        return editor != null
               ? editor.toEditing(writer.toWriting(Map.copyOf(variables), ""))
               : writer.toWriting(Map.copyOf(variables), "");
    }

    @Override
    public String writeWithEditor(TextEditor editor) {
        return editor.toEditing(writer.toWriting(Map.copyOf(variables), ""));
    }

    @Override
    public String writeWithoutEditor() {
        return editor.toEditing(writer.toWriting(Map.copyOf(variables), ""));
    }

}
