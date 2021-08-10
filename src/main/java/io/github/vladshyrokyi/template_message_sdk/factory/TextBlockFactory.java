package io.github.vladshyrokyi.template_message_sdk.factory;

import io.github.vladshyrokyi.template_message_sdk.DefaultRegex;
import io.github.vladshyrokyi.template_message_sdk.block.InvariantBlockImpl;
import io.github.vladshyrokyi.template_message_sdk.block.TemplateBlockImpl;
import io.github.vladshyrokyi.template_message_sdk.block.TextBlockImpl;
import io.github.vladshyrokyi.template_message_sdk.block.interfaces.TextBlockContract;
import io.github.vladshyrokyi.template_message_sdk.exceptions.TemplateNullPointException;
import io.github.vladshyrokyi.template_message_sdk.exceptions.VariableNullPointException;
import io.github.vladshyrokyi.template_message_sdk.writer.RegexTextWriter;

import java.util.Map;

public class TextBlockFactory {
    public static TextBlockContract createOnlyTemplate(String template) {
        if (template == null) {
            throw new TemplateNullPointException(TextBlockFactory.class);
        }
        return new InvariantBlockImpl(
                new RegexTextWriter(template, DefaultRegex.REGEX, DefaultRegex.SELECTOR_FACTORY), null);
    }

    public static TextBlockContract createText(String variable) {
        if (variable == null) {
            throw new VariableNullPointException(TextBlockFactory.class);
        }

        return new TextBlockImpl(new RegexTextWriter(
                DefaultRegex.SELECTOR_FACTORY.apply(DefaultRegex.DYNAMIC_VARIABLE_NAME),
                DefaultRegex.REGEX,
                DefaultRegex.SELECTOR_FACTORY
        ), null, variable);
    }

    public static TextBlockContract createTemplate(String template, Map<String, TextBlockContract> variables) {
        if (template == null) {
            throw new TemplateNullPointException(TextBlockFactory.class);
        }
        if (variables == null) {
            throw new VariableNullPointException(TextBlockFactory.class);
        }
        var block = new TemplateBlockImpl(
                new RegexTextWriter(template, DefaultRegex.REGEX, DefaultRegex.SELECTOR_FACTORY), null);
        variables.forEach(block::putVariable);
        return block;
    }
}
