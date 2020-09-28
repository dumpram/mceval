package com.github.dumpram.mceval.assignments;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.github.dumpram.mceval.interfaces.PriorityAssignment;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class PriorityAssignmentDM extends PriorityAssignment {
		
	private Comparator<MCTask> comp = new Comparator<MCTask>() {

		@Override
		public int compare(MCTask arg0, MCTask arg1) {
			return Integer.compare(arg0.getD(), arg1.getD());
		}
	};

	@Override
	public MCTaskSet assign(MCTaskSet set) {
		List<MCTask> tasks = set.getTasks();
		List<MCTask> tasksSorted = new ArrayList<MCTask>(tasks.size());
		for (MCTask t : tasks) {
			tasksSorted.add(t);
		}
 		tasksSorted.sort(comp);
 		
 		return new MCTaskSet(tasksSorted);
 		
	}
	
	@Override
	public String toString() {
		return "DMPA";
	}
}
