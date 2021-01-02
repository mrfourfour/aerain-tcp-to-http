package io.github.aerain.http;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HttpRequest {
    private final HttpMethod httpMethod;
    private final String requestUrl;
    private final String httpVersion;
    private final HttpHeaders httpHeaders;
    private final String entityContent;
    private final Map<String, List<String>> query;

    public HttpRequest(
            HttpMethod httpMethod,
            String requestUrl,
            String httpVersion,
            HttpHeaders httpHeaders,
            Map<String, List<String>> query,
            String entityContent) {

        this.httpMethod = httpMethod;
        this.requestUrl = requestUrl;
        this.httpVersion = httpVersion;
        this.httpHeaders = httpHeaders;
        this.entityContent = entityContent;
        this.query = query;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public String getEntityContent() {
        return entityContent;
    }

    @Override
    public String toString() {
        return "HttpRequest { httpMethod=" + httpMethod
                + ", requestUrl=" + requestUrl
                + ", httpVersion=" + httpVersion
                + ", httpHeaders=" + httpHeaders
                + ", entityContent=" + entityContent
                + "}";
    }


    public String getQueryParam(String param) {
        List<String> result = query.get(param);
        return Optional.ofNullable(result)
                .filter(it -> !it.isEmpty())
                .filter(it -> it.size() == 1)
                .map(it -> it.get(0))
                .orElse(null);
    }

    public List<String> getQueryParams(String param) {
        return Optional.ofNullable(query.get(param))
                .orElseGet(ArrayList::new);
    }
}
