package com.example.tema2javafxbun;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        int min = Integer.MAX_VALUE;
        Server s = new Server();

        for (Server server: servers) {
            if (server.getWaitingPeriod().get() < min) {
                min = server.getWaitingPeriod().get();
                s = server;
            }
        }
        int i = servers.indexOf(s);
        servers.get(i).addTask(t);
    }
}
