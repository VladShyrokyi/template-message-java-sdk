package template_message_sdk.builder;

import template_message_sdk.block.interfaces.TextBlockContract;
import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.writer.RegexTextWriter;

public class TemplateBlockDynamicBuilder extends TemplateBlockBuilder {
    protected final RegexTextWriter writer;
    private final String separator;
    private final String dynamicVariableName;

    private int dynamicVariableCounter = 0;

    public TemplateBlockDynamicBuilder(RegexTextWriter writer, TextEditorContract editor, String separator,
                                       String dynamicVariableName) {
        super(writer, editor);
        this.writer = writer;
        this.separator = separator;
        this.dynamicVariableName = dynamicVariableName;
    }

    public void append(TextBlockContract block) {
        if (block == null) {
            throw new VariableNullPointException(this);
        }
        var variableName = dynamicVariableName + "_" + dynamicVariableCounter;
        var templatePart = dynamicVariableCounter == 0
                           ? writer.createSelector(variableName)
                           : separator + writer.createSelector(variableName);
        super.append(templatePart);
        super.putVariable(variableName, block);
        dynamicVariableCounter++;
    }
}
