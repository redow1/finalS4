package http.handler;


import com.sun.net.httpserver.HttpExchange;
import exeptions.TaskOverlapException;
import http.HttpTaskServer;
import managers.HistoryManager;
import managers.TaskManager;
import tasks.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class HistoryHttpHandler extends BaseHttpHandler {

    private TaskManager taskManager;
    private final HistoryManager historyManager;

    public HistoryHttpHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.historyManager = taskManager.getHistoryManager();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET":
                String response = HttpTaskServer.getGson().toJson(historyManager.getHistory());
                sendText(exchange, response);
            default:
                String response1 = "Internal Server Error";
                internalServerError(exchange, response1);
        }
    }
}
