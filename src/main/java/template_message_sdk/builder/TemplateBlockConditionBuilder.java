package template_message_sdk.builder;

import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;
import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.TemplateNullPointException;
import template_message_sdk.exceptions.VariableNameNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.factory.TextBlockFactory;

import java.util.Map;

public class TemplateBlockConditionBuilder extends TemplateBlockBuilder {
    private final ConditionCheckerContract conditionChecker;

    public TemplateBlockConditionBuilder(String regex, TextEditorContract editor,
                                         ConditionCheckerContract conditionChecker) {
        super(regex, editor);
        this.conditionChecker = conditionChecker;
    }

    @Override
    public void append(String templatePart) {
        tryAppend(templatePart);
    }

    @Override
    public void putVariable(String name, TextBlockContract variable) {
        tryPutVariable(name, variable);
    }

    public boolean tryAppend(String templatePart) {
        if (templatePart == null) {
            throw new TemplateNullPointException(this);
        }
        var template = TextBlockFactory.createOnlyTemplate(templatePart);
        if (isNotContinueBuild(template)) {
            return false;
        }
        super.append(templatePart);
        updateIfCan(template);
        return true;
    }

    public boolean tryPutVariable(String name, TextBlockContract variable) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (variable == null) {
            throw new VariableNullPointException(this);
        }
        if (isNotContinueBuild(variable)) {
            return false;
        }
        super.putVariable(name, variable);
        updateIfCan(variable);
        return true;
    }

    @Override
    public void putVariables(Map<String, TextBlockContract> variables) {
        if (variables == null) {
            throw new VariableNullPointException(this);
        }
        variables.forEach(this::putVariable);
    }

    protected boolean isNotContinueBuild(TextBlockContract block) {
        return conditionChecker != null && !conditionChecker.Check(block);
    }

    protected void updateIfCan(TextBlockContract block) {
        if (conditionChecker != null) {
            conditionChecker.Update(block);
        }
    }
}
