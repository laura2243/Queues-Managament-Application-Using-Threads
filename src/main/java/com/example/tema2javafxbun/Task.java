package com.example.tema2javafxbun;

import java.util.Random;

public class Task {
    private int ID;
    private int arrivalTime;
    private int serviceTime;

    public Task(int servMin, int servMax, int arrMin, int arrMax)
    {
        Random random = new Random();
        arrivalTime = random.nextInt(arrMin,arrMax);
        serviceTime = random.nextInt(servMin,servMax);
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    @Override
    public String toString() {
        return
                 " Task " + ID +
                " arrivalTime = " + arrivalTime +
                " serviceTime = " + serviceTime ;
    }
}
