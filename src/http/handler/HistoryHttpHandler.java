package http.handler;


import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import managers.HistoryManager;
import managers.TaskManager;

import java.io.IOException;


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
                System.out.println("/task получил: " + exchange.getRequestMethod());
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
        }
    }
}
