package com.github.dumpram.mceval.examples;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExact;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;

/**
 * Example illustrates how the dominance of the exact analysis over AMC-max analysis.
 * 
 * @author Ivan Pavic
 *
 */
public class Example08 {

	public static void main(String[] args) {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 1, 2 }, 5, new int[] { 5, 5 }, 1));
		tasks.add(new MCTask(new int[] { 1, 2 }, 2, new int[] { 2, 2 }, 0));
		tasks.add(new MCTask(new int[] { 1, 2 }, 7, new int[] { 7, 7 }, 1));

		MCTaskSet set = new MCTaskSet(tasks);

		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()),
				new PriorityAssignmentOPA(new ResponseTimeAMCmax()));
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());

		amcmax.priorityAssignment.assign(set);
		System.out.println();
		exact.priorityAssignment.assign(set);
	}

}
