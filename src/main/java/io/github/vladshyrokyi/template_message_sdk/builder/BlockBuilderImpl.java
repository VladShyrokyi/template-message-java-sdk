package io.github.vladshyrokyi.template_message_sdk.builder;

import io.github.vladshyrokyi.template_message_sdk.DefaultRegex;
import io.github.vladshyrokyi.template_message_sdk.block.TemplateBlockImpl;
import io.github.vladshyrokyi.template_message_sdk.block.interfaces.TextBlockContract;
import io.github.vladshyrokyi.template_message_sdk.editor.TextEditorContract;
import io.github.vladshyrokyi.template_message_sdk.exceptions.VariableNullPointException;
import io.github.vladshyrokyi.template_message_sdk.writer.RegexTextWriter;
import io.github.vladshyrokyi.template_message_sdk.writer.TextWriterContract;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class BlockBuilderImpl implements BlockBuilderContract {
    protected final RegexTextWriter writer;
    protected final TextEditorContract editor;
    protected final List<String> variables = new LinkedList<>();
    protected final Map<String, String> variableTemplateParts = new HashMap<>();
    protected final Map<String, TextBlockContract> variableValues = new HashMap<>();
    private final String separator;
    private final String dynamicVariableName;
    private int dynamicVariableCounter = 0;

    public BlockBuilderImpl(String separator, String dynamicVariableName, RegexTextWriter writer,
                            TextEditorContract editor) {
        this.separator = separator;
        this.dynamicVariableName = dynamicVariableName;
        this.writer = writer;
        this.editor = editor;
    }

    public BlockBuilderImpl(String separator, String dynamicVariableName) {
        this(separator, dynamicVariableName, new RegexTextWriter("", DefaultRegex.REGEX, DefaultRegex.SELECTOR_FACTORY),
             null
        );
    }

    @Override
    public void append(TextBlockContract variable) {
        if (variable == null) {
            throw new VariableNullPointException(this);
        }

        var variableName = createVariableName();
        var templatePart = createTemplatePart(variableName);

        variables.add(variableName);
        variableTemplateParts.put(variableName, templatePart);
        variableValues.put(variableName, variable);

        updateVariableCounter();
    }

    @Override
    public TextBlockContract build() {
        TextWriterContract writer = null;
        if (this.writer != null) {
            writer = this.writer.copy();
        }
        TextEditorContract editor = null;
        if (this.editor != null) {
            editor = this.editor.copy();
        }
        var block = new TemplateBlockImpl(writer, editor);
        variables.forEach(variableName -> {
            block.append(variableTemplateParts.get(variableName));
            block.putVariable(variableName, variableValues.get(variableName).copy());
        });

        return block;
    }

    protected String createVariableName() {
        return dynamicVariableName + "_" + dynamicVariableCounter;
    }

    protected String createTemplatePart(String variableName) {
        return dynamicVariableCounter == 0
               ? writer.createSelector(variableName)
               : separator + writer.createSelector(variableName);
    }

    protected void updateVariableCounter() {
        dynamicVariableCounter++;
    }
}
