package org.vlad_shyrokyi.template_message_sdk.writer;

import org.vlad_shyrokyi.template_message_sdk.exceptions.RegexNullPointException;
import org.vlad_shyrokyi.template_message_sdk.exceptions.TemplateNullPointException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;

public class RegexTextWriter implements TextWriterContract {
    private final String regex;
    private final Pattern selectorPattern;
    private final Function<String, String> selectorFactory;
    private Map<String, String> selectors = new HashMap<>();

    private String template;

    public RegexTextWriter(String template, String regex,
                           Function<String, String> selectorFactory) {
        if (template == null) {
            throw new TemplateNullPointException(this);
        }
        if (regex == null) {
            throw new RegexNullPointException(this);
        }
        if (selectorFactory == null) {
            throw new NullPointerException("Factory of selectors can not be null! Exception in " + this);
        }
        this.regex = regex;
        this.selectorPattern = Pattern.compile(regex);
        this.selectorFactory = selectorFactory;
        setTemplate(template);
    }

    public RegexTextWriter(RegexTextWriter writer) {
        if (writer == null) {
            throw new NullPointerException("Writer can not be null!");
        }
        this.regex = writer.regex;
        this.selectorPattern = Pattern.compile(this.regex);
        this.selectorFactory = writer.selectorFactory;
        setTemplate(writer.template);
    }

    public String createSelector(String name) {
        return selectorFactory.apply(name);
    }

    public String getRegex() {
        return regex;
    }

    public Set<String> getSelectors() {
        return Set.copyOf(selectors.keySet());
    }

    @Override
    public String getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(String template) {
        selectors = new HashMap<>();
        var matcher = selectorPattern.matcher(template);
        while (matcher.find()) {
            selectors.put(matcher.group(1), matcher.group());
        }
        this.template = template;
    }

    @Override
    public RegexTextWriter copy() {
        return new RegexTextWriter(this);
    }

    @Override
    public String toWriting(Map<String, String> variables, String defaultValue) {
        var result = getTemplate();
        var selectorNames = getSelectors();

        // Replace selectors on variables
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String name = entry.getKey();
            String variable = entry.getValue();
            if (!selectorNames.contains(name)) {
                continue;
            }

            var selector = selectors.get(name);
            result = result.replaceAll(selector, variable);
            selectorNames.remove(name);
        }

        if (!selectorPattern.matcher(result).find()) {
            return result;
        }

        // Replace selectors on default value
        for (String name : selectorNames) {
            var selector = selectors.get(name);
            result = result.replaceAll(selector, defaultValue);
        }

        return result;
    }
}
