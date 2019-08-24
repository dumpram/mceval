package com.github.dumpram.mceval.assignments;

import com.github.dumpram.mceval.interfaces.IPriorityAssignment;
import com.github.dumpram.mceval.models.MCTaskSet;

public class PriorityAssignmentDynamic implements IPriorityAssignment {

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
