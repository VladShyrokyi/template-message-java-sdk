package template_message_sdk.builder;

import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;
import template_message_sdk.exceptions.TemplateNullPointException;
import template_message_sdk.exceptions.VariableNameNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.factory.TextBlockFactory;

import java.util.HashMap;
import java.util.Map;

public class CompositeTextBlockBuilder {
    protected final Map<String, TextBlockContract> variables = new HashMap<>();
    private final ConditionCheckerContract conditionChecker;
    private final Map<String, String> templateParts = new HashMap<>();

    public CompositeTextBlockBuilder(ConditionCheckerContract conditionChecker) {
        this.conditionChecker = conditionChecker;
    }

    public CompositeTextBlockBuilder() {
        this(null);
    }

    public CompositeTextBlockBuilder add(String name, String templatePart) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (templatePart == null) {
            throw new TemplateNullPointException(this);
        }
        var block = TextBlockFactory.createOnlyTemplate(templatePart);
        if (isNotContinueBuild(block)) {
            return this;
        }
        templateParts.put(name, templatePart);
        updateIfCan(block);
        return this;
    }

    public CompositeTextBlockBuilder put(String name, TextBlockContract variable) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (variable == null) {
            throw new VariableNullPointException(this);
        }
        if (isNotContinueBuild(variable)) {
            return this;
        }
        variables.put(name, variable);
        updateIfCan(variable);
        return this;
    }

    public TextBlockContract build() {
        return TextBlockFactory.createTemplate(collectTemplate(), variables);
    }

    protected String collectTemplate() {
        return variables.keySet().stream().reduce("", (template, variableName) ->
                templateParts.containsKey(variableName)
                ? template + templateParts.get(variableName)
                : template
        );
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
