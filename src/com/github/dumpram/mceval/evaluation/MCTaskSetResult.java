package com.github.dumpram.mceval.evaluation;

import java.util.List;

import com.github.dumpram.mceval.models.MCTaskSet;

public class MCTaskSetResult {

    MCTaskSet set;
    
    public List<Integer> responseTimes;
	
    public MCTaskSetResult(MCTaskSet set, List<Integer> responseTimes) {
		this.set = set;
		this.responseTimes = responseTimes;
	}
    
	public boolean isFeasible() {
		for (int i = 0; i < set.getTasks().size(); i++) {
			if (responseTimes.get(i) > set.getTasks().get(i).getD()) {
				return false;
			}
		}
		return true;
	}
	
}
