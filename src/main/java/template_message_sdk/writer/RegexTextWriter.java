package template_message_sdk.writer;

import template_message_sdk.DefaultRegex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class RegexTextWriter {
    private final String regex;
    private final Pattern selectorPattern;
    private Map<String, String> selectors = new HashMap<>();

    private String template;

    public RegexTextWriter(String template, String regex) {
        this.regex = regex;
        selectorPattern = Pattern.compile(regex);
        setTemplate(template);
    }

    public RegexTextWriter(String template) {
        this(template, DefaultRegex.REGEX);
    }

    public RegexTextWriter(RegexTextWriter writer) {
        this(writer.template, writer.regex);
    }

    public RegexTextWriter() {
        this("");
    }

    public String getRegex() {
        return regex;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        selectors = new HashMap<>();
        var matcher = selectorPattern.matcher(template);
        while (matcher.find()) {
            selectors.put(matcher.group(1), matcher.group());
        }
        this.template = template;
    }

    public Set<String> getSelectors() {
        return Set.copyOf(selectors.keySet());
    }

    public RegexTextWriter copy() {
        return new RegexTextWriter(this);
    }

    public String write(Map<String, String> variables, String defaultValue) {
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
