package com.github.mkopylec.charon.core.http;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.github.mkopylec.charon.exceptions.CharonException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import static java.util.Collections.list;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpMethod.resolve;

public class RequestDataExtractor {

    protected static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";
    protected static final Charset FORM_CHARSET = StandardCharsets.UTF_8;

    public byte[] extractBody(HttpServletRequest request) {
        try {
            return toByteArray(getBody(request));
        } catch (IOException e) {
            throw new CharonException("Error extracting body of HTTP request with URI: " + extractUri(request), e);
        }
    }

    public HttpHeaders extractHttpHeaders(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            List<String> value = list(request.getHeaders(name));
            headers.put(name, value);
        }
        return headers;
    }

    public HttpMethod extractHttpMethod(HttpServletRequest request) {
        return resolve(request.getMethod());
    }

    public String extractUri(HttpServletRequest request) {
        return request.getRequestURI() + getQuery(request);
    }

    protected String getQuery(HttpServletRequest request) {
        return request.getQueryString() == null ? EMPTY : "?" + request.getQueryString();
    }

    private InputStream getBody(HttpServletRequest request) throws IOException {
        if (isFormPost(request)) {
            return getBodyFromServletRequestParameters(request);
        } else {
            return request.getInputStream();
        }
    }

    private static boolean isFormPost(HttpServletRequest request) {
        String contentType = request.getContentType();
        return (contentType != null && contentType.contains(FORM_CONTENT_TYPE) &&
                HttpMethod.POST.matches(request.getMethod()));
    }

    /**
     * Use {@link javax.servlet.ServletRequest#getParameterMap()} to reconstruct the
     * body of a form 'POST' providing a predictable outcome as opposed to reading
     * from the body, which can fail if any other code has used the ServletRequest
     * to access a parameter, thus causing the input stream to be "consumed".
     */
    private static InputStream getBodyFromServletRequestParameters(HttpServletRequest request) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        Writer writer = new OutputStreamWriter(bos, FORM_CHARSET);

        Map<String, String[]> form = request.getParameterMap();
        for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext();) {
            String name = nameIterator.next();
            List<String> values = Arrays.asList(form.get(name));
            for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext();) {
                String value = valueIterator.next();
                writer.write(URLEncoder.encode(name, FORM_CHARSET.name()));
                if (value != null) {
                    writer.write('=');
                    writer.write(URLEncoder.encode(value, FORM_CHARSET.name()));
                    if (valueIterator.hasNext()) {
                        writer.write('&');
                    }
                }
            }
            if (nameIterator.hasNext()) {
                writer.append('&');
            }
        }
        writer.flush();

        return new ByteArrayInputStream(bos.toByteArray());
    }
}
