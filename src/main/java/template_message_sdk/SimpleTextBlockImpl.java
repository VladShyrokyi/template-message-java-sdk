package template_message_sdk;

public class SimpleTextBlockImpl implements TextBlockContract {
    private String value;
    private TextEditor editor;

    public SimpleTextBlockImpl(String value) {
        this.value = value;
    }

    public static SimpleTextBlockImpl valueOf(String value) {
        return new SimpleTextBlockImpl(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public TextBlockContract copy() {
        return new SimpleTextBlockImpl(value);
    }

    @Override
    public void setEditor(TextEditor editor) {
        this.editor = editor;
    }

    @Override
    public TextEditor getEditor() {
        return editor;
    }

    @Override
    public String write() {
        return editor != null ? editor.toEditing(value) : value;
    }

    @Override
    public String writeWithEditor(TextEditor editor) {
        return editor.toEditing(value);
    }

    @Override
    public String writeWithoutEditor() {
        return value;
    }
}
