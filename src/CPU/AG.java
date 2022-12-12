package CPU;

import java.util.Objects;
import java.util.Queue;
import java.util.Vector;

public class AG extends CPU {
    public AG(double contextSwitch, Vector<Process> Processes) {
        super(contextSwitch, Processes);
    }
    @Override
    public Queue<Process> execute() {
        while (!isFinished()) {
            // Check arrival process
            while (readyQueue.isEmpty() && inActionProcess == null) addTimer(1);

            // if no process is run set current process to be head of queue
            if (inActionProcess == null) {
                inActionProcess = readyQueue.poll();
            }

            // run current process 25% of its quantum time
            double actual_time = inActionProcess.run(Math.ceil(inActionProcess.getQuantumTime() / 4));
            addTimer(actual_time);
            if (inActionProcess.remainingTime == 0) {
                terminateActionProcess();
                if (isFinished()) return terminatedProcesses;
                continue;
            } else if (inActionProcess.getRemainingQuatum() == 0) {
                readyQueue.add(inActionProcess);
                inActionProcess = null;
                continue;
            }

            // check if current process has highest priority if yes continue if no set current process to be higest process then back to step 1
            Process pq = getHighestPriorityProcess();
            if (!Objects.equals(pq.name, inActionProcess.name)) {
                double new_quantum = inActionProcess.quantumTime + Math.ceil(inActionProcess.getRemainingQuatum() / 2);
                inActionProcess.setQuantumTime(new_quantum);
                inActionProcess = pq;
                continue;
            }

            // run current process 25% of its quantum time (correct formual is ceil(50% of it quantum time - 25% of its quantum time) to get value like 10 right)
            actual_time = Math.ceil(0.5 * inActionProcess.getQuantumTime()) - Math.ceil(0.25 * inActionProcess.getQuantumTime());
            inActionProcess.run(actual_time);
            addTimer(actual_time);
            if (inActionProcess.remainingTime == 0) {
                terminateActionProcess();
                if (isFinished()) return terminatedProcesses;
                continue;
            } else if (inActionProcess.getRemainingQuatum() == 0) {
                readyQueue.add(inActionProcess);
                inActionProcess = null;
                continue;
            }

            // check if current process has lowest burst time if yes continue if no set current process to be shortest job then back to step 1
            while (true) {
                Process temp = getLowestRemainingTimeProcess();
                if (Objects.equals(temp.name, inActionProcess.name)) {
                    inActionProcess.remainingTime -= 1;
                    addTimer(1);
                    if (inActionProcess.remainingTime == 0) {
                        terminateActionProcess();
                        if (isFinished()) return terminatedProcesses;
                        break;
                    }
                } else {
                    inActionProcess.setQuantumTime(inActionProcess.quantumTime + inActionProcess.getRemainingQuatum());
                    inActionProcess = temp;
                    break;
                }
            }
        }
        return terminatedProcesses;
    }
}
