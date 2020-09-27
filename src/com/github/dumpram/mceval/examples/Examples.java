package com.github.dumpram.mceval.examples;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.assignments.PriorityAssignmentCrMPO;
import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.evaluation.TestUtils;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeClassic;
import com.github.sh0nk.matplotlib4j.NumpyUtils;

public class Examples {

	@Test
	public void findExamples() {
		List<TestItem> tests = new ArrayList<TestItem>();

		tests.add(new TestItem(new FeasibilityTestResponseTime(new ResponseTimeClassic()), 
				new PriorityAssignmentCrMPO()));

		tests.add(new TestItem(new FeasibilityTestResponseTime(new ResponseTimeClassic()), 
				new PriorityAssignmentDM()));

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 3;
		int nsets = 10000;
		int tmin = 2;
		int tmax = 15;
		int criticality = 2;
		int DC = 1;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		List<Double> utils = NumpyUtils.arange(minimumUtilization, maximumUtilization, utilizationIncrement);
		List<List<MCTaskSet>> sets = new ArrayList<List<MCTaskSet>>();
		TestUtils.generateSets(n, nsets, tmin, tmax, criticality, DC, CF, CP, delta, fixed, 20000, sets, utils,
				new ArrayList<MCTaskSet>(), null);
		
		for (List<MCTaskSet> _sets : sets) {
			for (MCTaskSet set : _sets) {
				if (!tests.get(1).testFeasibility(tests.get(1).priorityAssignment.assign(set)) && 
						tests.get(0).testFeasibility(tests.get(0).priorityAssignment.assign(set))) {
					System.out.println(set);
				}
			}
		}
	}
}
