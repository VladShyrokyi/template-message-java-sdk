package org.vlad_shyrokyi.template_message_sdk.builder;

import org.vlad_shyrokyi.template_message_sdk.block.TemplateBlockImpl;
import org.vlad_shyrokyi.template_message_sdk.block.interfaces.TextBlockContract;
import org.vlad_shyrokyi.template_message_sdk.editor.TextEditorContract;
import org.vlad_shyrokyi.template_message_sdk.exceptions.VariableNullPointException;
import org.vlad_shyrokyi.template_message_sdk.writer.RegexTextWriter;

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
        var block = new TemplateBlockImpl(writer.copy(), editor.copy());
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
