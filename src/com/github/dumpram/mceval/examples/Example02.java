package com.github.dumpram.mceval.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentCrMPO;
import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeClassic;

/**
 * Example illustrates shows the pitfalls of partitioned criticality in constrast to 
 * mixed-criticality.
 * 
 * @author Ivan Pavic
 *
 */
public class Example02 {

	public static void main(String[] args) {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 1, 2 }, 4, new int[] { 4, 4 }, 0));
		tasks.add(new MCTask(new int[] { 1, 2 }, 10, new int[] { 10, 10 }, 1));
		tasks.add(new MCTask(new int[] { 1, 2 }, 11, new int[] { 11, 11 }, 1));

		MCTaskSet set = new MCTaskSet(tasks);

		List<TestItem> tests = new ArrayList<>();
		
		TestItem crmpa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeClassic()), 
				new PriorityAssignmentCrMPO());

		TestItem dmpa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeClassic()), 
				new PriorityAssignmentDM());
		
		tests.add(crmpa);
		tests.add(dmpa);
		
		for (TestItem test : tests) {
			MCTaskSet orderedSet = test.priorityAssignment.assign(set);
			HashMap<Integer, Integer> priorityOrder = test.priorityAssignment.getPriorityOrdering(set, orderedSet);
			for (int i = 0; i < 3; i++) { 
				IResponseTime responseTime = ((FeasibilityTestResponseTime) test.feasibilityTest).getResponseTime();
				System.out.println(responseTime.printResponseTime(i, priorityOrder, orderedSet));
			}
		}
	}

}
