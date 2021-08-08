package template_message_sdk.block;

import template_message_sdk.block.interfaces.TextBlockContract;
import template_message_sdk.editor.TextEditorContract;
import template_message_sdk.writer.TextWriterContract;

import java.util.Map;

public class InvariantBlockImpl implements TextBlockContract {
    private TextWriterContract writer;
    private TextEditorContract editor;

    public InvariantBlockImpl(TextWriterContract writer, TextEditorContract editor) {
        if (writer == null) {
            throw new NullPointerException("Writer can not be null!");
        }
        this.writer = writer;
        this.editor = editor;
    }

    public InvariantBlockImpl(InvariantBlockImpl block) {
        if (block == null) {
            throw new NullPointerException("Block can not be null!");
        }
        this.writer = block.getWriter().copy();
        var editor = block.getEditor();
        if (editor != null) {
            this.editor = editor.copy();
        }
    }

    @Override
    public TextWriterContract getWriter() {
        return this.writer;
    }

    @Override
    public void setWriter(TextWriterContract writer) {
        this.writer = writer;
    }

    @Override
    public TextEditorContract getEditor() {
        return this.editor;
    }

    @Override
    public void setEditor(TextEditorContract editor) {
        this.editor = editor;
    }

    @Override
    public TextBlockContract copy() {
        return new InvariantBlockImpl(this);
    }

    @Override
    public String write() {
        return editor != null
               ? editor.toEditing(writer.toWriting(Map.of(), ""))
               : writer.toWriting(Map.of(), "");
    }

    @Override
    public String writeWithEditor(TextEditorContract editor) {
        return editor.toEditing(writer.toWriting(Map.of(), ""));
    }

    @Override
    public String writeWithoutEditor() {
        return writer.toWriting(Map.of(), "");
    }
}
