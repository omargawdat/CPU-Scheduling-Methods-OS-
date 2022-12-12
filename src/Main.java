import CPU.Process;
import CPU.*;

import java.util.Queue;
import java.util.Scanner;
import java.util.Vector;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Number of processes: ");
        int process_count = sc.nextInt();
        System.out.print("Context switching Time: ");
        int context_switching = sc.nextInt();

        Vector<Process> processes = new Vector<>();
        for (int i = 0; i < process_count; i++) {
            System.out.print("-- CPU.Process " + (i + 1) + " -- \n");
            System.out.print("Name: ");
            String name = sc.next();
            double burstTime = Reader.readNumber("Burst Time:", 0, 10000);
            double arrivalTime = Reader.readNumber("Arrival Time:", 0, 10000);
            double priority = Reader.readNumber("Priority Level:", 0, 127);
            double quantum = Reader.readNumber("Quantum Time:", 0, 1000000);

            processes.add(new Process(name, burstTime, arrivalTime, priority, quantum));
        }


        // ********Test Cases*********
//        double context_switching = 0;
//        Vector<Process> processes = testCases();
        // *************************


        int choice;
        do {
            System.out.println("\n ********** MENU *********");
            System.out.println("1- Shortest-Job First Scheduling");
            System.out.println("2- Round Robin Scheduling");
            System.out.println("3- Priority Scheduling");
            System.out.println("4- AG Scheduling");
            choice = (int) Reader.readNumber("||Choice||:", 0, 10000);
            System.out.println("Press any other Key for Exit...");

            switch (choice) {
                case 1 -> {
                    Vector<Process> test = getDeepCopy(processes); // todo
                    CPU srj = new SRJ(context_switching, test);
                    System.out.println("------ SRJ Scheduling -------");
                    Printer.print(srj.execute());
                    System.out.println("----------- SRJ END --------------");

                }
                case 2 -> {
                    double quantum_RR_time = Reader.readNumber("Round Robin Quantum Time: ", 0, 10000);
                    CPU rr = new RR(context_switching, getDeepCopy(processes), quantum_RR_time);
                    System.out.println("------ Round Robin Scheduling -------");
                    Printer.print(rr.execute());
                    System.out.println("----------- Round Robin END --------------");

                }
                case 3 -> {
                    CPU priority = new Priority(context_switching, getDeepCopy(processes));
                    System.out.println("------ Priority Scheduling -------");
                    Printer.print(priority.execute());
                    System.out.println("------ Priority END-------");
                }
                case 4 -> {
                    AG ag = new AG(context_switching, getDeepCopy(processes));
                    System.out.println("------ AG Scheduling -------");
                    Queue<Process> terminatedProcesses = ag.execute();
                    Printer.print(terminatedProcesses);

                    System.out.println("\n-|Processes Quantum History|- ");
                    int i = 1;
                    for (Process p : terminatedProcesses) {
                        System.out.print(i + ". " + p.name + ": ");
                        for (Double q : p.quantumHistory)
                            System.out.print(q + ", ");
                        System.out.println();
                        i += 1;
                    }
                    System.out.println("------ AG END -------");
                }
                default -> System.exit(0);
            }
        }
        while (true);
    }

    public static Vector<Process> testCases() {
        Vector<Process> processes = new Vector<>();
        processes.add(new Process("p1", 17, 0, 4, 7));
        processes.add(new Process("p2", 6, 2, 7, 9));
        processes.add(new Process("p3", 11, 5, 3, 4));
        processes.add(new Process("p4", 4, 15, 6, 6));

        return processes;
    }

    public static Vector<Process> getDeepCopy(Vector<Process> processes) {
        Vector<Process> newVector = new Vector<>();
        for (Process p : processes)
            newVector.add(new Process(p.name, p.burstTime, p.arrivalTime, p.priority, p.quantumTime));
        return newVector;
    }
}