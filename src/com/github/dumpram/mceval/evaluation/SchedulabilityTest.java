package com.github.dumpram.mceval.evaluation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.assignments.PriorityAssignmentDynamic;
import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.ftests.FeasibilityTestEDFWithVD;
import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExact;
import com.github.dumpram.mceval.ftests.FeasibilityTestEkbergGreedy;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.ftests.FeasibilityTestUBHL;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCTight;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCrtb;
import com.github.dumpram.mceval.rtimes.ResponseTimePeriodic;
import com.github.dumpram.mceval.taskgen.UUniFastDiscard;
import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class SchedulabilityTest {

	static List<TestItem> tests = new ArrayList<TestItem>();

	static {
		tests.add(new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM()));
		tests.add(new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()),
				new PriorityAssignmentOPA(new ResponseTimeAMCmax())));
		tests.add(new TestItem(new FeasibilityTestEkbergGreedy(), new PriorityAssignmentDynamic()));
		tests.add(new TestItem(new FeasibilityTestEDFWithVD(), new PriorityAssignmentDynamic()));
		tests.add(new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCrtb()),
				new PriorityAssignmentOPA(new ResponseTimeAMCrtb())));
		// tests.add(new TestItem(new FeasibilityTestEfficientExact(), new
		// PriorityAssignmentNOPA()));
		// tests.add(new TestItem(new FeasibilityTestResponseTime(new
		// ResponseTimeAMCTight()), new PriorityAssignmentNOPA()));
		// tests.add(new TestItem(new FeasibilityTestResponseTime(new
		// ResponseTimePeriodic()), new PriorityAssignmentNOPA()));
	}

	public static void main(String[] args) throws IOException, PythonExecutionException {
		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 6;
		int nsets = 100;
		int tmin = 10;
		int tmax = 100;
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

		Plot plt = Plot.create();
		List<List<Double>> scores = new ArrayList<List<Double>>();
		for (TestItem test : tests) {
			List<Double> current = new ArrayList<Double>();
			for (List<MCTaskSet> _sets : sets) {
				double cnt = 0;
				for (MCTaskSet set : _sets) {
					if (test.feasibilityTest.isFeasible(test.priorityAssignment.assign(set))) {
						cnt++;
						test.schedulableSets.add(set);
					}
				}
				current.add(cnt);
			}
			scores.add(current);
			plt.plot().add(utils, current).label(test.toString());
		}

		plt.legend();
		plt.show();

	}

}
