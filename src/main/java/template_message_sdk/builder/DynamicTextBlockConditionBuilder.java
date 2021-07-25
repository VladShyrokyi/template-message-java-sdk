package template_message_sdk.builder;

import template_message_sdk.DefaultRegex;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;
import template_message_sdk.exceptions.TemplateNullPointException;
import template_message_sdk.exceptions.VariableNameNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.factory.TextBlockFactory;

import java.util.Map;

public class DynamicTextBlockConditionBuilder extends TextBlockConditionBuilder {
    private final String dynamicVariableName;
    private final String separator;

    private int dynamicVariableCounter = 0;

    public DynamicTextBlockConditionBuilder(String separator, String dynamicVariableName,
                                            ConditionCheckerContract conditionChecker) {
        super(conditionChecker);
        if (separator == null) {
            throw new NullPointerException("Separator can not be null");
        }
        this.separator = separator;
        this.dynamicVariableName = dynamicVariableName;
    }

    public DynamicTextBlockConditionBuilder(String separator, String dynamicVariableName) {
        this(separator, dynamicVariableName, null);
    }

    public DynamicTextBlockConditionBuilder(String separator) {
        this(separator, DefaultRegex.DYNAMIC_VARIABLE_NAME);
    }

    @Override
    public DynamicTextBlockConditionBuilder add(String name, String templatePart) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (templatePart == null) {
            throw new TemplateNullPointException(this);
        }
        return (DynamicTextBlockConditionBuilder) super.add(name, templatePart);
    }

    @Override
    public DynamicTextBlockConditionBuilder put(String name, TextBlockContract variable) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (variable == null) {
            throw new VariableNullPointException(this);
        }
        return (DynamicTextBlockConditionBuilder) super.put(name, variable);
    }

    public DynamicTextBlockConditionBuilder dynamicPut(TextBlockContract block) {
        if (block == null) {
            throw new VariableNullPointException(this);
        }
        var variableName = dynamicVariableName + "_" + dynamicVariableCounter;
        var templatePart = dynamicVariableCounter == 0
                           ? DefaultRegex.createSelector(variableName)
                           : separator + DefaultRegex.createSelector(variableName);
        var checkedBlock = TextBlockFactory.createTemplate(templatePart, Map.of(variableName, block));
        if (isNotContinueBuild(checkedBlock)) {
            return this;
        }
        add(variableName, templatePart).put(variableName, block);
        dynamicVariableCounter++;
        return this;
    }
}
