package com.github.dumpram.mceval.ftests;

import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.interfaces.IFeasibilityTest;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeHI;
import com.github.dumpram.mceval.rtimes.ResponseTimeLO;

public class FeasibilityTestUBHL implements IFeasibilityTest {
	
	private ResponseTimeHI responseTimeHI = new ResponseTimeHI();
	
	private ResponseTimeLO responseTimeLO = new ResponseTimeLO();
	
	private PriorityAssignmentDM assignment = new PriorityAssignmentDM();
	
	public boolean isFeasible(MCTaskSet set) {
		MCTaskSet assigned = assignment.assign(set);
		return isFeasibleLO(assigned) && isFeasibleHI(assigned);
	}

	private boolean isFeasibleHI(MCTaskSet set) {
		int n = set.getTasks().size();
		int[] R = new int[n];
		
		for (int i = 0; i < n; i++) {
			if (set.getTasks().get(i).getL() == 0) {
				continue;
			}
			R[i] = responseTimeHI.responseTime(i, set);
			if (R[i] > set.getTasks().get(i).getD()) {
				return false;
			}
		}
		return true;
	}

	private boolean isFeasibleLO(MCTaskSet set) {
		int n = set.getTasks().size();
		int[] R = new int[n];
		
		for (int i = 0; i < n; i++) {
			R[i] = responseTimeLO.responseTime(i, set);
			if (R[i] > set.getTasks().get(i).getD()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return "UBH&L";
	}
	
}
