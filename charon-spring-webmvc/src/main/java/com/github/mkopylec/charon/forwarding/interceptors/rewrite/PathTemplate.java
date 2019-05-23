package com.github.mkopylec.charon.forwarding.interceptors.rewrite;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang3.StringUtils.isBlank;

class PathTemplate {

    private static final Pattern placeholderSearchPattern = compile("<([^>]*)");

    private String value;
    private List<String> placeholders = new ArrayList<>();

    PathTemplate(String value) {
        this.value = value;
        findPlaceholders(value);
    }

    String fill(Matcher matcher) {
        String filledValue = value;
        for (String placeholder : placeholders) {
            String group = matcher.group(placeholder);
            filledValue = filledValue.replace("<" + placeholder + ">", group);
        }
        return filledValue;
    }

    boolean isEmpty() {
        return isBlank(value);
    }

    private void findPlaceholders(String value) {
        Matcher matcher = placeholderSearchPattern.matcher(value);
        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }
    }
}
