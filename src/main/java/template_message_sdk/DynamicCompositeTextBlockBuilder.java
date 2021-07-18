package template_message_sdk;

public class DynamicCompositeTextBlockBuilder extends CompositeTextBlockBuilder {
    private final String dynamicVariableName;
    private final String separator;

    private int dynamicVariableCounter = 0;

    public DynamicCompositeTextBlockBuilder(ConditionCheckerContract conditionChecker, String dynamicVariableName,
                                            String separator) {
        super(conditionChecker);
        this.dynamicVariableName = dynamicVariableName;
        this.separator = separator;
    }

    public CompositeTextBlockBuilder dynamicPut(TextBlockContract block) {
        var variableName = dynamicVariableName + "_" + dynamicVariableCounter;
        var templatePart = dynamicVariableCounter == 0
                           ? DefaultRegex.getRegexSelector(variableName)
                           : separator + DefaultRegex.getRegexSelector(variableName);
        var checkedBlock = new TemplateTextBlockImpl(templatePart);
        checkedBlock.putVariable(variableName, block);
        if (!conditionChecker.Check(checkedBlock)) {
            return this;
        }
        add(variableName, templatePart).put(variableName, block);
        dynamicVariableCounter++;
        return this;
    }
}
