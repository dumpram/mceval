package com.github.dumpram.mceval.assignments;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.interfaces.PriorityAssignment;
import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class PriorityAssignmentOPA extends PriorityAssignment {
	
	private IResponseTime responseTime;
	
	public PriorityAssignmentOPA(IResponseTime responseTime) {
		this.responseTime = responseTime;
	}
	
	private boolean testOPA(int i, int j, MCTaskSet set, IResponseTime responseTime) {
		int R = responseTime.responseTime(j, set);
		return R <= set.getTasks().get(j).getD();
	}

	@Override
	public MCTaskSet assign(MCTaskSet set) {
		int n = set.getTasks().size();
		boolean unassigned = true;
			
		List<MCTask> assigned = new ArrayList<MCTask>(n);
		for (int i = 0; i < n; i++) {
			assigned.add(set.getTasks().get(i));
		}
		
		MCTaskSet forExport = new MCTaskSet(assigned);

		for (int j = n - 1; j >= 0; j--) {
			unassigned = true;
			//System.out.println(n - j  + ". step (j = " + (j + 1) + "):");
			for (int i = 0; i < j + 1; i++) {
				forExport.reorder(i, j);
				//System.out.println("i = " + i + " :" + responseTime.printResponseTime(j, getPriorityOrdering(set, forExport), forExport));
				if (testOPA(i, j, forExport, responseTime) && unassigned) {
					unassigned = false;
					break;
				} else {
					forExport.reorder(j, i);
				}
			}
			if (unassigned) {
				return null;
			}
		}
		
		
		return forExport;
	}
	
	@Override
	public String toString() {
		return "OPA";
	}

}
