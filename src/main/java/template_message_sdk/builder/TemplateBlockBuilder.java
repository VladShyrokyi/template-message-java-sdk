package template_message_sdk.builder;

import template_message_sdk.block.TemplateBlockImpl;
import template_message_sdk.block.interfaces.TextBlockContract;
import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.TemplateNullPointException;
import template_message_sdk.exceptions.VariableNameNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.writer.TextWriterContract;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TemplateBlockBuilder {
    protected final LinkedList<String> templateParts = new LinkedList<>();
    protected final Map<String, TextBlockContract> variables = new HashMap<>();

    protected final TextWriterContract writer;
    protected final TextEditorContract editor;

    public TemplateBlockBuilder(TextWriterContract writer, TextEditorContract editor) {
        this.writer = writer;
        this.editor = editor;
    }

    public void append(String templatePart) {
        if (templatePart == null) {
            throw new TemplateNullPointException(this);
        }
        templateParts.add(templatePart);
    }

    public void putVariable(String name, TextBlockContract variable) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (variable == null) {
            throw new VariableNullPointException(this);
        }
        this.variables.put(name, variable);
    }

    public void putVariables(Map<String, TextBlockContract> variables) {
        if (variables == null) {
            throw new VariableNullPointException(this);
        }
        this.variables.putAll(variables);
    }

    public TextBlockContract build() {
        var writer = this.writer.copy();
        var editor = this.editor.copy();
        writer.setTemplate(writer.getTemplate() + toCollectTemplate());
        var block = new TemplateBlockImpl(writer, editor);
        variables.forEach(block::putVariable);
        return block;
    }

    protected String toCollectTemplate() {
        return String.join("", templateParts);
    }
}
