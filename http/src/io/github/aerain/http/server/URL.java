package io.github.aerain.http.server;

import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

public class URL {
    private final String url;
    private final Map<String, List<String>> query;

    public URL(String url, Map<String, List<String>> query) {
        this.url = url;
        this.query = query;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, List<String>> getQuery() {
        return query;
    }

    public static URL parse(String url) {
        return parse(url, Charset.defaultCharset());
    }

    public static URL parse(String url, Charset charset) {
        StringTokenizer urlTokenizer = new StringTokenizer(url, "?");
        String requestUrl = urlTokenizer.nextToken();
        Map<String, List<String>> query;
        if (urlTokenizer.hasMoreTokens()) {
            query = Arrays.stream(urlTokenizer.nextToken().split("&"))
                    .map(it -> it.split("="))
                    .peek(it -> it[1] = URLDecoder.decode(it[1], charset))
                    .collect(Collectors.groupingBy(
                            (String[] it) -> it[0],
                            Collectors.mapping(it -> it[1], Collectors.toList())));
        } else {
            query = new HashMap<>();
        }
        return new URL(requestUrl, query);
    }

    @Override
    public String toString() {
        return "URL { url=" + url + "\n"
            + "query=" + query + "}";
    }
}
