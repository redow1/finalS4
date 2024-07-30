package managers;

import tasks.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    Node tail = null;
    Node head = null;

    public HashMap<Task, Node> nodeMap = new HashMap<>();

    @Override
    public void add(Task task) {

        if (task.equals(nodeMap.get(task))) {
            remove(task);
        }

        Node node = new Node();
        if (nodeMap.size() > 1) {
            nodeMap.get(tail.value).next = node;
            Node temp = tail;
            tail = node;
            node.prev = temp;
        }
        if (head != null && tail != null && nodeMap.size() == 1) {
            node.prev = head;
            node.next = null;
            tail = node;
            head.next = node;
        }

        if (head == null) {
            head = node;
            head.prev = null;
        }
        if (tail == null) {
            tail = node;
            tail.next = null;

        }
        node.value = task;
        nodeMap.put(task, node);
    }


    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(nodeMap.keySet());
        return tasks;

    }

    @Override
    public void remove(Task task) {
        Node node1 = nodeMap.remove(task);
        if (node1 == null) {
            return;
        }
        removeNode(node1);

    }


    private void removeNode(Node node) {
        if (node.prev == null && node.next == null) {
            node.value = null;
        } else if (node.prev == null) {
            node.next.prev = null;
            head = node.next;
        } else if (node.next == null) {
            node.prev.next = null;
            tail = node.prev;
        } else {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
    }


    private static class Node {
        Node prev;
        Node next;
        Task value;

        public Node(Node prev, Node next, Task value) {
        }

        public Node() {
        }

        public Node(Task value) {
            this.value = value;
        }
    }
}
