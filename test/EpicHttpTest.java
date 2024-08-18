package tests;

import com.google.gson.Gson;
import http.HttpTaskServer;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.TaskType;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class EpicHttpTest {
    private static TaskManager taskManager;
    private static HttpTaskServer httpTaskServer;

    Gson gson = HttpTaskServer.getGson();

    @BeforeEach
    void startServer() throws IOException {
        taskManager = Managers.getDefault();
        httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }

    @AfterEach
    void stopServer() throws IOException {
        httpTaskServer.stop();
    }

    @Test
    void getEpics() throws IOException, InterruptedException {
        Epic task = new Epic("task1", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now(), null);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());

        Epic[] tasks = gson.fromJson(response1.body(), Epic[].class);
        assertEquals(1, tasks.length, "Количество задач не совпадает");
        assertEquals(task.getName(), tasks[0].getName(), "Имя задачи не совпадает");
        assertEquals(task.getDescription(), tasks[0].getDescription(), "Описание задачи не совпадает");
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        Epic task = new Epic("task3", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now(), null);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Задача не была успешно создана");

        String uuid = taskManager.getEpics().get(0).getUuid();

        URI url2 = URI.create("http://localhost:8080/epics/" + uuid);
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response2.statusCode());

        Epic task2 = gson.fromJson(response2.body(), Epic.class);
        assertEquals(task.getName(), task2.getName(), "Имя задачи не совпадает");
        assertEquals(task.getDescription(), task2.getDescription(), "Описание задачи не совпадает");
    }

    @Test
    void getTaskNegative() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String uuid = "123";

        URI url2 = URI.create("http://localhost:8080/epics/" + uuid);

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response2.statusCode());
    }


    @Test
    void createTask() throws IOException, InterruptedException {
        Epic task = new Epic("task1", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now(), null);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode());

        List<Epic> tasksFromManager = taskManager.getEpics();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("task1", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void createTaskOverlapExeption() throws IOException, InterruptedException {
        Epic task = new Epic("task1", "часов 8", TaskType.Task, Duration.ofMinutes(15), LocalDateTime.now().plusDays(3), null);
        Epic task1 = new Epic("task1", "часов 8", TaskType.Task, Duration.ofMinutes(9), LocalDateTime.now().plusDays(3), null);
        String taskJson = gson.toJson(task);
        String taskJson1 = gson.toJson(task1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
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
        assertEquals(406, response1.statusCode());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Epic task = new Epic("task2", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now(), null);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        task.setName("task1AfterUpdate");
        String taskJsonAfterUpdate = gson.toJson(task);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJsonAfterUpdate))
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response1.statusCode());

        List<Epic> tasksFromManager = taskManager.getEpics();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("task1AfterUpdate", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        Epic task = new Epic("task3", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now(), null);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode(), "Задача не была успешно создана");

        String uuid = taskManager.getEpics().get(0).getUuid();

        URI url2 = URI.create("http://localhost:8080/epics/" + uuid);
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url2)
                .DELETE()
                .build();
        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response1.statusCode());

        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(url2)
                .GET()
                .build();
        HttpResponse<String> response2 = client.send(request2, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response2.statusCode());

        List<Epic> tasksFromManager = taskManager.getEpics();
        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void deleteTaskWithoutTaskID() throws IOException, InterruptedException {
        Epic task = new Epic("task3", "часов 8", TaskType.Task, Duration.ofSeconds(1), LocalDateTime.now(), null);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response1 = client.send(request1, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response1.statusCode());
    }
}