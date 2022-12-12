import CPU.Process;

import java.util.Queue;

public class Printer {
    public static void print(Queue<Process> processes) {
        double avgWaiting = 0;
        double avgTurn = 0;

        int i = 1;
        for (Process p : processes) {
            avgWaiting += p.waitingTime;
            avgTurn += p.turnAroundTime;
            System.out.println(i + ". " + p.name + ":");
            System.out.println("Waiting Time: " + p.waitingTime);
            System.out.println("TurnAround Time: " + p.turnAroundTime);
            System.out.println();

            i += 1;
        }
        System.out.println("Average Waiting Time: " + avgWaiting / processes.size());
        System.out.println("Average TurnAround Time: " + avgTurn / processes.size());
    }
}
