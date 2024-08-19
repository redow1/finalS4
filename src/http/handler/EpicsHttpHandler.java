package http.handler;


import com.sun.net.httpserver.HttpExchange;
import exeptions.TaskOverlapException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.util.List;


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
                            sendNotFound(exchange, "Not Found");
                        } else {
                            String response = HttpTaskServer.getGson().toJson(epic);
                            sendText(exchange, response);
                        }
                    } else if (pathLength == 4) {
                        if (epic == null) {
                            sendNotFound(exchange, "Not Found");
                        } else {
                            String response = HttpTaskServer.getGson().toJson(taskManager.getEpicSubtasks(uuid));
                            sendText(exchange, response);
                        }
                    }

                }
                break;
            case "POST":
                try {
                    Epic epicForRead = null;
                    epicForRead = HttpTaskServer.getGson().fromJson(readText(exchange), Epic.class);

                    if (epicForRead == null) {
                        badRequest(exchange, "Bad request");
                        return;
                    }
                    Epic epic = new Epic(epicForRead.getName(), epicForRead.getDescription(), epicForRead.getTaskType(), epicForRead.getDuration(), epicForRead.getStartTime(), epicForRead.getEndTime());
                    if (epicForRead.getUuid() == null) {
                        String taskUuid = taskManager.createEpic(epic);
                        System.out.println("Created new task: " + taskUuid);
                        positiveWithoutText(exchange);
                    } else {
                        taskManager.updateEpicParameters(epicForRead.getUuid(), epic.getName(), epic.getDescription(), epic.getDuration(), epic.getStartTime(), epic.getEndTime());
                        System.out.println("Updated task: " + epic.getUuid());
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
                    Epic epic = taskManager.getEpic(uuid);
                    if (epic == null) {
                        sendNotFound(exchange, "Not Found");
                    } else {
                        String response = HttpTaskServer.getGson().toJson(epic);
                        taskManager.deleteEpic(epic.getUuid());
                        sendText(exchange, "Deleted epic " + response);
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
