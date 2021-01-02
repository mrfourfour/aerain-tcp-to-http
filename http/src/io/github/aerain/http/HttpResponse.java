package io.github.aerain.http;

import java.util.HashMap;
import java.util.Optional;

public class HttpResponse {
    public static final HttpResponse NOT_FOUND = HttpResponse.status(HttpStatus.NOT_FOUND).build();
    private static final String HTTP_1_1 = "HTTP/1.1";

    private String httpVersion;
    private HttpStatus httpStatus;
    private HttpHeaders httpHeaders;
    private String body;


    public HttpResponse(String httpVersion, HttpStatus httpStatus, HttpHeaders httpHeaders, String body) {
        this.httpVersion = httpVersion;
        this.httpStatus = httpStatus;
        this.httpHeaders = httpHeaders;
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "httpVersion='" + httpVersion + '\'' +
                ", httpStatus=" + httpStatus +
                ", httpHeaders=" + httpHeaders +
                ", body='" + body + '\'' +
                '}';
    }

    public HttpHeaders getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(HttpHeaders httpHeaders) {
        this.httpHeaders = httpHeaders;
    }

    public String getHttpVersion() {
        return httpVersion;
    }

    public void setHttpVersion(String httpVersion) {
        this.httpVersion = httpVersion;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public static HttpResponseBuilder status(HttpStatus notFound) {
        return new HttpResponseBuilder()
                .status(notFound);
    }

    public static class HttpResponseBuilder {
        private static final String EMPTY_BODY = "";
        private String httpVersion = null;
        private HttpStatus httpStatus = null;
        private String body = null;
        private HttpHeaders httpHeaders = HttpHeaders.of(new HashMap<>());

        public HttpResponseBuilder status(HttpStatus httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public HttpResponseBuilder body(String body) {
            this.body = body;
            return this;
        }

        public HttpResponseBuilder httpHeader(String key, String value) {
            httpHeaders.put(key, value);
            return this;
        }

        public HttpResponse build() {
            String httpVersion = Optional
                    .ofNullable(this.httpVersion)
                    .orElse(HTTP_1_1);

            HttpStatus httpStatus = Optional
                    .ofNullable(this.httpStatus)
                    .orElse(HttpStatus.OK);

            String body = Optional
                    .ofNullable(this.body)
                    .orElse(EMPTY_BODY);

            HttpHe5ders httpHeaders = Optional
                    .ofNullable(this.httpHeaders)
                    .orElse(HttpHeaders.of(new HashMap<>()));


            return new HttpResponse(httpVersion, httpStatus, httpHeaders, body);
        }
    }
}
