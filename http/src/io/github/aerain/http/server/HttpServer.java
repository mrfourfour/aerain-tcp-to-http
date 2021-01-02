package io.github.aerain.http.server;

import io.github.aerain.http.HttpHeaders;
import io.github.aerain.http.HttpRequest;
import io.github.aerain.http.HttpResponse;
import io.github.aerain.http.HttpStatus;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {
    private static Logger logger = Logger.getLogger(HttpServer.class.getSimpleName());

    private final ServerSocketChannel channel;
    private final RequestHandler requestHandler;
    private final HttpMessageConverter httpMessageConverter;

    public HttpServer(int port) {
        this.channel = configureSocketChannel(port);
        this.requestHandler = new RequestHandler();
        this.httpMessageConverter = new HttpMessageConverter();
    }

    private ServerSocketChannel configureSocketChannel(int port) {
        ServerSocketChannel channel = null;
        try {
            channel = ServerSocketChannel.open();
            channel.configureBlocking(true); // this was intended;
            channel.bind(new InetSocketAddress(port));
            logger.info("socket was bounded with port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channel;
    }

    public void start() {
        if (channel == null) {
            throw new IllegalStateException("channel is null");
        }
        try {
            while (true) {
                acceptRequest();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RequestHandler router() {
        return requestHandler;
    }

    private void acceptRequest() throws IOException {
        SocketChannel socketChannel = channel.accept();
        ByteBuffer byteBuffer = ByteBuffer.allocate(2048);
        socketChannel.read(byteBuffer);
        byteBuffer.flip();

        HttpRequest request = httpMessageConverter.parse(byteBuffer);
        HttpResponse response = handle(request);
        logger.info(response.toString());
        ByteBuffer responseBuffer = httpMessageConverter.toByteBuffer(response);
        socketChannel.write(responseBuffer);

        socketChannel.finishConnect();
        socketChannel.close();
    }

    private HttpResponse handle(HttpRequest httpRequest) {
        HttpResponse httpResponse;
        try {
            httpResponse = HttpResponse
                    .status(HttpStatus.OK)
                    .httpHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML.getMediaType())
                    .build();
            requestHandler.handle(httpRequest, httpResponse);
        } catch (Exception exception) {
            logger.log(Level.WARNING, "Exception occurred", exception);
            httpResponse = HttpResponse
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(exception.getMessage())
                    .build();
            httpMessageConverter.toByteBuffer(httpResponse);
        }

        return httpResponse;
    }
}
