package template_message_sdk.block;

import template_message_sdk.DefaultRegex;
import template_message_sdk.editor.TextEditor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class SimpleTextBlockImpl implements TextBlockContract {
    private final String regex;
    private final Pattern selectorPattern;
    private final Map<String, String> variables = new HashMap<>();

    private TextEditor editor;
    private String template = "";
    private Map<String, String> selectors = new HashMap<>();

    public SimpleTextBlockImpl(String template, String regex, TextEditor editor) {
        this.regex = regex;
        selectorPattern = Pattern.compile(regex);
        this.editor = editor;
        setTemplate(template);
    }

    public SimpleTextBlockImpl(String template, String regex) {
        this(template, regex, null);
    }

    public SimpleTextBlockImpl(String template) {
        this(template, DefaultRegex.REGEX);
    }

    public static SimpleTextBlockImpl valueOf(String template) {
        return new SimpleTextBlockImpl(template);
    }

    public static SimpleTextBlockImpl valueOf(String template, Map<String, String> variables) {
        var block = new SimpleTextBlockImpl(template);
        variables.forEach(block::putVariable);
        return block;
    }

    public Set<String> getSelectors() {
        return Set.copyOf(selectors.keySet());
    }

    public void setTemplate(String template) {
        selectors = new HashMap<>();
        var matcher = selectorPattern.matcher(template);
        while (matcher.find()) {
            selectors.put(matcher.group(1), matcher.group());
        }
        this.template = template;
    }

    public String getVariable(String name) {
        if (name == null) {
            return null;
        }
        return variables.get(name);
    }

    public void putVariable(String name, String variable) {
        if (name == null) {
            throw new NullPointerException("Variable name can not be null!");
        }
        if (variable == null) {
            throw new NullPointerException("Variable can not be null");
        }
        variables.put(name, variable);
    }

    @Override
    public TextBlockContract copy() {
        var block = new SimpleTextBlockImpl(template, regex, editor);
        variables.forEach(block::putVariable);
        return block;
    }

    @Override
    public TextEditor getEditor() {
        return editor;
    }

    @Override
    public void setEditor(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public String write() {
        return editor != null
               ? editor.toEditing(write(Map.copyOf(variables), ""))
               : write(Map.copyOf(variables), "");
    }

    @Override
    public String writeWithEditor(TextEditor editor) {
        return editor.toEditing(write(Map.copyOf(variables), ""));
    }

    @Override
    public String writeWithoutEditor() {
        return editor.toEditing(write(Map.copyOf(variables), ""));
    }

    public String write(Map<String, String> variables, String defaultValue) {
        var result = template;
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
