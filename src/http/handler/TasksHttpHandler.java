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
                        sendNotFound(exchange,"Not Found");
                    } else {
                        String response = HttpTaskServer.getGson().toJson(task);
                        sendText(exchange, response);
                    }
                }
                break;
            case "POST":
                try {
                    Task taskForRead = null;
                    taskForRead = HttpTaskServer.getGson().fromJson(readText(exchange), Task.class);

                    if (taskForRead == null) {
                        badRequest(exchange, "Bad request");
                        return;
                    }
                    Task task = new Task(taskForRead.getName(),taskForRead.getDescription(),taskForRead.getTaskType(),taskForRead.getDuration(),taskForRead.getStartTime(),taskForRead.getEndTime());
                    if (taskForRead.getUuid() == null) {
                        String taskUuid = taskManager.createTask(task);
                        System.out.println("Created new task: " + taskUuid);
                        positiveWithoutText(exchange);
                    } else {
                        taskManager.updateTaskParameters(taskForRead.getUuid(), task.getName(), task.getDescription(), task.getDuration(), task.getStartTime(), task.getEndTime());
                        System.out.println("Updated task: " + task.getUuid());
                        positiveWithoutText(exchange);
                    }
                } catch (TaskOverlapException e) {
                    sendHasInteractions(exchange, "Task overlap: " + e.getMessage());
                } catch (Exception e) {
                    internalServerError(exchange, "Internal Server Error");
                }

                break;

            case "DELETE":
                if (uuid == null) {
                    badRequest(exchange, "Bad request");
                } else {
                    Task task  = taskManager.getTask(uuid);
                    if (task == null) {
                        sendNotFound(exchange,"Not Found");
                    } else {
                        String response = HttpTaskServer.getGson().toJson(task);
                        taskManager.deleteTask(task.getUuid());
                        sendText(exchange, "Deleted task " + response);
                    }
                }
                break;
            default:
                System.out.println("/task получил: " + exchange.getRequestMethod());
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
        }
    }
}
