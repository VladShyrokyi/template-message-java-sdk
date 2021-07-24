package template_message_sdk.block;

import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.exceptions.VariableNameNullPointException;
import template_message_sdk.exceptions.VariableNullPointException;
import template_message_sdk.writer.TextWriterContract;

import java.util.HashMap;
import java.util.Map;

public class CompositeTemplateBlockImpl implements TextBlockContract,
        TextBlockHaveVariablesContract<TextBlockContract> {
    private final Map<String, TextBlockContract> variables = new HashMap<>();

    private TextWriterContract writer;
    private TextEditorContract editor;

    public CompositeTemplateBlockImpl(TextWriterContract writer, TextEditorContract editor) {
        if (writer == null) {
            throw new NullPointerException("Writer can not be null!");
        }
        this.writer = writer;
        this.editor = editor;
    }

    public CompositeTemplateBlockImpl(CompositeTemplateBlockImpl block) {
        if (block == null) {
            throw new NullPointerException("Block can not be null!");
        }
        writer = block.getWriter().copy();
        if (block.getEditor() != null) {
            editor = block.getEditor().copy();
        }
        block.variables.forEach((name, variable) -> putVariable(name, variable.copy()));
    }

    @Override
    public TextBlockContract getVariable(String name) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        return variables.get(name);
    }

    @Override
    public void putVariable(String name, TextBlockContract variable) {
        if (name == null) {
            throw new VariableNameNullPointException(this);
        }
        if (variable == null) {
            throw new VariableNullPointException(this);
        }

        variables.put(name, variable);
    }

    @Override
    public TextWriterContract getWriter() {
        return writer;
    }

    @Override
    public void setWriter(TextWriterContract writer) {
        this.writer = writer;
    }

    @Override
    public TextEditorContract getEditor() {
        return editor;
    }

    @Override
    public void setEditor(TextEditorContract editor) {
        this.editor = editor;
    }

    @Override
    public TextBlockContract copy() {
        return new CompositeTemplateBlockImpl(this);
    }

    @Override
    public String write() {
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.write()));

        return editor != null
               ? editor.toEditing(writer.toWriting(stringVariables, ""))
               : writer.toWriting(stringVariables, "");
    }

    @Override
    public String writeWithEditor(TextEditorContract editor) {
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.writeWithEditor(editor)));

        return editor.toEditing(writer.toWriting(stringVariables, ""));
    }

    @Override
    public String writeWithoutEditor() {
        var stringVariables = new HashMap<String, String>();
        variables.forEach((name, block) -> stringVariables.put(name, block.writeWithoutEditor()));

        return writer.toWriting(stringVariables, "");
    }
}
