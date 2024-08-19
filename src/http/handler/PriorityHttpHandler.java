package http.handler;


import com.sun.net.httpserver.HttpExchange;
import http.HttpTaskServer;
import managers.TaskManager;

import java.io.IOException;


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
                System.out.println("/task получил: " + exchange.getRequestMethod());
                exchange.sendResponseHeaders(405, 0);
                exchange.close();
        }
    }
}
