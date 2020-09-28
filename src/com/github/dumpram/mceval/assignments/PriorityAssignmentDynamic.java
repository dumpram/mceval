package com.github.dumpram.mceval.assignments;

import com.github.dumpram.mceval.interfaces.PriorityAssignment;
import com.github.dumpram.mceval.models.MCTaskSet;

public class PriorityAssignmentDynamic extends PriorityAssignment {

	@Override
	public MCTaskSet assign(MCTaskSet set) {
		// does not change order of tasks in set offline
		return set;
	}

	@Override
	public String toString() {
		return "";
	}
}
