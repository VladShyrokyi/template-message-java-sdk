package org.vlad_shyrokyi.template_message_sdk.writer;

import java.util.Map;

public interface TextWriterContract {
    String getTemplate();

    void setTemplate(String template);

    TextWriterContract copy();

    String toWriting(Map<String, String> variables, String defaultValue);
}
