package com.github.mkopylec.charon.workaround;

import org.apache.http.ConnectionClosedException;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Converter from octet-stream to byte[]
 *
 * This converter is tolerant to "Connection closed" exceptions that can arise when some
 * chunked requests are invalid
 *
 */
public class PrematureEndOfChunkHttpMessageConverter extends ByteArrayHttpMessageConverter {

    @Override
    public byte[] readInternal(Class<? extends byte[]> clazz, HttpInputMessage inputMessage) throws IOException {
        long contentLength = inputMessage.getHeaders().getContentLength();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(contentLength >= 0L ? (int)contentLength : 4096);
        try {
            StreamUtils.copy(inputMessage.getBody(), bos);
        } catch (ConnectionClosedException e) {
            String msg = e.getMessage();
            if (msg.equals("Premature end of chunk coded message body: closing chunk expected")) {
                // Do nothing, hope the response is complete enough
            }
            else {
                throw e;
            }
        }
        return bos.toByteArray();
    }
}
