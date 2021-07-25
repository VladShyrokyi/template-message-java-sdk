package template_message_sdk.builder;

import template_message_sdk.block.TextBlockContract;
import template_message_sdk.checker.ConditionCheckerContract;
import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.writer.RegexTextWriter;

public class TemplateBlockConditionDynamicBuilder extends TemplateBlockConditionBuilder {
    protected final RegexTextWriter writer;
    private final String separator;
    private final String dynamicVariableName;

    private int dynamicVariableCounter = 0;

    public TemplateBlockConditionDynamicBuilder(RegexTextWriter writer, TextEditorContract editor,
                                                ConditionCheckerContract conditionChecker, String separator,
                                                String dynamicVariableName) {
        super(writer, editor, conditionChecker);
        this.writer = writer;
        this.separator = separator;
        this.dynamicVariableName = dynamicVariableName;
    }

    public void append(TextBlockContract block) {
        tryAppend(block);
    }

    public boolean tryAppend(TextBlockContract block) {
        if (block == null) {
            throw new VariableNullPointException(this);
        }
        var variableName = dynamicVariableName + "_" + dynamicVariableCounter;
        var templatePart = dynamicVariableCounter == 0
                           ? writer.createSelector(variableName)
                           : separator + writer.createSelector(variableName);
        var isAppended = super.tryAppend(templatePart);
        if (!isAppended) {
            return false;
        }
        super.putVariable(variableName, block);
        dynamicVariableCounter++;
        return true;
    }
}
