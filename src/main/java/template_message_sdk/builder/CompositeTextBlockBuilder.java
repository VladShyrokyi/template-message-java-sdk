package template_message_sdk.builder;

import template_message_sdk.block.TemplateTextBlockImpl;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;
import template_message_sdk.factory.TextBlockFactory;

import java.util.HashMap;
import java.util.Map;

public class CompositeTextBlockBuilder {
    private final ConditionCheckerContract conditionChecker;
    private final Map<String, String> templateParts = new HashMap<>();

    protected final Map<String, TextBlockContract> variables = new HashMap<>();

    public CompositeTextBlockBuilder(ConditionCheckerContract conditionChecker) {
        this.conditionChecker = conditionChecker;
    }

    public CompositeTextBlockBuilder() {
        this(null);
    }

    public CompositeTextBlockBuilder add(String name, String templatePart) {
        var block = TextBlockFactory.createSimpleEmptyWith(templatePart);
        if (isNotContinueBuild(block)) {
            return this;
        }
        templateParts.put(name, templatePart);
        updateIfCan(block);
        return this;
    }

    public CompositeTextBlockBuilder put(String name, TextBlockContract variable) {
        if (variable == null) {
            return this;
        }
        if (isNotContinueBuild(variable)) {
            return this;
        }
        variables.put(name, variable);
        updateIfCan(variable);
        return this;
    }

    public TemplateTextBlockImpl build() {
        return TextBlockFactory.createTemplateWith(collectTemplate(), variables);
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
