package com.github.dumpram.mceval.interfaces;

import java.util.HashMap;

import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public abstract class PriorityAssignment {

	public abstract MCTaskSet assign(MCTaskSet set);
	
	public HashMap<Integer, Integer> getPriorityOrdering(MCTaskSet set, MCTaskSet orderedSet) {
		HashMap<Integer, Integer> priorityOrder = new HashMap<Integer, Integer>();
		int n = set.getTasks().size();
		for (int i = 0; i < n; i++) {
			MCTask task = orderedSet.getTasks().get(i);
			int taskIndex = set.getTasks().indexOf(task);
			priorityOrder.put(i, taskIndex);
		}
		return priorityOrder;
	}
	
}
