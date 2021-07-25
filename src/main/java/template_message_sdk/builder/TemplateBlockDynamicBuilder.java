package template_message_sdk.builder;

import template_message_sdk.DefaultRegex;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.VariableNullPointException;

public class TemplateBlockDynamicBuilder extends TemplateBlockBuilder {
    private final String separator;
    private final String dynamicVariableName;
    private int dynamicVariableCounter = 0;

    public TemplateBlockDynamicBuilder(String regex, TextEditorContract editor, String separator,
                                       String dynamicVariableName) {
        super(regex, editor);
        this.separator = separator;
        this.dynamicVariableName = dynamicVariableName;
    }

    public void dynamicPut(TextBlockContract block) {
        if (block == null) {
            throw new VariableNullPointException(this);
        }
        var variableName = dynamicVariableName + "_" + dynamicVariableCounter;
        var templatePart = dynamicVariableCounter == 0
                           ? DefaultRegex.selectorFrom(variableName)
                           : separator + DefaultRegex.selectorFrom(variableName);
        super.append(templatePart);
        super.putVariable(variableName, block);
        dynamicVariableCounter++;
    }
}
