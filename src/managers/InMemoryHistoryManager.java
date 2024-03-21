package managers;

import entities.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private HashMap<Integer, Node> innerMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void add(Task task) {
        Integer id = task.getUid();
        if (innerMap.containsKey(id)) {
            removeNode(innerMap.get(id));
            innerMap.remove(id);
        }
        innerMap.put(id, linkLast(task));
    }

    @Override
    public ArrayList<Task> getHistory() {
        ArrayList<Task> history = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            history.add(currentNode.data);
            currentNode = currentNode.next;
        }
        return history;
    }

    @Override
    public void remove(int id) {
        if (innerMap.containsKey(id)) {
            removeNode(innerMap.get(id));
            innerMap.remove(id);
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
