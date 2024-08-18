package http.handler;


import com.sun.net.httpserver.HttpExchange;
import exeptions.TaskOverlapException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Epic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class EpicsHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public EpicsHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String uuid = getIdFromPath(exchange.getRequestURI().getPath());
        Integer pathLength = getPathLength(exchange.getRequestURI().getPath());
        switch (exchange.getRequestMethod()) {
            case "GET":
                if (uuid == null) {
                    List<Epic> epics = taskManager.getEpics();
                    String response = HttpTaskServer.getGson().toJson(epics);
                    sendText(exchange, response);
                } else {
                    Epic epic = taskManager.getEpic(uuid);
                    if (pathLength == 3) {
                        if (epic == null) {
                            String response = "Not Found";
                            sendNotFound(exchange, response);
                        } else {
                            String response = HttpTaskServer.getGson().toJson(epic);
                            sendText(exchange, response);
                        }
                    } else if (pathLength == 4) {
                        if (epic == null) {
                            String response = "Not Found";
                            sendNotFound(exchange, response);
                        } else {
                            String response = HttpTaskServer.getGson().toJson(taskManager.getEpicSubtasks(uuid));
                            sendText(exchange, response);
                        }
                    }

                }
                break;
            case "POST":
                try {
                    Epic task1 = null;
                    try (InputStream inputStream = exchange.getRequestBody();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        String input = reader.lines().collect(Collectors.joining("\n"));
                        task1 = HttpTaskServer.getGson().fromJson(input, Epic.class);

                    }

                    if (task1 == null) {
                        String response = "Bad request";
                        badRequest(exchange, response);
                        return;
                    }
                    Epic epic = new Epic(task1.getName(), task1.getDescription(), task1.getTaskType(), task1.getDuration(), task1.getStartTime(), task1.getEndTime());
                    if (task1.getUuid() == null) {
                        String taskUuid = taskManager.createEpic(epic);
                        System.out.println("Created new task: " + taskUuid);
                        sendHasInteractions(exchange);
                    } else {
                        taskManager.updateEpicParameters(task1.getUuid(), epic.getName(), epic.getDescription(), epic.getDuration(), epic.getStartTime(), epic.getEndTime());
                        System.out.println("Updated task: " + epic.getUuid());
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
                    Epic epic = taskManager.getEpic(uuid);
                    if (epic == null) {
                        String response = "Not Found";
                        sendNotFound(exchange, response);
                    } else {
                        String response = HttpTaskServer.getGson().toJson(epic);
                        taskManager.deleteEpic(epic.getUuid());
                        sendText(exchange, "Deleted epic " + response);
                    }
                }
                break;
            default:
                String response = "Internal Server Error";
                internalServerError(exchange, response);
        }
    }
}
