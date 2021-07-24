package template_message_sdk.factory;

import template_message_sdk.DefaultRegex;
import template_message_sdk.block.SimpleTextBlockImpl;
import template_message_sdk.block.TemplateTextBlockImpl;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.builder.DynamicCompositeTextBlockBuilder;
import template_message_sdk.exceptions.TemplateNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.writer.RegexTextWriter;

import java.util.Arrays;
import java.util.Map;

public class TextBlockFactory {
    public static SimpleTextBlockImpl createSimpleEmptyWith(String template) {
        if (template == null) {
            throw new TemplateNullPointException(TextBlockFactory.class);
        }
        return new SimpleTextBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
    }

    public static TemplateTextBlockImpl createTemplateEmptyWith(String template) {
        if (template == null) {
            throw new TemplateNullPointException(TextBlockFactory.class);
        }
        return new TemplateTextBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
    }

    public static SimpleTextBlockImpl createSimpleWith(String template, Map<String, String> variables) {
        if (template == null) {
            throw new TemplateNullPointException(TextBlockFactory.class);
        }
        if (variables == null) {
            throw new VariableNullPointException(TextBlockFactory.class);
        }
        var block = new SimpleTextBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
        variables.forEach(block::putVariable);
        return block;
    }

    public static SimpleTextBlockImpl createSimpleWith(String variable) {
        if (variable == null) {
            throw new VariableNullPointException(TextBlockFactory.class);
        }
        var block = new SimpleTextBlockImpl(new RegexTextWriter(
                DefaultRegex.createSelector(DefaultRegex.DYNAMIC_VARIABLE_NAME),
                DefaultRegex.REGEX
        ), null);
        block.putVariable(DefaultRegex.DYNAMIC_VARIABLE_NAME, variable);
        return block;
    }

    public static TemplateTextBlockImpl createTemplateWith(String template, Map<String, TextBlockContract> variables) {
        if (template == null) {
            throw new TemplateNullPointException(TextBlockFactory.class);
        }
        if (variables == null) {
            throw new VariableNullPointException(TextBlockFactory.class);
        }
        var block = new TemplateTextBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
        variables.forEach(block::putVariable);
        return block;
    }

    public static TemplateTextBlockImpl createTemplateWith(String separator, TextBlockContract[] variables) {
        if (separator == null) {
            throw new NullPointerException("Separator can not be null!");
        }
        if (variables == null) {
            throw new VariableNullPointException(TextBlockFactory.class);
        }
        var builder = new DynamicCompositeTextBlockBuilder(separator);
        Arrays.stream(variables).forEach(builder::dynamicPut);
        return builder.build();
    }
}
