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
import com.github.dumpram.mceval.ftests.FeasibilityTestUBHL;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCrtb;
import com.github.dumpram.mceval.rtimes.ResponseTimeFeasibilityTest;
import com.github.dumpram.mceval.taskgen.UUniFastDiscard;
import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class SchedulabilityTest {

	static List<TestPair> tests = new ArrayList<TestPair>();

	static {
		tests.add(new TestPair(new FeasibilityTestUBHL(), new PriorityAssignmentDM()));
		tests.add(new TestPair(new ResponseTimeFeasibilityTest(new ResponseTimeAMCmax()),
				new PriorityAssignmentOPA(new ResponseTimeAMCmax())));
		tests.add(new TestPair(new FeasibilityTestEkbergGreedy(), new PriorityAssignmentDynamic()));
		tests.add(new TestPair(new FeasibilityTestEDFWithVD(), new PriorityAssignmentDynamic()));
		tests.add(new TestPair(new ResponseTimeFeasibilityTest(new ResponseTimeAMCrtb()),
				new PriorityAssignmentOPA(new ResponseTimeAMCrtb())));
		tests.add(new TestPair(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA()));
	}

	public static void main(String[] args) throws IOException, PythonExecutionException {
		double minimumUtilization = 0.0;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.05;
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
		for (double u : utils) {
			List<MCTaskSet> generated = new ArrayList<MCTaskSet>();
			while (generated.size() < nsets) {
				List<MCTaskSet> newOnes = UUniFastDiscard.generate(u, n, nsets - generated.size(), tmin, tmax,
						criticality, DC, CF, CP, delta, fixed);
				for (MCTaskSet set : newOnes) {
					if (!generated.contains(set)) {
						generated.add(set);
					}
				}
			}
			sets.add(generated);
		}

		Plot plt = Plot.create();
		List<List<Double>> scores = new ArrayList<List<Double>>();
		for (TestPair test : tests) {
			List<Double> current = new ArrayList<Double>();
			for (List<MCTaskSet> _sets : sets) {
				double cnt = 0;
				for (MCTaskSet set : _sets) {
					if (test.feasibilityTest.isFeasible(test.priorityAssignment.assign(set))) {
						cnt++;
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
