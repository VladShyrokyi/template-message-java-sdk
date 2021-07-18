package template_message_sdk.block;

import template_message_sdk.editor.TextEditor;
import template_message_sdk.writer.TextWriter;

import java.util.HashMap;
import java.util.Map;

public class TemplateTextBlockImpl implements TextBlockContract {
    private final Map<String, TextBlockContract> variables = new HashMap<>();

    private TextWriter writer;
    private TextEditor editor;

    public TemplateTextBlockImpl(TextWriter writer, TextEditor editor) {
        this.writer = writer;
        this.editor = editor;
    }

    public TemplateTextBlockImpl(TemplateTextBlockImpl template) {
        this(template.getWriter().copy(), template.getEditor());
        variables.forEach((name, variable) -> putVariable(name, variable.copy()));
    }

    public TextBlockContract getVariable(String name) {
        if (name == null) {
            return null;
        }
        return variables.get(name);
    }

    public void putVariable(String name, TextBlockContract variable) throws NullPointerException {
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
        return new TemplateTextBlockImpl(this);
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
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.write()));

        return editor != null
               ? editor.toEditing(writer.toWriting(stringVariables, ""))
               : writer.toWriting(stringVariables, "");
    }

    @Override
    public String writeWithEditor(TextEditor editor) {
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.writeWithEditor(editor)));

        return editor.toEditing(writer.toWriting(stringVariables, ""));
    }

    @Override
    public String writeWithoutEditor() {
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.writeWithoutEditor()));

        return writer.toWriting(stringVariables, "");
    }
}
