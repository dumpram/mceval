package com.github.dumpram.mceval.examples;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeSMCno;

/**
 * Example illustrates how that the deadline monotonic assignment is not optimal.
 * 
 * @author Ivan Pavic
 *
 */
public class Example0304 {

	public static void main(String[] args) {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 2, 4 }, 8, new int[] { 8, 8 }, 1));
		tasks.add(new MCTask(new int[] { 1, 2 }, 14, new int[] { 14, 14 }, 1));
		tasks.add(new MCTask(new int[] { 2, 4 }, 9, new int[] { 9, 9 }, 0));

		MCTaskSet set = new MCTaskSet(tasks);

		TestItem smcnodmpa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMCno()),
				new PriorityAssignmentDM());
		TestItem smcnoopa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMCno()),
				new PriorityAssignmentOPA(new ResponseTimeSMCno()));

		smcnodmpa.priorityAssignment.assign(set);
		System.out.println();
		smcnoopa.priorityAssignment.assign(set);
	}

}
