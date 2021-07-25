package template_message_sdk.builder;

import template_message_sdk.block.TemplateBlockImpl;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.TemplateNullPointException;
import template_message_sdk.exceptions.VariableNameNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.writer.RegexTextWriter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TemplateBlockBuilder {
    protected final LinkedList<String> templateParts = new LinkedList<>();
    protected final Map<String, TextBlockContract> variables = new HashMap<>();

    protected final String regex;
    protected final TextEditorContract editor;

    public TemplateBlockBuilder(String regex, TextEditorContract editor) {
        this.regex = regex;
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
        var block = new TemplateBlockImpl(new RegexTextWriter(toCollectTemplate(), regex), editor);
        variables.forEach(block::putVariable);
        return block;
    }

    protected String toCollectTemplate() {
        return String.join("", templateParts);
    }

}
