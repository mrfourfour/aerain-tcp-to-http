package io.github.aerain.http.server;

public enum MediaType {
    APPLICATION_JSON("application/json"),
    TEXT_HTML("text/html");

    private final String mediaType;

    MediaType(String mediaType) {
       this.mediaType = mediaType;
    }

    public String getMediaType() {
        return mediaType;
    }
}
