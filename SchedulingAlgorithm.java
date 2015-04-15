// Run() is called from Scheduling.main() and is where
// the scheduling algorithm written by the user resides.
// User modification should occur within the Run() function.

import java.util.Vector;

public abstract class SchedulingAlgorithm {

	public abstract Results Run(int runtime, Vector processVector, Results result) ;

}
