package com.example.tema2javafxbun;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class SimulationManager implements Runnable {
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int minArrivingTime;
    public int maxArrivingTime;
    public int numberOfServers;
    public int numberOfClients;
    public SelectionPolicy selectionPolicy;

    static float averageServiceTime = 0;
    public static Path fisier = Path.of("D:/TP/tema2JavaFXbun/test3.txt");
    public static LinkedList<String> mess = new LinkedList<>();


    public SimulationManager(int timeLimit, int numberOfClients, int numberOfServers, int minArrivingTime, int maxArrivingTime, int minProcessingTime, int maxProcessingTime, SelectionPolicy selectionPolicy) {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.minArrivingTime = minArrivingTime;
        this.maxArrivingTime = maxArrivingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.selectionPolicy = selectionPolicy;
        scheduler = new Scheduler(numberOfServers, numberOfClients);
        scheduler.changeStrategy(selectionPolicy);
        //GUI
        generateNRandomTasks();
    }


    public static String rezultat = "";

    private Scheduler scheduler;
    //GUI
    private List<Task> generatedTasks = new ArrayList<>();


    private void generateNRandomTasks() {

        for (int i = 0; i < numberOfClients; i++) {
            Task task = new Task(minProcessingTime, maxProcessingTime, minArrivingTime, maxArrivingTime);
            task.setID(i + 1);
            generatedTasks.add(task);
        }
        generatedTasks.sort(new Sort());

        for (Task task : generatedTasks) {
            averageServiceTime += task.getServiceTime();
        }
    }


    static int minTime = 0;
    static int maxTime = 0;

    @Override
    public void run() {
        int currentTime = 0;
        int i = 0;
        int ok = 0;
        int okPeak = 0;
        int peak = 0;
        int size = 0;

        while (currentTime < timeLimit) {
            System.out.println("\n TIME :" + currentTime);
            mess.add("\n TIME :" + currentTime);

            try {
                Files.writeString(SimulationManager.fisier, ("\n TIME : " + currentTime + "\n"), StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }

            rezultat += "\n TIME :" + currentTime + "\n";
            i = 0;
            while (i < generatedTasks.size()) {
                if (generatedTasks.get(i).getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(generatedTasks.get(i));
                    generatedTasks.remove(generatedTasks.get(i));
                    i--;
                    ///GUI
                }
                i++;
            }


            if (generatedTasks.size() == 0) {
                int nr = 0;

                for (Server s : scheduler.getServers()) {

                    if (s.getWaitingPeriod().intValue() != 0) {
                        System.out.println("There are no clients in the waiting queue. \n");
                        mess.add("There are no clients in the waiting queue. \n");

                        try {
                            Files.writeString(SimulationManager.fisier, ("There are no clients in the waiting queue. \n"), StandardOpenOption.APPEND);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        rezultat += "There are no clients in the waiting queue. \n";
                        break;
                    } else {
                        nr++;
                        if (nr == scheduler.getServers().size()) {
                            System.out.println("DONE! There are no more clients.");

                            mess.add("DONE! There are no more clients.");

                            try {
                                Files.writeString(SimulationManager.fisier, ("DONE! There are no more clients."), StandardOpenOption.APPEND);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            rezultat += "DONE! There are no more clients.";
                            for (Thread t : scheduler.getThreads()) {
                                t.stop();
                            }
                            ok = 1;
                            break;

                        }
                    }
                }

                if (ok == 1) {
                    return;
                }

            } else {
                System.out.println(generatedTasks);


                rezultat += "Generated tasks: \n";
                for (Task task : generatedTasks) {

                    try {
                        Files.writeString(SimulationManager.fisier, task.toString() + '\n', StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    rezultat += task.toString();
                    rezultat += "\n";
                    mess.add(task.toString());
                    mess.add("\n");
                }


            }

            size = 0;
            for (Server server : scheduler.getServers()) {
                size += server.getTasks().size();


            }
            System.out.println("\n !!!!SIZE!!! \n" + size);
            if (size > peak) {
                peak = size;
                minTime = currentTime;
                maxTime = currentTime;


            } else if (maxTime == currentTime - 1) {
                if (size == peak) {
                    maxTime++;
                }
            }


            currentTime++;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }


        }

        for (Thread t : scheduler.getThreads()) {
            t.stop();
        }


        // System.out.println(scheduler.getServers());
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public void setMaxProcessingTime(int maxProcessingTime) {
        this.maxProcessingTime = maxProcessingTime;
    }

    public void setMinProcessingTime(int minProcessingTime) {
        this.minProcessingTime = minProcessingTime;
    }

    public void setMinArrivingTime(int minArrivingTime) {
        this.minArrivingTime = minArrivingTime;
    }

    public void setMaxArrivingTime(int maxArrivingTime) {
        this.maxArrivingTime = maxArrivingTime;
    }

    public void setNumberOfServers(int numberOfServers) {
        this.numberOfServers = numberOfServers;
    }

    public void setNumberOfClients(int numberOfClients) {
        this.numberOfClients = numberOfClients;
    }

    public void setSelectionPolicy(SelectionPolicy selectionPolicy) {
        this.selectionPolicy = selectionPolicy;
    }

    public static String getRezultat() {
        return rezultat;
    }

    public static void main(String[] args) {
        // SimulationManager gen = new SimulationManager();
        // Thread t = new Thread(gen,"");
        // t.start();
    }
}
