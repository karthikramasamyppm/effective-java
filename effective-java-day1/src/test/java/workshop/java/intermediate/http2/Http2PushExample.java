package workshop.java.intermediate.http2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

public class Http2PushExample {

    private static ExecutorService executor = Executors.newFixedThreadPool(20, new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            System.out.println("Created new executor " + (thread.isDaemon() ? "daemon " : "") + "thread with thread group: " + thread.getThreadGroup());
            return thread;
        }
    });

    @Test
    void run() {
        System.out.println("Running HTTP/2 example with push promises...");
        try {
            HttpClient httpClient = HttpClient.newBuilder()
                    .version(Version.HTTP_2)
                    .proxy(ProxySelector.getDefault())
                    .build();

            long start = System.currentTimeMillis();

            HttpRequest mainRequest = HttpRequest.newBuilder()
                    .uri(URI.create("https://http2.akamai.com/demo/h2_demo_frame_sp2.html?pushnum=20"))
                    .build();

            List<Future<?>> futures = new ArrayList<>();

            Set<String> pushedImages = new HashSet<>();

            httpClient.sendAsync(mainRequest, BodyHandlers.ofString(), (initiatingRequest, pushPromiseRequest, acceptor) -> {
                System.out.println("Got promise request " + pushPromiseRequest.uri());
                acceptor.apply(BodyHandlers.ofString()).thenAccept(resp -> {
                    System.out.println("Got pushed response " + resp.uri());
                    pushedImages.add(resp.uri().toString());
                });
            }).thenAccept(mainResponse -> {

                System.out.println("Main response status code: " + mainResponse.statusCode());
                System.out.println("Main response headers: " + mainResponse.headers());
                String responseBody = mainResponse.body();
                System.out.println(responseBody);

                // For each image resource in the main HTML, send a request on a separate thread
                responseBody.lines()
                        .filter(line -> line.trim().startsWith("<img height"))
                        .map(line -> line.substring(line.indexOf("src='") + 5, line.indexOf("'/>")))
                        .filter(image -> !pushedImages.contains("https://http2.akamai.com" + image))
                        .forEach(image -> {

                            Future<?> imgFuture = executor.submit(() -> {
                                HttpRequest imgRequest = HttpRequest.newBuilder()
                                        .uri(URI.create("https://http2.akamai.com" + image))
                                        .build();
                                try {
                                    HttpResponse<String> imageResponse = httpClient.send(imgRequest, BodyHandlers.ofString());
                                    System.out.println("Loaded " + image + ", status code: " + imageResponse.statusCode());
                                } catch (IOException | InterruptedException ex) {
                                    System.out.println("Error during image request for " + image + ex);
                                }
                            });
                            futures.add(imgFuture);
                            System.out.println("Submitted future for image " + image);
                        });

            }).join();

            // Wait for all submitted image loads to be completed
            futures.forEach(f -> {
                try {
                    f.get();
                } catch (InterruptedException | ExecutionException ex) {
                    System.out.println("Error waiting for image load" + ex);
                }
            });

            long end = System.currentTimeMillis();
            System.out.println("Total load time: " + (end - start) + " ms");
        } finally {
            executor.shutdown();
        }
    }

}