package CPU;

import java.util.Queue;
import java.util.Vector;

public class RR extends CPU {
    double quantumTime;

    public RR(double contextSwitchTime, Vector<Process> processes, double quantumTime) {
        super(contextSwitchTime, processes);
        this.quantumTime = quantumTime;
    }

    @Override
    public Queue<Process> execute() {
        while (!isFinished()) {
            while (inActionProcess == null && readyQueue.isEmpty()) addTimer(1);

            inActionProcess = readyQueue.poll();
            double x = Math.min(inActionProcess.remainingTime, quantumTime);
            inActionProcess.remainingTime -= x;
            addTimer(x);

            if (inActionProcess.remainingTime == 0)
                terminateActionProcess();
            else
                readyQueue.add(inActionProcess);
        }
        return terminatedProcesses;
    }
}
