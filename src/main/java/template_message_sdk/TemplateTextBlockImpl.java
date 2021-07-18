package template_message_sdk;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class TemplateTextBlockImpl implements TextBlockContract {
    private final String regex;
    private final Pattern selectorPattern;
    private final Map<String, TextBlockContract> variables = new HashMap<>();

    public TextEditor editor;
    private String template = "";
    private Map<String, String> selectors = new HashMap<>();

    public TemplateTextBlockImpl(String template, String regex, TextEditor editor) {
        this.regex = regex;
        selectorPattern = Pattern.compile(regex);
        this.editor = editor;
        setTemplate(template);
    }

    public TemplateTextBlockImpl(String template) {
        this(template, DefaultRegex.REGEX);
    }

    public TemplateTextBlockImpl(String template, String regex) {
        this(template, regex, null);
    }

    public TemplateTextBlockImpl() {
        this("");
    }

    public TemplateTextBlockImpl(TemplateTextBlockImpl template) {
        this(template.template, template.regex, template.getEditor());
        variables.forEach((name, variable) -> putVariable(name, variable.copy()));
    }

    public Set<String> getSelectors() {
        return Set.copyOf(selectors.keySet());
    }

    public void setTemplate(String template) {
        selectors = new HashMap<>();
        var match = selectorPattern.matcher(template);
        while (match.find()) {
            selectors.put(match.group(1), match.group());
        }
        this.template = template;
    }

    public TextBlockContract getVariable(String name) {
        if (name == null) {
            return null;
        }
        return variables.get(name);
    }

    public void putVariable(String name, TextBlockContract variable) throws NullPointerException {
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
        return new TemplateTextBlockImpl(this);
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
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.write()));

        return editor != null
               ? editor.toEditing(write(stringVariables, ""))
               : write(stringVariables, "");
    }

    @Override
    public String writeWithEditor(TextEditor editor) {
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.writeWithEditor(editor)));

        return editor.toEditing(write(stringVariables, ""));
    }

    @Override
    public String writeWithoutEditor() {
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.writeWithoutEditor()));

        return write(stringVariables, "");
    }

    public String write(Map<String, String> variables, String defaultValue) {
        var result = template;
        var selectorNames = getSelectors();

        // Replace selectors on variables
        for (var entry : variables.entrySet()) {
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
