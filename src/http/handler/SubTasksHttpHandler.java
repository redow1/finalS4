package http.handler;


import com.sun.net.httpserver.HttpExchange;
import exeptions.TaskOverlapException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.SubTask;
import tasks.Task;

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
                        sendNotFound(exchange,"Not Found");
                    } else {
                        String response = HttpTaskServer.getGson().toJson(subTask);
                        sendText(exchange, response);
                    }
                }
                break;
            case "POST":
                try {
                    SubTask subTaskForRead = null;
                    subTaskForRead = HttpTaskServer.getGson().fromJson(readText(exchange), SubTask.class);

                    if (subTaskForRead == null) {
                        badRequest(exchange, "Bad request");
                        return;
                    }
                    SubTask subTask = new SubTask(subTaskForRead.getName(),subTaskForRead.getDescription(),subTaskForRead.getTaskType(),subTaskForRead.getDuration(),subTaskForRead.getStartTime(),subTaskForRead.getEndTime(),subTaskForRead.getEpicUuid());
                    if (subTaskForRead.getUuid() == null) {
                        String taskUuid = taskManager.createSubTask(subTask);
                        System.out.println("Created new task: " + taskUuid);
                        positiveWithoutText(exchange);
                    } else {
                        taskManager.updateSubTaskParameters(subTaskForRead.getUuid(), subTask.getName(), subTask.getDescription(), subTask.getDuration(), subTask.getStartTime(), subTask.getEndTime(), subTask.getEpicUuid());
                        System.out.println("Updated subTask: " + subTask.getUuid());
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
                    String response = "Bad request";
                    badRequest(exchange, response);
                } else {
                    SubTask subTask  = taskManager.getSubTask(uuid);
                    if (subTask == null) {
                        sendNotFound(exchange,"Not Found");
                    } else {
                        String response = HttpTaskServer.getGson().toJson(subTask);
                        taskManager.deleteSubTask(subTask.getUuid());
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
