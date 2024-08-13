package tasks.formatters;

import tasks.Task;
import tasks.TaskType;

public class CSVFormatter {
    private CSVFormatter() {

    }

    public static String toString(Task task) {
        if (task.getTaskType().equals(TaskType.SubTask)) {
            StringBuilder append1 = new StringBuilder()
                    .append(task.getUuid()).append(",")
                    .append(task.getTaskType()).append(",")
                    .append(task.getName()).append(",")
                    .append(task.getTaskStatus()).append(",")
                    .append(task.getDescription()).append(",")
                    .append(task.getDuration()).append(",")
                    .append(task.getStartTime()).append(",")
                    .append(task.getEndTime()).append(",")
                    .append(task.getEpicUuid());
                    return append1.toString();
        } else {
            StringBuilder append2 = new StringBuilder()
                    .append(task.getUuid()).append(",")
                    .append(task.getTaskType()).append(",")
                    .append(task.getName()).append(",")
                    .append(task.getTaskStatus()).append(",")
                    .append(task.getDescription()).append(",")
                    .append(task.getDuration()).append(",")
                    .append(task.getStartTime()).append(",")
                    .append(task.getEndTime()).append(",");
            return append2.toString();

        }

    }

    public static String[] fromString(String csvRow) {
        String[] split = csvRow.split(",");
        return split;
    }

    public static String getHeaders() {
        return "id,type,name,status,description, duration, startTime, endTime, epic";
    }
}
