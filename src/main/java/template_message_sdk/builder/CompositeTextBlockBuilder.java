package template_message_sdk.builder;

import template_message_sdk.block.SimpleTextBlockImpl;
import template_message_sdk.block.TemplateTextBlockImpl;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;

import java.util.HashMap;
import java.util.Map;

public class CompositeTextBlockBuilder {
    protected final ConditionCheckerContract conditionChecker;
    private final Map<String, String> templateParts = new HashMap<>();
    private final Map<String, TextBlockContract> variables = new HashMap<>();

    public CompositeTextBlockBuilder(ConditionCheckerContract conditionChecker) {
        this.conditionChecker = conditionChecker;
    }

    public CompositeTextBlockBuilder add(String name, String templatePart) {
        var block = SimpleTextBlockImpl.valueOf(templatePart);
        if (!conditionChecker.Check(block)) {
            return this;
        }
        templateParts.put(name, templatePart);
        conditionChecker.Update(block);
        return this;
    }

    public CompositeTextBlockBuilder put(String name, TextBlockContract variable) {
        if (variable == null) {
            return this;
        }
        if (!conditionChecker.Check(variable)) {
            return this;
        }
        variables.put(name, variable);
        conditionChecker.Update(variable);
        return this;
    }

    public TemplateTextBlockImpl build() {
        StringBuilder template = new StringBuilder();
        for (String name : variables.keySet()) {
            if (templateParts.containsKey(name)) {
                template.append(templateParts.get(name));
            }
        }
        var block = new TemplateTextBlockImpl(template.toString());
        variables.forEach(block::putVariable);
        return block;
    }
}
