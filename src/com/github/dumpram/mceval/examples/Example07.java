package com.github.dumpram.mceval.examples;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCrtb;

/**
 * Example illustrates how the dominance of AMC-max analysis over AMC-rtb analysis.
 * 
 * @author Ivan Pavic
 *
 */
public class Example07 {

	public static void main(String[] args) {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 3, 6 }, 18, new int[] { 18, 18 }, 1));
		tasks.add(new MCTask(new int[] { 1, 2 }, 4, new int[] { 4, 4 }, 1));
		tasks.add(new MCTask(new int[] { 1, 2 }, 3, new int[] { 3, 3 }, 0));

		MCTaskSet set = new MCTaskSet(tasks);

		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()),
				new PriorityAssignmentOPA(new ResponseTimeAMCmax()));
		TestItem amcrtb = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCrtb()),
				new PriorityAssignmentOPA(new ResponseTimeAMCrtb()));

		amcrtb.priorityAssignment.assign(set);
		System.out.println();
		amcmax.priorityAssignment.assign(set);
	}

}
