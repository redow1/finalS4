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

class HistoryHttpTest {
    private static TaskManager taskManager;
    private static HttpTaskServer httpTaskServer;

    Gson gson = HttpTaskServer.getGson();

    @BeforeAll
    static void setUp() throws IOException {
        taskManager = Managers.getDefault();
        if (httpTaskServer != null) {
            httpTaskServer.stop();
        }
        httpTaskServer = new HttpTaskServer(taskManager);
    }

    @BeforeEach
    void startServer() throws IOException {
        if (httpTaskServer != null) {
            httpTaskServer.stop();
        }
        httpTaskServer.start();
    }

    @AfterEach
    void stopServer() throws IOException {
        if (httpTaskServer != null) {
            httpTaskServer.stop();
        }
    }

    @Test
    void getHistory() throws IOException, InterruptedException {
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

        String uuid = taskManager.getTasks().get(0).getUuid();
        String uuid1 = taskManager.getTasks().get(1).getUuid();
        String uuid2 = taskManager.getTasks().get(2).getUuid();
        String uuid3 = taskManager.getTasks().get(3).getUuid();

        URI urlGet = URI.create("http://localhost:8080/tasks/"+ uuid);
        URI urlGet1 = URI.create("http://localhost:8080/tasks/"+ uuid1);
        URI urlGet2 = URI.create("http://localhost:8080/tasks/"+ uuid2);
        URI urlGet3 = URI.create("http://localhost:8080/tasks/"+ uuid3);

        HttpRequest requestGet = HttpRequest.newBuilder()
                .uri(urlGet)
                .GET()
                .build();
        HttpResponse<String> responseGet = client.send(requestGet, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet.statusCode());

        HttpRequest requestGet1 = HttpRequest.newBuilder()
                .uri(urlGet1)
                .GET()
                .build();
        HttpResponse<String> responseGet1 = client.send(requestGet1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet1.statusCode());

        HttpRequest requestGet2 = HttpRequest.newBuilder()
                .uri(urlGet2)
                .GET()
                .build();
        HttpResponse<String> responseGet2 = client.send(requestGet2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet2.statusCode());

        HttpRequest requestGet3 = HttpRequest.newBuilder()
                .uri(urlGet3)
                .GET()
                .build();
        HttpResponse<String> responseGet3 = client.send(requestGet3, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet3.statusCode());

        HttpRequest requestGet4 = HttpRequest.newBuilder()
                .uri(urlGet)
                .GET()
                .build();
        HttpResponse<String> responseGet4 = client.send(requestGet4, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseGet4.statusCode());

        URI urlHistory = URI.create("http://localhost:8080/history");
        HttpRequest requestHistory = HttpRequest.newBuilder()
                .uri(urlHistory)
                .GET()
                .build();
        HttpResponse<String> responseHistory = client.send(requestHistory, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, responseHistory.statusCode());

        Task[] tasks = gson.fromJson(responseHistory.body(), Task[].class);
        assertEquals(4, tasks.length, "Количество задач не совпадает");
    }
}