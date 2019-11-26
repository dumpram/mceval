package com.github.dumpram.mceval.evaluation;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExact;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class TestForRevision {
	
	public static void main(String[] args) {
		
		List<MCTask> tasks = new ArrayList<MCTask>();
		
		tasks.add(new MCTask(new int[] {1, 1},  4,  4, 0));
		tasks.add(new MCTask(new int[] {1, 2},  6,  6, 1));
		tasks.add(new MCTask(new int[] {2, 3},  8,  8, 1));
		
		MCTaskSet set = new MCTaskSet(tasks);
		
		FeasibilityTestEfficientExact test = new FeasibilityTestEfficientExact();
		
		System.out.println(test.isFeasible(set));
	}

}
