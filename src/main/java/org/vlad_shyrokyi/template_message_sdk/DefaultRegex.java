package org.vlad_shyrokyi.template_message_sdk;

import java.util.function.Function;

public final class DefaultRegex {
    public static final String REGEX = "%\\[([^%,\\s]+)\\]%";
    public static final String DYNAMIC_VARIABLE_NAME = "DYN_VAR";
    public static final Function<String, String> SELECTOR_FACTORY = name -> "%[" + name + "]%";
}
