package com.github.dumpram.mceval.evaluation;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExact;
import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExactWrong;
import com.github.dumpram.mceval.ftests.FeasibilityTestUBHL;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class TestForRevision {
	
	public static void main(String[] args) {
		
		List<MCTask> tasks = new ArrayList<MCTask>();
		
		tasks.add(new MCTask(new int[] {1, 1},  3,  3, 0));
		tasks.add(new MCTask(new int[] {1, 2},  5,  5, 1));
		tasks.add(new MCTask(new int[] {2, 4},  7,  7, 1));
		
		MCTaskSet set = new MCTaskSet(tasks);
		
		set = new PriorityAssignmentNOPA().assign(set);
		
		System.out.println(set);
		
		FeasibilityTestEfficientExact test = new FeasibilityTestEfficientExact();
		FeasibilityTestEfficientExactWrong wrongTest = new FeasibilityTestEfficientExactWrong();
		FeasibilityTestUBHL ubhl = new FeasibilityTestUBHL();
		
		System.out.println(test.isFeasible(set));
		System.out.println(wrongTest.isFeasible(set));
		System.out.println(ubhl.isFeasible(set));
	}

}
