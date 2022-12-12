package CPU;

import java.util.Queue;
import java.util.Vector;

public class Priority extends CPU {
    public Priority(double contextSwitchTime, Vector<Process> processes) {
        super(contextSwitchTime, processes);
    }

    @Override
    public Queue<Process> execute() {
        while (!isFinished()) {
            while (inActionProcess == null && readyQueue.isEmpty()) addTimer(1);

            inActionProcess = getHighestPriorityProcess();
            if (inActionProcess == null) return terminatedProcesses;
            inActionProcess.remainingTime -= 1;
            addTimer(1);
            if (inActionProcess.remainingTime == 0)
                terminateActionProcess();
        }
        // Handle Starvation
        for (Process p : readyQueue) {
            if (p.priority > 1)
                p.priority -= 1;
        }
        return terminatedProcesses;
    }
}
