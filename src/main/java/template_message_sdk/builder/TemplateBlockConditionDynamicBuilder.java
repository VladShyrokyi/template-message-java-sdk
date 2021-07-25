package template_message_sdk.builder;

import template_message_sdk.DefaultRegex;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;
import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.VariableNullPointException;

public class TemplateBlockConditionDynamicBuilder extends TemplateBlockConditionBuilder {
    private final String separator;
    private final String dynamicVariableName;

    private int dynamicVariableCounter = 0;

    public TemplateBlockConditionDynamicBuilder(String regex, TextEditorContract editor,
                                                ConditionCheckerContract conditionChecker, String separator,
                                                String dynamicVariableName) {
        super(regex, editor, conditionChecker);
        this.separator = separator;
        this.dynamicVariableName = dynamicVariableName;
    }

    public void dynamicPut(TextBlockContract block) {
        tryDynamicPut(block);
    }

    public boolean tryDynamicPut(TextBlockContract block) {
        if (block == null) {
            throw new VariableNullPointException(this);
        }
        var variableName = dynamicVariableName + "_" + dynamicVariableCounter;
        var templatePart = dynamicVariableCounter == 0
                           ? DefaultRegex.createSelector(variableName)
                           : separator + DefaultRegex.createSelector(variableName);
        var isAppended = super.tryAppend(templatePart);
        if (!isAppended) {
            return false;
        }
        super.putVariable(variableName, block);
        dynamicVariableCounter++;
        return true;
    }
}
