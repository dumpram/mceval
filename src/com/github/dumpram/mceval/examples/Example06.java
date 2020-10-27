package com.github.dumpram.mceval.examples;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCrtb;
import com.github.dumpram.mceval.rtimes.ResponseTimeSMC;

/**
 * Example illustrates how the dominance of AMC-rtb analysis over SMC analysis.
 * 
 * @author Ivan Pavic
 *
 */
public class Example06 {

	public static void main(String[] args) {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 3, 6 }, 12, new int[] { 12, 12 }, 1));
		tasks.add(new MCTask(new int[] { 1, 2 }, 8, new int[] { 8, 8 }, 1));
		tasks.add(new MCTask(new int[] { 1, 2 }, 4, new int[] { 4, 4 }, 0));

		MCTaskSet set = new MCTaskSet(tasks);

		TestItem smc = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMC()),
				new PriorityAssignmentOPA(new ResponseTimeSMC()));
		TestItem amcrtb = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCrtb()),
				new PriorityAssignmentOPA(new ResponseTimeAMCrtb()));

		smc.priorityAssignment.assign(set);
		System.out.println();
		amcrtb.priorityAssignment.assign(set);
	}

}
