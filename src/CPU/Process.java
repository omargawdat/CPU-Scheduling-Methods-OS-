package CPU;

import java.util.LinkedList;
import java.util.Queue;

public class Process implements Comparable<Process> {
    public String name;
    public double arrivalTime;
    public double remainingTime;
    public double waitingTime;
    public double turnAroundTime;
    public double priority;
    public double quantumTime;

    private double remainingQuatum;
    public Queue<Double> quantumHistory;
    public double burstTime;

    public double getRemainingQuatum() {
        return remainingQuatum;
    }

    public Process(String name, double burstTime, double arrivalTime, double priority, double quantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.remainingTime = burstTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.quantumHistory = new LinkedList<>();
        this.setQuantumTime(quantum);
    }

    public void setQuantumTime(double quantumTime) {
        this.quantumTime = quantumTime;
        this.remainingQuatum = quantumTime;
        quantumHistory.add(quantumTime);
    }

    public double getQuantumTime() {
        return quantumTime;
    }

    public void terminate(double timer) {
        turnAroundTime = timer - arrivalTime;
        waitingTime = turnAroundTime - burstTime;
    }

    // AG
    public double run(double time) {
        double actualRunning = Math.min(Math.min(remainingQuatum, time), remainingTime);
        remainingQuatum -= actualRunning;
        remainingTime -= actualRunning;

        if (remainingQuatum == 0 && remainingTime > 0)
            quantumTime += 2;

        return actualRunning;
    }


    @Override // For Sorting Don't Use
    public int compareTo(Process p) {
        if (this.arrivalTime > p.arrivalTime)
            return 1;
        else
            return -1;
    }
}
