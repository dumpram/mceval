package com.github.dumpram.mceval.evaluation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.ftests.FeasibilityTestEkbergGreedy;
import com.github.dumpram.mceval.interfaces.IFeasibilityTest;
import com.github.dumpram.mceval.models.MCTask;
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
					if (test.testFeasibility(test.priorityAssignment.assign(set))) {
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

	public static void generateSets(int n, int nsets, int tmin, int tmax, int criticality, int DC, int CF, double CP,
			double delta, boolean fixed, int hyperperiodlimit, List<List<MCTaskSet>> sets, List<Double> utils, List<MCTaskSet> blacklist,
			IFeasibilityTest filter) {
		for (double u : utils) {
			List<MCTaskSet> generated = new ArrayList<MCTaskSet>();
			while (generated.size() < nsets) {
				List<MCTaskSet> newOnes = UUniFastDiscard.generate(u, n, nsets - generated.size(), tmin, tmax,
						criticality, DC, CF, CP, delta, fixed, hyperperiodlimit, filter);
				for (MCTaskSet set : newOnes) {
					if (!generated.contains(set) && !blacklist.contains(set)) {
						generated.add(set);
					}
				}
				System.out.println("Generating task sets for u = " + u + ": "
						+ (int) ((1.0 * generated.size() / nsets) * 100) + " %");
			}
			sets.add(generated);
		}
	}
	
	public static void generateSets(int n, int nsets, int tmin, int tmax, int criticality, int DC, int CF, double CP,
			double delta, boolean fixed, List<List<MCTaskSet>> sets, List<Double> utils) {
		generateSets(n, nsets, tmin, tmax, criticality, DC, CF, CP, delta, fixed, Integer.MAX_VALUE, sets, utils, new ArrayList<MCTaskSet>(), null);
	}
	
	public static void saveToFile(int n, int nsets, int tmin, int tmax, int criticality, int DF, int CF, double CP,
			double delta, boolean fixed, int hyperperiodlimit, List<List<MCTaskSet>> sets, List<Double> utils, List<MCTaskSet> blacklist,
			IFeasibilityTest filter, String fileName) {
		
		String header = utils.get(0) + ", " + utils.get(utils.size() - 1) + ", " + n + ", " + nsets + ", " + tmin + ", " + 
		tmax + ", " + criticality + ", " + DF + ", " + CF + ", " + CP + ", " + delta + ", " + fixed + ", " + hyperperiodlimit + 
		", " + filter;
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(header + "\n");
			
			for (List<MCTaskSet> _sets : sets) {
				for (MCTaskSet set : _sets) {
					writer.write(set.export() + "\n");
				}
			}
			
			
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void loadFromFile(String fileName, List<List<MCTaskSet>> sets) {
		List<String> lines = new ArrayList<String>();
		
		try {
			lines = Files.readAllLines(Paths.get(fileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String header = lines.get(0);
		String[] params = header.split(", ");
		int n = Integer.parseInt(params[2]);
		int nsets = Integer.parseInt(params[3]);
		double umin = Double.parseDouble(params[0]);
		double umax = Double.parseDouble(params[1]);
		double delta = Double.parseDouble(params[10]);
		
		List<Double> utils = NumpyUtils.arange(umin, umax, delta);
		int nbins = utils.size();
		
		lines.remove(0); // skip header
		int linecnt = 0;

		
		for (int i = 0; i < nbins; i++) {
			List<MCTaskSet> _sets = new ArrayList<MCTaskSet>();
			for (int j = 0; j < nsets && linecnt < lines.size(); j++) {
				List<MCTask> tasks = new ArrayList<MCTask>();
				for (int k = 0; k < n; k++) {
					tasks.add(new MCTask(lines.get(linecnt)));
					linecnt++;
				}
				_sets.add(new MCTaskSet(tasks));
				linecnt++;
			}
			sets.add(_sets);
		}
		
		
	}
	
	public static void main(String[] args) {
		if (args.length < 6) {
			System.out.println("Too few input arguments. Provide at least: umin, umax, n, nsets, tmin, tmax, criticality");
		}
		double umin = 0.1, umax = 0.9, DF = 1.0, CF = 1.0, CP = 0.5, delta = 0.1;
		int n = 8, nsets = 10, tmin = 10, tmax = 100, criticality = 1, hyperperiodLimit = Integer.MAX_VALUE; 
		boolean fixed = true;
		IFeasibilityTest filter = null;
		
		umin = Double.parseDouble(args[0]);
		umax = Double.parseDouble(args[1]);
		n = Integer.parseInt(args[2]);
		nsets = Integer.parseInt(args[3]);
		tmin = Integer.parseInt(args[4]);
		tmax = Integer.parseInt(args[5]);
		criticality = Integer.parseInt(args[6]);
		if (args.length > 7) {
			DF = Double.parseDouble(args[7]);
		}
		if (args.length > 8) {
			CF = Double.parseDouble(args[8]);
		}
		if (args.length > 9) {
			CP = Double.parseDouble(args[9]);
		}
		if (args.length > 10) {
			delta = Double.parseDouble(args[10]);
		}
		if (args.length > 11) {
			fixed = Boolean.parseBoolean(args[11]);
		}
		if (args.length > 12) {
			hyperperiodLimit = Integer.parseInt(args[12]);
		}
		if (args.length > 13) {
			if (args[13] == "ekberg") {
				filter = new FeasibilityTestEkbergGreedy();
			}
		}
		
		System.out.println("Here");
		List<List<MCTaskSet>> sets = new ArrayList<List<MCTaskSet>>();
		List<Double> utils = NumpyUtils.arange(umin, umax, delta);
		List<MCTaskSet> blacklist = new ArrayList<MCTaskSet>();
		generateSets(n, nsets, tmin, tmax, criticality, (int)DF, (int)CF, CP, delta, fixed, hyperperiodLimit, sets, utils, blacklist, filter);
		saveToFile(n, nsets, tmin, tmax, criticality, (int)DF, (int)CF, CP, delta, fixed, hyperperiodLimit, sets, utils, blacklist, filter, "sets.txt");
		//List<List<MCTaskSet>> loadedSets = new ArrayList<List<MCTaskSet>>();
		//loadFromFile("sets.txt", loadedSets);
		//System.out.println(loadedSets);
	}
}
