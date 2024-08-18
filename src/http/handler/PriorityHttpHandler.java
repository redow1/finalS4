package http.handler;


import com.sun.net.httpserver.HttpExchange;
import exeptions.TaskOverlapException;
import http.HttpTaskServer;
import managers.TaskManager;
import tasks.Epic;
import tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class PriorityHttpHandler extends BaseHttpHandler {

    private final TaskManager taskManager;

    public PriorityHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                String response = HttpTaskServer.getGson().toJson(taskManager.getPrioritizedTasks());
                sendText(exchange, response);
                break;
            default:
                String response1 = "Internal Server Error";
                internalServerError(exchange, response1);
        }
    }
}
