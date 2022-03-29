package com.example.tema2javafxbun;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) throws InterruptedException {
        int min = Integer.MAX_VALUE;
        Server s = new Server();

        for (Server server:servers) {
            if (server.getTasks().size() < min) {
                min = server.getTasks().size();
                s = server;

            }
        }
        int i = servers.indexOf(s);
        servers.get(i).addTask(t);
    }
}
