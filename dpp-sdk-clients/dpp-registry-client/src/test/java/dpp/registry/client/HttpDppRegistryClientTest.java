package dpp.registry.client;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import dpp.registry.client.exception.DppApiClientException;
import dpp.registry.client.exception.DppMappingClientException;
import dpp.registry.payloads.DppStatusCode;
import dpp.registry.payloads.RegisterDppRequest;
import dpp.registry.payloads.RegisterDppResponse;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HttpDppRegistryClientTest {
    @Test
    void postNewDppToRegistryRejectsNullBeforeNetworkWithCause() throws IOException {
        try (TestServer server = TestServer.start()) {
            AtomicInteger hits = new AtomicInteger();
            server.httpServer().createContext("/v1/registerDPP", exchange -> {
                hits.incrementAndGet();
                respond(exchange, 500, "{}");
            });
            DppRegistryClient client = new HttpDppRegistryClient(server.baseUrl());

            DppMappingClientException ex = assertThrows(
                    DppMappingClientException.class,
                    () -> client.postNewDppToRegistry(null)
            );

            assertInstanceOf(IllegalArgumentException.class, ex.getCause());
            assertEquals("request must not be null", ex.getCause().getMessage());
            assertEquals(0, hits.get());
        }
    }

    @Test
    void postNewDppToRegistryUsesEn18222PathAndSupportedFieldNames() throws IOException {
        try (TestServer server = TestServer.start()) {
            AtomicInteger hits = new AtomicInteger();
            server.httpServer().createContext("/v1/registerDPP", exchange -> {
                hits.incrementAndGet();
                assertEquals("POST", exchange.getRequestMethod());
                assertEquals(
                        "{\"uniqueProductIdentifier\":\"product-1\",\"digitalProductPassportId\":\"dpp-1\",\"uniqueEconomicOperatorIdentifier\":\"operator-1\",\"dppApiEndpoint\":\"http://localhost:8082\"}",
                        requestBody(exchange)
                );
                respond(exchange, 201, "{\"statusCode\":\"SuccessCreated\",\"payload\":{\"registrationId\":\"registry-1\"},\"messages\":[]}");
            });

            DppRegistryClient client = new HttpDppRegistryClient(server.baseUrl());

            RegisterDppResponse response = client.postNewDppToRegistry(
                    new RegisterDppRequest("product-1", "dpp-1", "operator-1", "http://localhost:8082")
            );

            assertEquals("registry-1", response.getRegistrationId());
            assertEquals(1, hits.get());
        }
    }

    @Test
    void registryRequestSerializationDoesNotIncludeRemovedBackupFields() {
        try (TestServer server = TestServer.start()) {
            server.httpServer().createContext("/v1/registerDPP", exchange -> {
                String body = requestBody(exchange);
                assertFalse(body.contains("backupOperatorIdentifier"));
                assertFalse(body.contains("backupProviderIdentifier"));
                assertFalse(body.contains("backupProvider"));
                assertFalse(body.contains("backupId"));
                respond(exchange, 201, "{\"statusCode\":\"SuccessCreated\",\"payload\":{\"registrationId\":\"registry-1\"},\"messages\":[]}");
            });
            DppRegistryClient client = new HttpDppRegistryClient(server.baseUrl());

            RegisterDppResponse response = client.postNewDppToRegistry(
                    new RegisterDppRequest("product-1", "dpp-1", "operator-1", "http://localhost:8082")
            );

            assertEquals("registry-1", response.getRegistrationId());
        }
    }

    @Test
    void postNewDppToRegistryRequiresRegistrationId() {
        try (TestServer server = TestServer.start()) {
            server.httpServer().createContext("/v1/registerDPP", exchange ->
                    respond(exchange, 201, "{\"statusCode\":\"SuccessCreated\",\"payload\":{},\"messages\":[]}"));
            DppRegistryClient client = new HttpDppRegistryClient(server.baseUrl());

            DppMappingClientException ex = assertThrows(
                    DppMappingClientException.class,
                    () -> client.postNewDppToRegistry(new RegisterDppRequest("product-1", "dpp-1", "operator-1", "http://localhost:8082"))
            );

            assertEquals("Missing required response field: payload.registrationId", ex.getMessage());
        }
    }

    @Test
    void postNewDppToRegistryAcceptsLegacyRegistryIdentifierDuringTransition() {
        try (TestServer server = TestServer.start()) {
            server.httpServer().createContext("/v1/registerDPP", exchange ->
                    respond(exchange, 201, "{\"statusCode\":\"SuccessCreated\",\"payload\":{\"registryIdentifier\":\"registry-1\"},\"messages\":[]}"));
            DppRegistryClient client = new HttpDppRegistryClient(server.baseUrl());

            RegisterDppResponse response = client.postNewDppToRegistry(
                    new RegisterDppRequest("product-1", "dpp-1", "operator-1", "http://localhost:8082")
            );

            assertEquals("registry-1", response.getRegistrationId());
        }
    }

    @Test
    void wrapperApiErrorsExposeRegistryStatus() {
        try (TestServer server = TestServer.start()) {
            server.httpServer().createContext("/v1/registerDPP", exchange ->
                    respond(exchange, 200, "{\"statusCode\":\"ClientResourceConflict\",\"payload\":null,\"messages\":[{\"messageType\":\"Error\",\"text\":\"duplicate\"}]}"));
            DppRegistryClient client = new HttpDppRegistryClient(server.baseUrl());

            DppApiClientException ex = assertThrows(
                    DppApiClientException.class,
                    () -> client.postNewDppToRegistry(new RegisterDppRequest("product-1", "dpp-1", "operator-1", "http://localhost:8082"))
            );

            assertEquals(DppStatusCode.ClientResourceConflict, ex.statusCode());
            assertEquals("duplicate", ex.messages().get(0).getText());
        }
    }

    private static void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        exchange.getResponseBody().write(bytes);
        exchange.close();
    }

    private static String requestBody(HttpExchange exchange) throws IOException {
        return new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    private static final class TestServer implements AutoCloseable {
        private final HttpServer server;
        private final ExecutorService executor;

        private TestServer(HttpServer server, ExecutorService executor) {
            this.server = server;
            this.executor = executor;
        }

        static TestServer start() {
            try {
                HttpServer httpServer = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
                ExecutorService executor = Executors.newCachedThreadPool();
                httpServer.setExecutor(executor);
                httpServer.start();
                return new TestServer(httpServer, executor);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }

        HttpServer httpServer() {
            return server;
        }

        String baseUrl() {
            return "http://localhost:" + server.getAddress().getPort();
        }

        @Override
        public void close() {
            server.stop(0);
            executor.shutdownNow();
        }
    }
}
