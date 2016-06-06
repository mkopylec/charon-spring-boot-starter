package com.github.mkopylec.reverseproxy.utils;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.prependIfMissing;
import static org.apache.commons.lang3.StringUtils.removeEnd;

public class UriCorrector {

    public static String correctUri(String uri) {
        if (isBlank(uri)) {
            return EMPTY;
        }
        return removeEnd(prependIfMissing(uri, "/"), "/");
    }
}
