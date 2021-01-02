package io.github.aerain.http;

import java.util.*;
import java.util.function.Consumer;

public class HttpHeaders {

    public static final String CONTENT_TYPE = "Content-Type";

    private final Map<String, List<String>> headerMap;

    private HttpHeaders(Map<String, List<String>> headerMap) {
        this.headerMap = Optional.ofNullable(headerMap).orElse(new HashMap<>());
    }

    public HttpHeaders put(String key, String value) {
        this.headerMap
                .computeIfAbsent(key, it -> new ArrayList<>())
                .add(value);
        return this;
    }

    public Optional<String> firstValue(String name) {
        return Optional.ofNullable(name)
                .map(headerMap::get)
                .filter(it -> !it.isEmpty())
                .map(it -> it.get(0));
    }

    public void forEach(Consumer<Map.Entry<String, List<String>>> consumer) {
        headerMap.entrySet().forEach(consumer);
    }

    @Override
    public String toString() {
        return "HttpHeaders { " + headerMap.toString() + " }";
    }

    public static HttpHeaders of(Map<String, List<String>> headerMap) {
        return new HttpHeaders(headerMap);
    }
}
