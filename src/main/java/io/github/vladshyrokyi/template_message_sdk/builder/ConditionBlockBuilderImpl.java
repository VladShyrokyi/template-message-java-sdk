package io.github.vladshyrokyi.template_message_sdk.builder;

import io.github.vladshyrokyi.template_message_sdk.block.InvariantBlockImpl;
import io.github.vladshyrokyi.template_message_sdk.block.interfaces.TextBlockContract;
import io.github.vladshyrokyi.template_message_sdk.checker.ConditionCheckerContract;
import io.github.vladshyrokyi.template_message_sdk.editor.TextEditorContract;
import io.github.vladshyrokyi.template_message_sdk.exceptions.VariableNullPointException;
import io.github.vladshyrokyi.template_message_sdk.writer.RegexTextWriter;

public class ConditionBlockBuilderImpl extends BlockBuilderImpl {
    protected final ConditionCheckerContract conditionChecker;

    public ConditionBlockBuilderImpl(String separator, String dynamicVariableName, RegexTextWriter writer,
                                     TextEditorContract editor, ConditionCheckerContract conditionChecker) {
        super(separator, dynamicVariableName, writer, editor);
        this.conditionChecker = conditionChecker;
    }

    @Override
    public void append(TextBlockContract variable) {
        if (variable == null) {
            throw new VariableNullPointException(this);
        }

        var variableName = createVariableName();
        var templatePart = createTemplatePart(variableName);

        var writer = this.writer.copy();
        writer.setTemplate(templatePart);
        var onlyTemplate = new InvariantBlockImpl(writer, null);

        if (!conditionChecker.Check(onlyTemplate)) {
            return;
        }
        if (!conditionChecker.Check(variable)) {
            return;
        }

        conditionChecker.Update(onlyTemplate);
        conditionChecker.Update(variable);

        variables.add(variableName);
        variableTemplateParts.put(variableName, templatePart);
        variableValues.put(variableName, variable);

        updateVariableCounter();
    }
}
