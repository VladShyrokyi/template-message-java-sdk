package template_message_sdk.factory;

import template_message_sdk.DefaultRegex;
import template_message_sdk.block.InvariableTextBlockImpl;
import template_message_sdk.block.TemplateBlockImpl;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.block.TextBlockImpl;
import template_message_sdk.exceptions.TemplateNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.writer.RegexTextWriter;

import java.util.Map;

public class TextBlockFactory {
    public static TextBlockContract createOnlyTemplate(String template) {
        if (template == null) {
            throw new TemplateNullPointException(TextBlockFactory.class);
        }
        return new InvariableTextBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
    }

    public static TextBlockContract createText(String variable) {
        if (variable == null) {
            throw new VariableNullPointException(TextBlockFactory.class);
        }

        var block = new TextBlockImpl(new RegexTextWriter(
                DefaultRegex.createSelector(DefaultRegex.DYNAMIC_VARIABLE_NAME),
                DefaultRegex.REGEX
        ), null);
        block.setVariable(variable);
        return block;
    }

    public static TextBlockContract createTemplate(String template, Map<String, TextBlockContract> variables) {
        if (template == null) {
            throw new TemplateNullPointException(TextBlockFactory.class);
        }
        if (variables == null) {
            throw new VariableNullPointException(TextBlockFactory.class);
        }
        var block = new TemplateBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
        variables.forEach(block::putVariable);
        return block;
    }
}
