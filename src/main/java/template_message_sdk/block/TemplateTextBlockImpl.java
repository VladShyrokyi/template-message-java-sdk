package template_message_sdk.block;

import template_message_sdk.DefaultRegex;
import template_message_sdk.editor.TextEditor;
import template_message_sdk.writer.RegexTextWriter;

import java.util.HashMap;
import java.util.Map;

public class TemplateTextBlockImpl implements TextBlockContract {
    private final RegexTextWriter writer;
    private TextEditor editor;
    private final Map<String, TextBlockContract> variables = new HashMap<>();

    public TemplateTextBlockImpl(RegexTextWriter writer, TextEditor editor) {
        this.writer = writer;
        this.editor = editor;
    }

    public TemplateTextBlockImpl(String template, String regex, TextEditor editor) {
        writer = new RegexTextWriter(template, regex);
        this.editor = editor;
    }

    public TemplateTextBlockImpl(String template, String regex) {
        this(template, regex, null);
    }

    public TemplateTextBlockImpl(String template) {
        this(template, DefaultRegex.REGEX);
    }

    public TemplateTextBlockImpl() {
        this("");
    }

    public TemplateTextBlockImpl(TemplateTextBlockImpl template) {
        this(template.writer.copy(), template.getEditor());
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
               ? editor.toEditing(writer.write(stringVariables, ""))
               : writer.write(stringVariables, "");
    }

    @Override
    public String writeWithEditor(TextEditor editor) {
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.writeWithEditor(editor)));

        return editor.toEditing(writer.write(stringVariables, ""));
    }

    @Override
    public String writeWithoutEditor() {
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.writeWithoutEditor()));

        return writer.write(stringVariables, "");
    }
}
