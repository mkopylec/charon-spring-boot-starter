package com.github.mkopylec.charon.core.utils;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.github.mkopylec.charon.exceptions.CharonException;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.springframework.web.client.HttpStatusCodeException;

public class ResponseBodyUtils {

    public static void copyBody(CloseableHttpResponse destinationResponse, HttpServletResponse response) {
        try {
            copy(destinationResponse.getEntity().getContent(), response.getOutputStream());
        } catch (IOException e) {
            throw new CharonException("Error copying HTTP response body", e);
        }
    }

    public static void copyBody(HttpStatusCodeException httpError, HttpServletResponse response) {
        try {
            write(httpError.getResponseBodyAsByteArray(), response.getOutputStream());
        } catch (IOException e) {
            throw new CharonException("Error copying HTTP response body", e);
        }
    }

//    public static final Charset RESPONSE_CHARSET = forName("UTF-8");
//
//    public static String convertBodyToString(byte[] body) {
//        if (body == null) {
//            return null;
//        }
//        return new String(body, RESPONSE_CHARSET);
//    }
//
//    public static byte[] convertStringToBody(String body) {
//        if (body == null) {
//            return null;
//        }
//        return body.getBytes(RESPONSE_CHARSET);
//    }
//
//    protected static HttpClientErrorException convertBodyToClientError(CloseableHttpResponse destinationResponse) {
//        return convertBodyToHttpError(destinationResponse, (status, body) -> new HttpClientErrorException(status, status.getReasonPhrase(), body, RESPONSE_CHARSET));
//    }
//
//    protected static HttpServerErrorException convertBodyToServerError(CloseableHttpResponse destinationResponse) {
//        return convertBodyToHttpError(destinationResponse, (status, body) -> new HttpServerErrorException(status, status.getReasonPhrase(), body, RESPONSE_CHARSET));
//    }
//
//    protected static <E extends HttpStatusCodeException> E convertBodyToHttpError(CloseableHttpResponse destinationResponse, BiFunction<HttpStatus, byte[], E> errorCreator) {
//        HttpStatus status = HttpStatus.valueOf(destinationResponse.getStatusLine().getStatusCode());
//        try {
//            byte[] body = toByteArray(destinationResponse.getEntity().getContent());
//            return errorCreator.apply(status, body);
//        } catch (IOException e) {
//            throw new CharonException("Error reading body of destination HTTP response", e);
//        }
//    }
}
