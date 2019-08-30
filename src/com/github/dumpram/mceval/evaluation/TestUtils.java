package com.github.dumpram.mceval.evaluation;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.taskgen.UUniFastDiscard;
import com.github.sh0nk.matplotlib4j.NumpyUtils;
import com.github.sh0nk.matplotlib4j.Plot;

public class TestUtils {

	public static Plot runTest(List<TestItem> tests, double minimumUtilization, double maximumUtilization,
			double utilizationIncrement, int n, int nsets, int tmin, int tmax, int criticality, int DC, int CF,
			double CP, double delta, boolean fixed) {
		List<Double> utils = NumpyUtils.arange(minimumUtilization, maximumUtilization, utilizationIncrement);
		List<List<MCTaskSet>> sets = new ArrayList<List<MCTaskSet>>();
		
		generateSets(n, nsets, tmin, tmax, criticality, DC, CF, CP, delta, fixed, sets, utils);

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
					System.out.println("Testing " + test.feasibilityTest + " for u = " + utils.get(sets.indexOf(_sets))
							+ ": " + (int) ((1.0 * _sets.indexOf(set) / _sets.size()) * 100) + " %");
				}
				current.add(cnt);
			}
			scores.add(current);
			plt.plot().add(utils, current).label(test.toString());
		}

		plt.legend();

		return plt;
	}
	
	public static void runTestResponseTime(List<TestItem> tests, double minimumUtilization, double maximumUtilization,
			double utilizationIncrement, int n, int nsets, int tmin, int tmax, int criticality, int DC, int CF,
			double CP, double delta, boolean fixed) {

		List<List<MCTaskSet>> sets = new ArrayList<List<MCTaskSet>>();
		List<Double> utils = NumpyUtils.arange(minimumUtilization, maximumUtilization, utilizationIncrement);
		
		generateSets(n, nsets, tmin, tmax, criticality, DC, CF, CP, delta, fixed, sets, utils);

		for (TestItem test : tests) {
			for (List<MCTaskSet> _sets : sets) {;
				for (MCTaskSet set : _sets) {
					List<Integer> currentTimes = new ArrayList<Integer>();
					for (int i = 0; i < set.getTasks().size(); i++) {
						currentTimes.add(test.responseTime.responseTime(i, set));
					}
					test.setResults.add(new MCTaskSetResult(set, currentTimes));
					System.out.println("Testing " + test.responseTime + " for u = " + utils.get(sets.indexOf(_sets))
							+ ": " + (int) ((1.0 * _sets.indexOf(set) / _sets.size()) * 100) + " %");
				}
			}
		}
	}

	private static void generateSets(int n, int nsets, int tmin, int tmax, int criticality, int DC, int CF, double CP,
			double delta, boolean fixed, List<List<MCTaskSet>> sets, List<Double> utils) {
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
				System.out.println("Generating task sets for u = " + u + ": "
						+ (int) ((1.0 * generated.size() / nsets) * 100) + " %");
			}
			sets.add(generated);
		}
	}
}
