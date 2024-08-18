package http.handler;


import com.sun.net.httpserver.HttpExchange;
import exeptions.TaskOverlapException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.SubTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class SubTasksHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public SubTasksHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uuid = getIdFromPath(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (uuid == null) {
                    List<SubTask> subTasks = taskManager.getSubTasks();
                    String response = HttpTaskServer.getGson().toJson(subTasks);
                    sendText(exchange, response);
                } else {
                    SubTask subTask  = taskManager.getSubTask(uuid);
                    if (subTask == null) {
                        String response = "Not Found";
                        sendNotFound(exchange,response);
                    } else {
                        String response = HttpTaskServer.getGson().toJson(subTask);
                        sendText(exchange, response);
                    }
                }
                break;
            case "POST":
                try {
                    SubTask task1 = null;
                    try (InputStream inputStream = exchange.getRequestBody();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        String input = reader.lines().collect(Collectors.joining("\n"));
                        task1 = HttpTaskServer.getGson().fromJson(input, SubTask.class);

                    }

                    if (task1 == null) {
                        String response = "Bad request";
                        badRequest(exchange, response);
                        return;
                    }
                    SubTask subTask = new SubTask(task1.getName(),task1.getDescription(),task1.getTaskType(),task1.getDuration(),task1.getStartTime(),task1.getEndTime(),task1.getEpicUuid());
                    if (task1.getUuid() == null) {
                        String taskUuid = taskManager.createSubTask(subTask);
                        System.out.println("Created new task: " + taskUuid);
                        sendHasInteractions(exchange);
                    } else {
                        taskManager.updateSubTaskParameters(task1.getUuid(), subTask.getName(), subTask.getDescription(), subTask.getDuration(), subTask.getStartTime(), subTask.getEndTime(), subTask.getEpicUuid());
                        System.out.println("Updated subTask: " + subTask.getUuid());
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
                    SubTask subTask  = taskManager.getSubTask(uuid);
                    if (subTask == null) {
                        String response = "Not Found";
                        sendNotFound(exchange,response);
                    } else {
                        String response = HttpTaskServer.getGson().toJson(subTask);
                        taskManager.deleteSubTask(subTask.getUuid());
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
