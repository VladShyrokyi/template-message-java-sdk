package template_message_sdk;

public final class DefaultRegex {
    public static final String REGEX = "%\\[([^%,\\s]+)\\]%";
    public static final String DYNAMIC_VARIABLE_NAME = "DYN_VAR";

    private DefaultRegex() {}

    public static String createSelector(String name) {
        return "%[" + name + "]%";
    }
}
