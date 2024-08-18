package http.handler;


import com.sun.net.httpserver.HttpExchange;
import exeptions.TaskOverlapException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class TasksHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public TasksHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uuid = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (uuid == null) {
                    List<Task> tasks = taskManager.getTasks();
                    String response = HttpTaskServer.getGson().toJson(tasks);
                    sendText(exchange, response);
                } else {
                    Task task  = taskManager.getTask(uuid);
                    if (task == null) {
                        String response = "Not Found";
                        sendNotFound(exchange,response);
                    } else {
                        String response = HttpTaskServer.getGson().toJson(task);
                        sendText(exchange, response);
                    }
                }
                break;
            case "POST":
                try {
                    Task task1 = null;
                    try (InputStream inputStream = exchange.getRequestBody();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        String input = reader.lines().collect(Collectors.joining("\n"));
                        task1 = HttpTaskServer.getGson().fromJson(input, Task.class);

                    }

                    if (task1 == null) {
                        String response = "Bad request";
                        badRequest(exchange, response);
                        return;
                    }
                    Task task = new Task(task1.getName(),task1.getDescription(),task1.getTaskType(),task1.getDuration(),task1.getStartTime(),task1.getEndTime());
                    if (task1.getUuid() == null) {
                        String taskUuid = taskManager.createTask(task);
                        System.out.println("Created new task: " + taskUuid);
                        sendHasInteractions(exchange);
                    } else {
                            taskManager.updateTaskParameters(task1.getUuid(), task.getName(), task.getDescription(), task.getDuration(), task.getStartTime(), task.getEndTime());
                            System.out.println("Updated task: " + task.getUuid());
                         sendHasInteractions(exchange);
                    }
                } catch (TaskOverlapException e) {
                    String response = "Task overlap: " + e.getMessage();
                    notAcceptable(exchange, response);
                } catch (Exception e) {
                    String response = "Internal Server Error";
                    internalServerError(exchange, response);
                }

                break;

            case "DELETE":
                if (uuid == null) {
                    String response = "Bad request";
                    badRequest(exchange, response);
                } else {
                    Task task  = taskManager.getTask(uuid);
                    if (task == null) {
                        String response = "Not Found";
                        sendNotFound(exchange,response);
                    } else {
                        String response = HttpTaskServer.getGson().toJson(task);
                        taskManager.deleteTask(task.getUuid());
                        sendText(exchange, "Deleted task " + response);
                    }
                }
                break;
            default:
                String response = "Internal Server Error";
                internalServerError(exchange, response);
        }
    }
}
