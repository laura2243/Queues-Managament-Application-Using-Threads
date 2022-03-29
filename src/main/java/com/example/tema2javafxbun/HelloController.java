package com.example.tema2javafxbun;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.concurrent.Task;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;

public class HelloController {
    public TextField numberOfQueues;
    public TextField numberOfClients;
    public TextField minArrivingTime;
    public TextField maxArrivingTime;
    public TextField minProcessingTime;
    public TextField maxProcessingTime;
    public MenuButton strategy;
    public TextField time;
    public Button start;
    public TextArea rezultat = new TextArea();
    public MenuItem shortestTime;
    public MenuItem shortestQueue;



    @FXML
    public int getNumberOfQueues() {
        return Integer.parseInt(numberOfQueues.getText());
    }

    @FXML
    public int getNumberOfClients() {
        return Integer.parseInt(numberOfClients.getText());
    }

    @FXML
    public int getMinArrivingTime() {
        return Integer.parseInt(minArrivingTime.getText());
    }

    @FXML
    public int getMaxArrivingTime() {
        return Integer.parseInt(maxArrivingTime.getText());
    }

    @FXML
    public int getMinProccesingTime() {
        return Integer.parseInt(minProcessingTime.getText());
    }

    @FXML
    public int getMaxProcessingTime() {
        return Integer.parseInt(maxProcessingTime.getText());
    }

    @FXML
    public int getTime() {
        return Integer.parseInt(time.getText());
    }

    int choice;

    @FXML
    protected void textBoxChoice1() {
        choice = 1;
        strategy.setText("Shortest Queue");
    }

    @FXML
    protected void textBoxChoice2() {
        choice = 2;
        strategy.setText("Shortest Time");
    }
    @FXML
    protected void textBoxChoice() {
        if(choice ==1){
            strategy.setText("Shortest Queue");
        }
        else
            strategy.setText("Shortest Time");
    }


    @FXML
    protected void startB() {
        SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
        if (choice == 1) {
            selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
        } else if (choice == 2) {
            selectionPolicy = SelectionPolicy.SHORTEST_TIME;
        }
        SimulationManager gen = new SimulationManager(getTime(),
                getNumberOfClients(),
                getNumberOfQueues(),
                getMinArrivingTime(),
                getMaxArrivingTime(),
                getMinProccesingTime(),
                getMaxProcessingTime(), selectionPolicy);
//
//        SimulationManager gen = new SimulationManager(10,
//                6,
//               3,
//               2,
//                10,
//                2,
//                12,SelectionPolicy.SHORTEST_QUEUE);


        // System.out.println(SimulationManager.getRezultat());

//        rezultat.setText(SimulationManager.getRezultat());

//
        // t1.start();



        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }

            @Override
            public void run() {
                final int max =getTime()+1;
                for (int i = 1; i <= max; i++) {
//                    String s="";
//                    if(SimulationManager.getRezultat().contains("TIME")){
//                   s = SimulationManager.getRezultat().substring(SimulationManager.getRezultat().indexOf("TIME"));
//
//                    rezultat.setText(s);}
//                    else {
//                        rezultat.setText(SimulationManager.getRezultat());
//                    }

                    String result = "";
                    try {
                        result = Files.readString(SimulationManager.fisier);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                 rezultTextArea.appendText(SimulationManager.textt+Server.textt);
                   rezultat.setText(result);
                   rezultat.setScrollTop(Double.MAX_VALUE);

                    rezultat.setScrollTop(Double.MAX_VALUE);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                float awt =  (float)(Server.getAverageWaitingTime().floatValue()/getNumberOfClients());
                rezultat.appendText("\n Peak time: [" + String.valueOf(SimulationManager.minTime) + ", " + SimulationManager.maxTime + "]");
                rezultat.appendText("\n Average waiting time: " + awt + "s");
                rezultat.appendText("\n Average service time: "+SimulationManager.averageServiceTime/getNumberOfClients() + "s");



                try {
                    Files.writeString(SimulationManager.fisier,"\n Peak time: [" + String.valueOf(SimulationManager.minTime) + ", " + SimulationManager.maxTime + "]", StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    Files.writeString(SimulationManager.fisier,"\n Average waiting time: " + awt + "s" , StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                try {
                    Files.writeString(SimulationManager.fisier,"\n Average service time: "+SimulationManager.averageServiceTime/getNumberOfClients() + "s" + SimulationManager.maxTime + "]", StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                return;
            }
        };

        new Thread(task).start();
        Thread t = new Thread(gen);
        t.start();
    }
}




