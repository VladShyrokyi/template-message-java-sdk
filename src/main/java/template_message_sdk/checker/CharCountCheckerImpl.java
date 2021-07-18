package template_message_sdk.checker;

import template_message_sdk.block.TextBlockContract;

public class CharCountCheckerImpl implements ConditionCheckerContract {
    private int limit = 0;

    public CharCountCheckerImpl(int limit) {
        this.limit = limit;
    }

    @Override
    public boolean Check(TextBlockContract block) {
        return limit - block.write().length() >= 0;
    }

    @Override
    public void Update(TextBlockContract block) {
        limit -= block.write().length();
    }

    public int getLimit() {
        return limit;
    }
}
