package io.github.aerain.http.server;

import io.github.aerain.http.HttpHeaders;
import io.github.aerain.http.HttpMethod;
import io.github.aerain.http.HttpRequest;
import io.github.aerain.http.HttpResponse;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Logger;

public class HttpMessageConverter {
    private static final Logger logger = Logger.getLogger(HttpMessageConverter.class.getSimpleName());

    private static String parseEntityContent(StringTokenizer tokenizer) {
        StringBuilder stringBuilder = new StringBuilder();
        if (tokenizer.hasMoreTokens()) {
            stringBuilder.append(tokenizer.nextToken());
        }
        return stringBuilder.toString();
    }

    private static Map<String, List<String>> parseHeader(final StringTokenizer tokenizer) {
        String line;
        StringTokenizer headerTokenizer;
        Map<String, List<String>> headerMap = new HashMap<>();
        while (tokenizer.hasMoreTokens()) {
            line = tokenizer.nextToken();
            System.out.println(line);
            if (line == null || line.isBlank()) {
                break;
            }
            headerTokenizer = new StringTokenizer(line, ":");
            String key = headerTokenizer.nextToken();
            String value = headerTokenizer.nextToken().trim();

            if (!headerMap.containsKey(key)) {
                headerMap.put(key, new ArrayList<>());
            }

            headerMap.get(key).add(value);
        }
        return headerMap;
    }

    public HttpRequest parse(ByteBuffer byteBuffer) {
        return parse(byteBuffer, Charset.defaultCharset());
    }

    public HttpRequest parse(ByteBuffer byteBuffer, Charset charset) {
        StringTokenizer tokenizer = new StringTokenizer(charset.decode(byteBuffer).toString(), "\r\n");
        String[] methodAndUrlAndVersion = tokenizer.nextToken().split(" ");

        HttpMethod httpMethod = HttpMethod.valueOf(methodAndUrlAndVersion[0]);
        URL url = URL.parse(methodAndUrlAndVersion[1]);
        String httpVersion = methodAndUrlAndVersion[2];

        logger.info(url.toString());

        Map<String, List<String>> headerMap = parseHeader(tokenizer);
        HttpHeaders httpHeaders = HttpHeaders.of(headerMap);

        String entityContent = parseEntityContent(tokenizer);

        return new HttpRequest(
                httpMethod,
                url.getUrl(),
                httpVersion,
                httpHeaders,
                url.getQuery(),
                entityContent
        );
    }

    public ByteBuffer toByteBuffer(HttpResponse httpResponse) {
        return toByteBuffer(httpResponse, Charset.defaultCharset());
    }

    public ByteBuffer toByteBuffer(HttpResponse httpResponse, Charset charset) {
        StringBuilder sb = new StringBuilder();
        sb
                .append(httpResponse.getHttpVersion())
                .append(" ")
                .append(httpResponse.getHttpStatus().getCode())
                .append(" ")
                .append(httpResponse.getHttpStatus().getDescription())
                .append("\r\n");

        httpResponse.getHttpHeaders().forEach(it -> {
            String key = it.getKey();
            it.getValue().forEach(headerValue -> {
                sb.append(key).append(": ").append(headerValue).append("\r\n");
            });
        });
        sb.append("\r\n").append(httpResponse.getBody()).append("\r\n");

        return charset.encode(sb.toString());
    }
}
