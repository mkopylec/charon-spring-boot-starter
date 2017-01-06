package com.github.mkopylec.charon.application;

import com.github.mkopylec.charon.configuration.CharonProperties;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;

import static java.nio.ByteBuffer.allocate;
import static java.nio.channels.SelectionKey.OP_ACCEPT;
import static java.nio.channels.SelectionKey.OP_READ;
import static java.nio.charset.Charset.forName;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * This class in not thread safe.
 * Source http://www.programcreek.com/java-api-examples/index.php?source_dir=appmon4j-master/testutil/src/main/java/de/is24/util/monitoring/tools/GraphiteMockServer.java
 */
@Component
@ConditionalOnProperty("charon.metrics.reporting.graphite.enabled")
@EnableConfigurationProperties(CharonProperties.class)
public class GraphiteServerMock {

    private static final Logger log = getLogger(GraphiteServerMock.class);

    private final CharonProperties charon;
    private ServerSocketChannel server;
    private Selector selector;
    private Thread thread;
    private boolean metricsCaptured;

    @Autowired
    public GraphiteServerMock(CharonProperties charon) {
        this.charon = charon;
    }

    public boolean isMetricsCaptured() {
        return metricsCaptured;
    }

    @PostConstruct
    private void start() throws IOException {
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.socket().bind(new InetSocketAddress(charon.getMetrics().getReporting().getGraphite().getPort()));
        selector = Selector.open();
        server.register(selector, OP_ACCEPT);
        thread = new Thread(() -> {
            CharsetDecoder decoder = forName("UTF-8").newDecoder();
            try {
                while (!thread.isInterrupted()) {
                    selector.select();
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (!key.isValid()) {
                            continue;
                        }
                        if (key.isAcceptable()) {
                            SocketChannel client = server.accept();
                            client.configureBlocking(false);
                            client.register(selector, OP_READ);
                            continue;
                        }
                        if (key.isReadable()) {
                            SocketChannel client = (SocketChannel) key.channel();
                            ByteBuffer buffer = allocate(1024);
                            try {
                                int bytesRead = client.read(buffer);
                                if (bytesRead > 0) {
                                    buffer.flip();
                                    metricsCaptured = decoder.decode(buffer).toString().startsWith(charon.getMetrics().getNamesPrefix() + ".");
                                }
                            } catch (Exception e) {
                                log.error("Error reading data sent to Graphite", e);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Error reading data sent to Graphite", e);
            }
        });
        thread.start();
    }

    @PreDestroy
    private void stop() {
        try {
            thread.interrupt();
        } catch (Exception e) {
        }
        try {
            selector.close();
        } catch (IOException e) {
        }
        try {
            server.close();
        } catch (Exception e) {
        }
    }
}
