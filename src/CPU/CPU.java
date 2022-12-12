package CPU;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

public abstract class CPU {
    public double currentTimer;
    public double contextSwitchTime;
    public Process inActionProcess;
    public int processesCount;
    protected final Queue<Process> jobQueue = new LinkedList<>();
    protected Queue<Process> readyQueue;
    protected final Queue<Process> terminatedProcesses;

    public CPU(double contextSwitchTime, Vector<Process> processes) {
        Collections.sort(processes);
        jobQueue.addAll(processes);
        this.currentTimer = 0;
        this.contextSwitchTime = contextSwitchTime;
        processesCount = jobQueue.size();
        readyQueue = new LinkedList<Process>();
        terminatedProcesses = new LinkedList<Process>();
        inActionProcess = null;
        addTimer(0);
        while (!jobQueue.isEmpty() && readyQueue.isEmpty()) {
            addTimer(1);
        }
    }


    public abstract Queue<Process> execute();

    protected void sendProcessesFromJobIntoReady() {
        for (Process p : jobQueue) {
            if (p.arrivalTime <= currentTimer)
                readyQueue.add(p);
        }
        jobQueue.removeIf(i -> i.arrivalTime <= currentTimer);
    }

    protected Process getHighestPriorityProcess() {
        if (readyQueue.size() == 0 && inActionProcess == null) return null;
        if (readyQueue.size() == 0) return inActionProcess;
        Process minProcess = readyQueue.peek();
        for (Process p : readyQueue) {
            if (p.priority < minProcess.priority)
                minProcess = p;
        }
        if (inActionProcess == null) {
            readyQueue.remove(minProcess);
            return minProcess;
        }

        if (minProcess.priority < inActionProcess.priority) {
            // Swap
            readyQueue.add(inActionProcess);
            readyQueue.remove(minProcess);
            addTimer(contextSwitchTime);
            return minProcess;
        } else
            return inActionProcess;
    }

    protected Process getLowestRemainingTimeProcess() {
        if (readyQueue.size() == 0 && inActionProcess == null) return null;
        if (readyQueue.size() == 0) return inActionProcess;
        Process minProcess = readyQueue.peek();
        for (Process p : readyQueue) {
            if (p.remainingTime < minProcess.remainingTime)
                minProcess = p;
        }
        if (inActionProcess == null) {
            readyQueue.remove(minProcess);
            return minProcess;
        }

        if (minProcess.remainingTime < inActionProcess.remainingTime) {
            // Swap
            readyQueue.add(inActionProcess);
            readyQueue.remove(minProcess);
            addTimer(contextSwitchTime);
            return minProcess;
        } else
            return inActionProcess;
    }

    protected void terminateActionProcess() {
        inActionProcess.terminate(currentTimer);
        inActionProcess.setQuantumTime(0);
        terminatedProcesses.add(inActionProcess);
        inActionProcess = null;
    }

    protected boolean isFinished() {
        return (terminatedProcesses.size() == processesCount);
    }

    protected void addTimer(double time) {
        currentTimer += time;
        sendProcessesFromJobIntoReady();
    }
}