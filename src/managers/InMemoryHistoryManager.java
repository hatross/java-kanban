package managers;

import entities.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> innerMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        int id = task.getUid();
        if (!innerMap.containsKey(id)) {
            innerMap.put(id, linkLast(task));
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        for (Integer id : innerMap.keySet()) {
            Task nodeData = innerMap.get(id).data;
            history.add(nodeData);
        }
        return history;
    }

    @Override
    public void remove(int id) {
        if (innerMap.size() > 1) {
            removeNode(innerMap.get(id));
            innerMap.remove(id);
        } else {
            innerMap.clear();
        }
    }

    @Override
    public void clear() {
        innerMap.clear();
        head = null;
        tail = null;
    }

    private Node linkLast(Task task) {
        final Node t = tail;
        final Node newNode = new Node(t, task, null);
        tail = newNode;
        if (t == null) {
            head = newNode;
        } else {
            t.next = newNode;
        }
        return newNode;
    }

    private void removeNode(Node node) {
        Node prevNode = node.prev;
        Node nextNode = node.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        } else {
            head = nextNode;
        }

        if (nextNode != null) {
            nextNode.prev = prevNode;
        } else {
            tail = prevNode;
        }
    }

    private class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
}
