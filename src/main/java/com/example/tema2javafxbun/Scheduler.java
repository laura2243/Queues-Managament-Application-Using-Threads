package com.example.tema2javafxbun;

import java.util.ArrayList;
import java.util.List;

enum SelectionPolicy {
    SHORTEST_QUEUE, SHORTEST_TIME
}

public class Scheduler {
    private int maxNrServers;
    private int maxTasksPerServer;
    private List<Server> servers = new ArrayList<>();
    private Strategy strategy;
    private List<Thread> threads = new ArrayList<>();

    public Scheduler(int maxNrServers, int maxTasksPerServer) {
        for (int i = 0; i < maxNrServers; i++) {
            Server server = new Server();
            servers.add(server);
            Thread thread = new Thread(server, "server" + (i + 1));
            threads.add(thread);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy selectionPolicy) {
        if (selectionPolicy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        if (selectionPolicy == SelectionPolicy.SHORTEST_TIME)
            strategy = new ConcreteStrategyTime();
    }

    public void dispatchTask(Task t) {

        try {
            strategy.addTask(servers, t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<Server> getServers() {
        return servers;
    }

    public List<Thread> getThreads() {
        return threads;
    }
}
