package http;

import adapter.DurationTypeAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import http.handler.*;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private final HttpServer httpServer;
    private final TaskManager taskManager;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        this.httpServer = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.httpServer.createContext("/tasks", new TasksHttpHandler(taskManager));
        this.httpServer.createContext("/subtasks", new SubTasksHttpHandler(taskManager));
        this.httpServer.createContext("/epics", new EpicsHttpHandler(taskManager));
        this.httpServer.createContext("/history", new HistoryHttpHandler(taskManager));
        this.httpServer.createContext("/prioritized", new PriorityHttpHandler(taskManager));
    }

    public void start() {
        this.httpServer.start();
        System.out.println("Server started");
    }

    public void stop() {
        this.httpServer.stop(0);
        System.out.println("Server stopped");
    }

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault();
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();

    }

    public static Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationTypeAdapter());
        return gsonBuilder.create();
    }
}
