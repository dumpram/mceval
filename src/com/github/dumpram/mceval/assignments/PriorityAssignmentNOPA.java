package com.github.dumpram.mceval.assignments;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.interfaces.PriorityAssignment;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeLO;

public class PriorityAssignmentNOPA extends PriorityAssignment {
	
	private ResponseTimeLO responseTimeLO = new ResponseTimeLO();
	
	@Override
	public MCTaskSet assign(MCTaskSet set) {
		List<MCTask> tasks = set.getTasks();
		int n = tasks.size();
		
		List<MCTask> assigned = new ArrayList<MCTask>(n);
		for (int i = 0; i < n; i++) {
			assigned.add(tasks.get(i));
		}
		
		MCTaskSet forExport = new MCTaskSet(assigned);
		
		int j = n - 1;
		while(someLoUnassigned(j + 1, forExport.getTasks()) && 
				someHiUnassigned(j + 1, forExport.getTasks())) {
			int i = getLargestDeadlineLO(forExport.getTasks(), j + 1);
			forExport.reorder(i, j);
			int R = responseTimeLO.responseTime(j, forExport);
			if (R > forExport.getTasks().get(j).getD()) {
				forExport.reorder(j, i);
				i = getLargestDeadlineHI(forExport.getTasks(), j + 1);
				forExport.reorder(i, j);
			}
			j--;
		}
		while(j >= 0) {
			int i = getLargestDeadline(forExport.getTasks(), j + 1);
			forExport.reorder(i, j);
			j--;
		}
		
		return forExport;
	}

	private int getLargestDeadline(List<MCTask> tasks, int j) {
		int max = 0;
		int idx = 0;
		
		for (int i = 0; i < j; i++) {
			if (tasks.get(i).getD() > max) {
				max = tasks.get(i).getD();
				idx = i;
			}
		}
		
		return idx;
	}

	private int getLargestDeadlineHI(List<MCTask> tasks, int j) {
		int max = 0;
		int idx = 0;
		
		for (int i = 0; i < j; i++) {
			if (tasks.get(i).getL() == 1 && tasks.get(i).getD() > max) {
				max = tasks.get(i).getD();
				idx = i;
			}
		}
		
		return idx;
	}

	private int getLargestDeadlineLO(List<MCTask> tasks, int j) {
		int max = 0;
		int idx = 0;
		
		for (int i = 0; i < j; i++) {
			if (tasks.get(i).getL() == 0 && tasks.get(i).getD() > max) {
				max = tasks.get(i).getD();
				idx = i;
			}
		}
		
		return idx;
	}

	private boolean someHiUnassigned(int j, List<MCTask> tasks) {
		for (int i = 0; i < j; i++) {
			if (tasks.get(i).getL() == 1) {
				return true;
			}
		}
		
		return false;
	}

	private boolean someLoUnassigned(int j, List<MCTask> tasks) {
		for (int i = 0; i < j; i++) {
			if (tasks.get(i).getL() == 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "NOPA";
	}

}
