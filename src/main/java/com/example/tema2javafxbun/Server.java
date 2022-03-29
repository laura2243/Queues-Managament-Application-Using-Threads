package com.example.tema2javafxbun;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private static AtomicInteger averageWaitingTime = new AtomicInteger(0);
    public static int peak=0;

    public static AtomicInteger getAverageWaitingTime() {
        return averageWaitingTime;
    }

    public Server() {
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);

    }

    public void addTask(Task task) {
        try {
            tasks.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        waitingPeriod.getAndAdd(task.getServiceTime());
        averageWaitingTime.getAndAdd(waitingPeriod.intValue());
    }

    @Override
    public void run() {
        Task t;

        while (true) {
            if (tasks.size() != 0) {

                t = tasks.peek();

                System.out.println(Thread.currentThread().getName() + " " + tasks);

                SimulationManager.rezultat += Thread.currentThread().getName() + " : ";
                try {
                    Files.writeString(SimulationManager.fisier,(Thread.currentThread().getName() + " : " + tasks + "\n"), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SimulationManager.rezultat += tasks;
                SimulationManager.rezultat += "\n";
//                for (Task task : tasks) {
//                    SimulationManager.rezultat += task.toString();
//
//
////                    try {
////                        Files.writeString(SimulationManager.fisier, (task + " "), StandardOpenOption.APPEND);
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
//
//                    if (tasks.size() == 1) {
//                        SimulationManager.rezultat += "  ";
//                    } else {
//                        SimulationManager.rezultat += ", ";
//                    }
//
//                }

                try {
                    Files.writeString(SimulationManager.fisier, "\n", StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                SimulationManager.rezultat += "\n";

                waitingPeriod.decrementAndGet();

                t.setServiceTime(t.getServiceTime() - 1);

                if (t.getServiceTime() == 0) {
                    tasks.remove(t);
                }


            } else {
                System.out.println(Thread.currentThread().getName() + " is closed. ");
                SimulationManager.mess.add(Thread.currentThread().getName() + " is closed. ");
                try {
                    Files.writeString(SimulationManager.fisier,(Thread.currentThread().getName() + " is closed." + "\n"), StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                SimulationManager.rezultat += Thread.currentThread().getName() + " is closed." + "\n";
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    @Override
    public String toString() {

        String s = "\n";
        for (Task task : tasks) {
            s += task.toString();
        }
        s += "\n";
        return s;

    }
}
