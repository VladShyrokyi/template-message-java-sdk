package template_message_sdk.builder;

import template_message_sdk.DefaultRegex;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;
import template_message_sdk.exceptions.TemplateNullPointException;
import template_message_sdk.exceptions.VariableNameNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.factory.TextBlockFactory;

public class DynamicCompositeTextBlockBuilder extends CompositeTextBlockBuilder {
    private final String dynamicVariableName;
    private final String separator;

    private int dynamicVariableCounter = 0;

    public DynamicCompositeTextBlockBuilder(String separator, String dynamicVariableName,
                                            ConditionCheckerContract conditionChecker) throws NullPointerException {
        super(conditionChecker);
        if (separator == null) {
            throw new NullPointerException("Separator can not be null");
        }
        this.separator = separator;
        this.dynamicVariableName = dynamicVariableName;
    }

    public DynamicCompositeTextBlockBuilder(String separator, String dynamicVariableName) {
        this(separator, dynamicVariableName, null);
    }

    public DynamicCompositeTextBlockBuilder(String separator) {
        this(separator, DefaultRegex.DYNAMIC_VARIABLE_NAME);
    }

    @Override
    public DynamicCompositeTextBlockBuilder add(String name,
                                                String templatePart) throws VariableNameNullPointException,
                                                                            TemplateNullPointException {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (templatePart == null) {
            throw new TemplateNullPointException(this);
        }
        return (DynamicCompositeTextBlockBuilder) super.add(name, templatePart);
    }

    @Override
    public DynamicCompositeTextBlockBuilder put(String name,
                                                TextBlockContract variable) throws VariableNameNullPointException,
                                                                                   VariableNullPointException {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (variable == null) {
            throw new VariableNullPointException(this);
        }
        return (DynamicCompositeTextBlockBuilder) super.put(name, variable);
    }

    public DynamicCompositeTextBlockBuilder dynamicPut(TextBlockContract block) throws VariableNullPointException {
        if (block == null) {
            throw new VariableNullPointException(this);
        }
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
