package template_message_sdk.builder;

import template_message_sdk.DefaultRegex;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;
import template_message_sdk.factory.TextBlockFactory;

public class DynamicCompositeTextBlockBuilder extends CompositeTextBlockBuilder {
    private final String dynamicVariableName;
    private final String separator;

    private int dynamicVariableCounter = 0;

    public DynamicCompositeTextBlockBuilder(String separator, String dynamicVariableName,
                                            ConditionCheckerContract conditionChecker) {
        super(conditionChecker);
        this.dynamicVariableName = dynamicVariableName;
        this.separator = separator;
    }

    public DynamicCompositeTextBlockBuilder(String separator, String dynamicVariableName) {
        this(separator, dynamicVariableName, null);
    }

    public DynamicCompositeTextBlockBuilder(String separator) {
        this(separator, DefaultRegex.DYNAMIC_VARIABLE_NAME);
    }

    @Override
    public DynamicCompositeTextBlockBuilder add(String name, String templatePart) {
        return (DynamicCompositeTextBlockBuilder) super.add(name, templatePart);
    }

    @Override
    public DynamicCompositeTextBlockBuilder put(String name, TextBlockContract variable) {
        return (DynamicCompositeTextBlockBuilder) super.put(name, variable);
    }

    public DynamicCompositeTextBlockBuilder dynamicPut(TextBlockContract block) {
        var variableName = dynamicVariableName + "_" + dynamicVariableCounter;
        var templatePart = dynamicVariableCounter == 0
                           ? DefaultRegex.createSelector(variableName)
                           : separator + DefaultRegex.createSelector(variableName);
        var checkedBlock = TextBlockFactory.createTemplateEmptyWith(templatePart);
        checkedBlock.putVariable(variableName, block);
        if (isNotContinueBuild(checkedBlock)) {
            return this;
        }
        add(variableName, templatePart).put(variableName, block);
        dynamicVariableCounter++;
        return this;
    }
}
