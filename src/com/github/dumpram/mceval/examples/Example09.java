package com.github.dumpram.mceval.examples;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCTight;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;

/**
 * Example illustrates how the dominance of the analysis devised in the thesis
 * over AMC-max analysis.
 * 
 * @author Ivan Pavic
 *
 */
public class Example09 {

	public static void main(String[] args) {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 1, 2 }, 10, new int[] { 10, 10 }, 1));
		tasks.add(new MCTask(new int[] { 1, 1 }, 5, new int[] { 5, 5 }, 0));
		tasks.add(new MCTask(new int[] { 4, 8 }, 13, new int[] { 13, 13 }, 1));

		MCTaskSet set = new MCTaskSet(tasks);

		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()),
				new PriorityAssignmentOPA(new ResponseTimeAMCmax()));
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentNOPA());

		amcmax.priorityAssignment.assign(set);
		MCTaskSet orderedSet = amctight.priorityAssignment.assign(set);
		for (int i = 0; i < 3; i++) {
			IResponseTime test = ((FeasibilityTestResponseTime) amctight.feasibilityTest).getResponseTime();
			System.out.println(test.responseTime(i, orderedSet));
		}

	}

}
