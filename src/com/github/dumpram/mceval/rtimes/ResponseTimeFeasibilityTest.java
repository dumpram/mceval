package com.github.dumpram.mceval.rtimes;

import com.github.dumpram.mceval.interfaces.IFeasibilityTest;
import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTaskSet;

public class ResponseTimeFeasibilityTest implements IFeasibilityTest {

	private IResponseTime responseTime;

	public ResponseTimeFeasibilityTest(IResponseTime responseTime) {
		this.responseTime = responseTime;
	}

	@Override
	public boolean isFeasible(MCTaskSet set) {
		if (set == null) {
			return false;
		}
		
		int n = set.getTasks().size();
		boolean feasible = true;

		for (int i = 0; i < n; i++) {
			boolean taskFeasible = responseTime.responseTime(i, set) <= set.getTasks().get(i).getD();
			if (!taskFeasible) {
				feasible = false;
				break;
			}
		}

		return feasible;

	}
	
	@Override
	public String toString() {
		return responseTime.toString();
	}

}
