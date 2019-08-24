package com.github.dumpram.mceval.taskgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class UUniFastDiscard {

	private static Random rand = new Random();

	public static List<MCTaskSet> generate(double u, int n, int nsets, int tmin, int tmax, int criticality) {
		return generate(u, n, nsets, tmin, tmax, criticality, 1, 2.0, 0.5, 0.025, true);
	}

	public static List<MCTaskSet> generate(double u, int n, int nsets, int tmin, int tmax, int criticality, double DC,
			double CF, double CP, double delta, boolean fixed) {
		List<MCTaskSet> systems = new ArrayList<MCTaskSet>();
		List<List<Double>> u_sets = new ArrayList<List<Double>>();
		List<List<Integer>> p_sets = new ArrayList<List<Integer>>();
		List<List<List<Integer>>> c_sets = new ArrayList<List<List<Integer>>>();
		List<List<Integer>> cr_sets = new ArrayList<List<Integer>>();

		double sumU, nextSumU = 0;

		// Classical UUniFast algorithm
		while (u_sets.size() < nsets) {
			List<Double> utilizations = new ArrayList<Double>();
			sumU = u;
			for (int i = 1; i < n; i++) {
				nextSumU = sumU * Math.pow(rand.nextDouble(), 1.0 / (n - i));
				utilizations.add(sumU - nextSumU);
				sumU = nextSumU;
			}
			utilizations.add(nextSumU);

			// UUniFast-Discard
			boolean discard = false;

			for (int i = 0; i < utilizations.size(); i++) {
				if (utilizations.get(i) > 1) {
					discard = true;
				}
			}
			if (!discard) {
				u_sets.add(utilizations);
			}
		}

		// generate periods
		for (int i = 0; i < nsets; i++) {
			List<Integer> periods = new ArrayList<Integer>();
			for (int j = 0; j < n; j++) {
				int val = rand.nextInt(tmax - tmin) + tmin;
				if (!periods.contains(val)) {
					periods.add(val);
				} else {
					j--;
				}
			}
			p_sets.add(periods);
		}

		// generate criticality levels
		for (int i = 0; i < nsets; i++) {
			List<Integer> criticalities = new ArrayList<Integer>();
			for (int j = 0; j < n; j++) {
				criticalities.add((rand.nextDouble() < CP) ? 1 : 0);// rand.nextInt(criticality));
			}
			cr_sets.add(criticalities);
		}

		// calculate WCETs
		for (int i = 0; i < nsets; i++) {
			List<List<Integer>> l_wcets = new ArrayList<List<Integer>>(criticality);
			for (int j = 0; j < n; j++) {
				List<Integer> wcets = new ArrayList<Integer>();
				for (int k = 0; k < criticality; k++) {
					if (cr_sets.get(i).get(j) == 1) {
						wcets.add(
								(int) Math.ceil(p_sets.get(i).get(j) * (1.0 * u_sets.get(i).get(j)) * Math.pow(CF, k)));
					} else {
						wcets.add(
								(int) Math.ceil(p_sets.get(i).get(j) * (1.0 * u_sets.get(i).get(j)) * Math.pow(CF, 0)));
					}
				}
				l_wcets.add(wcets);
			}
			c_sets.add(l_wcets);
		}

		// create systems
		for (int i = 0; i < nsets; i++) {
			List<MCTask> tasks = new ArrayList<MCTask>();
			for (int j = 0; j < n; j++) {
				int offset = (!fixed) ? rand.nextInt(1 + p_sets.get(i).get(j) - (int) (p_sets.get(i).get(j) / DC)) : 0;
				MCTask task = new MCTask(c_sets.get(i).get(j), p_sets.get(i).get(j),
						(int) (p_sets.get(i).get(j) / DC) + offset, cr_sets.get(i).get(j));
				tasks.add(task);
			}
			MCTaskSet set = new MCTaskSet(tasks);
			double ulo = set.getUtilizationLO();

			if (ulo <= u + delta && ulo >= u - delta) {
				systems.add(new MCTaskSet(tasks));
			}
		}
		return systems;
	}

}
