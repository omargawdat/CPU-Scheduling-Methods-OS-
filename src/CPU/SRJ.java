package CPU;

import java.util.Queue;
import java.util.Vector;

public class SRJ extends CPU {
    public SRJ(double contextSwitchTime, Vector<Process> processes) {
        super(contextSwitchTime, processes);
    }

    @Override
    public Queue<Process> execute() {
        while (!isFinished()) {
            while (inActionProcess == null && readyQueue.isEmpty()) addTimer(1);

            inActionProcess = getLowestRemainingTimeProcess();
            if (inActionProcess == null) return terminatedProcesses;
            inActionProcess.remainingTime -= 1;
            addTimer(1);
            if (inActionProcess.remainingTime == 0)
                terminateActionProcess();
        }
        return terminatedProcesses;
    }
}