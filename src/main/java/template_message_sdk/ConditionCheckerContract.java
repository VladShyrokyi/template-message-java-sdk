package template_message_sdk;

public interface ConditionCheckerContract {
    boolean Check(TextBlockContract block);

    void Update(TextBlockContract block);
}
