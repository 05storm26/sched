/**
 * (c) 2015 Maróti Ádám
 * 
 * */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class PreemptiveScheduler extends SchedulingAlgorithm {

	private Queue<sProcess> line = new LinkedBlockingQueue<sProcess>();
	public static final int sliceSize = 25;

	public Results Run(int runtime, Vector processVector, Results result) {
		int i = 0;
		int comptime = 0;
		int size = processVector.size();
		int completed = 0;

		String resultsFile = "Summary-Processes";

		result.schedulingType = "Round-Robin (Preemptive)";
		result.schedulingName = "First-Come First-Served with respect to max time slice.";
		try {
			// BufferedWriter out = new BufferedWriter(new
			// FileWriter(resultsFile));
			// OutputStream out = new FileOutputStream(resultsFile);
			PrintStream out = new PrintStream(new FileOutputStream(resultsFile));

			initQueue(processVector);

			sProcess process = getNextProcess();

			process.remainFromSlice = sliceSize;

			out.println("Process registered:     " + process);
			System.out.println("Process registered:     " + process + " init");
			while (comptime < runtime) {
				if (process.cpudone == process.cputime) {
					completed++;
					out.println("Process completed:      " + process);
					System.out.println("Process completed:      " + process);
					if (completed == size) {
						result.compuTime = comptime;
						out.close();
						return result;
					}
					process = getNextProcess();

					out.println("Process registered:     " + process);
					System.out.println("Process registered:     " + process
							+ " done");
				}

				if (process.remainFromSlice == 0) {
					out.println("Process interrupted (slice is over)...: "
							+ process);
					System.out
							.println("Process interrupted (slice is over)...: "
									+ process);

					process.remainFromSlice = PreemptiveScheduler.sliceSize;
					line.add(process);
					process = getNextProcess();

					out.println("Process registered:     " + process);
					System.out.println("Process registered:     " + process
							+ " sliceisover");

				}

				if (process.ioblocking == process.ionext) {
					out.println("Process I/O blocked...: " + process);
					System.out.println("Process I/O blocked...: " + process);
					process.numblocked++;
					process.ionext = 0;

					process.remainFromSlice = PreemptiveScheduler.sliceSize;
					line.add(process);

					process = getNextProcess();

					out.println("Process registered:     " + process);
					System.out.println("Process registered:     " + process
							+ " blocking");
				}
				process.cpudone++;
				process.remainFromSlice--;

				if (process.ioblocking > 0) {
					process.ionext++;
				}
				comptime++;
			}
			out.close();
		} catch (IOException e) { /* Handle exceptions */
		}
		result.compuTime = comptime;
		return result;
	}

	private void initQueue(Vector processVector) {
		for (int i = 0; i < processVector.size(); ++i) {
			sProcess p = (sProcess) processVector.elementAt(i);
			p.remainFromSlice = PreemptiveScheduler.sliceSize;
			line.add(p);
		}

	}

	private sProcess getNextProcess() {
		if (line.isEmpty())
			return null;
		sProcess first = null;
		for (sProcess p = line.remove();; line.add(p), p = line.remove()) {
			if (p == first)
				return null;

			if (p.cpudone < p.cputime)
				return p;

			if (first == null)
				first = p;
		}
	}

}
