package org.vlad_shyrokyi.template_message_sdk.checker;

import org.vlad_shyrokyi.template_message_sdk.block.interfaces.TextBlockContract;

public interface ConditionCheckerContract {
    boolean Check(TextBlockContract block);

    void Update(TextBlockContract block);
}
