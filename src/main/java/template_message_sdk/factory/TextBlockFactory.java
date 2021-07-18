package template_message_sdk.factory;

import template_message_sdk.DefaultRegex;
import template_message_sdk.block.SimpleTextBlockImpl;
import template_message_sdk.block.TemplateTextBlockImpl;
import template_message_sdk.block.TextBlockContract;
import template_message_sdk.writer.RegexTextWriter;

import java.util.Map;

public class TextBlockFactory {
    public static SimpleTextBlockImpl createSimple() {
        return new SimpleTextBlockImpl(new RegexTextWriter(), null);
    }

    public static TemplateTextBlockImpl createTemplate() {
        return new TemplateTextBlockImpl(new RegexTextWriter(), null);
    }

    public static SimpleTextBlockImpl createSimpleEmptyWith(String template) {
        return new SimpleTextBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
    }

    public static TemplateTextBlockImpl createTemplateEmptyWith(String template) {
        return new TemplateTextBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
    }

    public static SimpleTextBlockImpl createSimpleWith(String template, Map<String, String> variables) {
        var block = new SimpleTextBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
        variables.forEach(block::putVariable);
        return block;
    }

    public static TemplateTextBlockImpl createTemplateWith(String template, Map<String, TextBlockContract> variables) {
        var block = new TemplateTextBlockImpl(new RegexTextWriter(template, DefaultRegex.REGEX), null);
        variables.forEach(block::putVariable);
        return block;
    }
}
