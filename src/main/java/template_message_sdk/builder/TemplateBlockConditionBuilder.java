package template_message_sdk.builder;

import template_message_sdk.block.TemplateBlockImpl;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;
import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.TemplateNullPointException;
import template_message_sdk.exceptions.VariableNameNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.factory.TextBlockFactory;
import template_message_sdk.writer.RegexTextWriter;

import java.util.HashMap;
import java.util.Map;

public class TemplateBlockConditionBuilder extends TemplateBlockBuilder {
    protected final Map<String, TextBlockContract> variables = new HashMap<>();
    private final ConditionCheckerContract conditionChecker;

    public TemplateBlockConditionBuilder(String regex, TextEditorContract editor,
                                         ConditionCheckerContract conditionChecker) {
        super(regex, editor);
        this.conditionChecker = conditionChecker;
    }

    @Override
    public void append(String templatePart) {
        if (templatePart == null) {
            throw new TemplateNullPointException(this);
        }
        var template = TextBlockFactory.createOnlyTemplate(templatePart);
        if (isNotContinueBuild(template)) {
            return;
        }
        super.append(templatePart);
        updateIfCan(template);
    }

    @Override
    public void putVariable(String name, TextBlockContract variable) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (variable == null) {
            throw new VariableNullPointException(this);
        }
        if (isNotContinueBuild(variable)) {
            return;
        }
        super.putVariable(name, variable);
        updateIfCan(variable);
    }

    @Override
    public void putVariables(Map<String, TextBlockContract> variables) {
        if (variables == null) {
            throw new VariableNullPointException(this);
        }
        variables.forEach(this::putVariable);
    }

    @Override
    public TextBlockContract build() {
        var block = new TemplateBlockImpl(new RegexTextWriter(super.toCollectTemplate(), regex), editor);
        variables.forEach(block::putVariable);
        return block;
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
