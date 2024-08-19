package tests;

import com.google.gson.Gson;
import http.HttpTaskServer;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PrioritizedHttpTest {
    private static TaskManager taskManager;
    private static HttpTaskServer httpTaskServer;

    Gson gson = HttpTaskServer.getGson();

    @BeforeAll
    static void setUp() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
    }

    @BeforeEach
    void startServer() throws IOException {
        httpTaskServer.start();
    }

    @AfterEach
    void stopServer() throws IOException {
        httpTaskServer.stop();
    }

    @Test
    void getPrioritized() throws IOException, InterruptedException {
        Task task = new Task("task", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now(), null);
        Task task1 = new Task("task1", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now().plusMinutes(1), null);
        Task task2 = new Task("task2", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now().plusMinutes(2), null);
        Task task3 = new Task("task3", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now().plusMinutes(3), null);
        String taskJson = gson.toJson(task);
        String taskJson1 = gson.toJson(task1);
        String taskJson2 = gson.toJson(task2);
        String taskJson3 = gson.toJson(task3);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson1))
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response1.statusCode());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson2))
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response2.statusCode());

        HttpRequest request3 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson3))
                .build();
        HttpResponse<String> response3 = client.send(request3, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response3.statusCode());

        URI urlPrioritized = URI.create("http://localhost:8080/prioritized");
        HttpRequest request4 = HttpRequest.newBuilder()
                .uri(urlPrioritized)
                .GET()
                .build();
        HttpResponse<String> response4 = client.send(request4, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response4.statusCode());

        Task[] tasks = gson.fromJson(response4.body(), Task[].class);
        assertEquals(4, tasks.length, "Количество задач не совпадает");
    }
}