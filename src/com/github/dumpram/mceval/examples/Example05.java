package com.github.dumpram.mceval.examples;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeSMC;
import com.github.dumpram.mceval.rtimes.ResponseTimeSMCno;

/**
 * Example illustrates how the dominance of SMC analysis over SMC-NO analysis.
 * 
 * @author Ivan Pavic
 *
 */
public class Example05 {

	public static void main(String[] args) {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 2, 4 }, 13, new int[] { 13, 13 }, 1));
		tasks.add(new MCTask(new int[] { 1, 2 }, 4, new int[] { 4, 4 }, 0));
		tasks.add(new MCTask(new int[] { 2, 4 }, 14, new int[] { 14, 14 }, 1));

		MCTaskSet set = new MCTaskSet(tasks);

		TestItem smc = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMC()),
				new PriorityAssignmentOPA(new ResponseTimeSMC()));
		TestItem smcno = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMCno()),
				new PriorityAssignmentOPA(new ResponseTimeSMCno()));

		smcno.priorityAssignment.assign(set);
		System.out.println();
		smc.priorityAssignment.assign(set);
	}

}
