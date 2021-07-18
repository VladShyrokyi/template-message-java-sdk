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
    private final Map<String, TextBlockContract> variables = new HashMap<>();

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
        StringBuilder template = new StringBuilder();
        for (String name : variables.keySet()) {
            if (templateParts.containsKey(name)) {
                template.append(templateParts.get(name));
            }
        }
        return TextBlockFactory.createTemplateWith(template.toString(), variables);
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
