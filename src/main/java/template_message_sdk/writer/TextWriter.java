package template_message_sdk.writer;

import java.util.Map;

public interface TextWriter {
    String getTemplate();

    void setTemplate(String template);

    TextWriter copy();

    String toWriting(Map<String, String> variables, String defaultValue);
}
