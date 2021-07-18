package template_message_sdk;

public final class DefaultRegex {
    public static final String REGEX = "%\\[([^%,\\s]+)\\]%";

    private DefaultRegex() {}

    public static String getRegexSelector(String name) {
        return "%[" + name + "]%";
    }
}
