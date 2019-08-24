package com.github.dumpram.mceval.interfaces;

import com.github.dumpram.mceval.models.MCTaskSet;

public interface IPriorityAssignment {

	MCTaskSet assign(MCTaskSet set);
	
}
