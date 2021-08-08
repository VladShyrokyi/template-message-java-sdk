package template_message_sdk.builder;

import template_message_sdk.block.interfaces.TextBlockContract;

public interface BlockBuilderContract {
    void append(TextBlockContract variable);

    TextBlockContract build();
}
