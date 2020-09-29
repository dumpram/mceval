package com.github.dumpram.mceval.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.assignments.PriorityAssignmentCrMPO;
import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.evaluation.TestUtils;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCrtb;
import com.github.dumpram.mceval.rtimes.ResponseTimeClassic;
import com.github.dumpram.mceval.rtimes.ResponseTimeEfficientExact;
import com.github.dumpram.mceval.rtimes.ResponseTimeSMC;
import com.github.dumpram.mceval.rtimes.ResponseTimeSMCno;
import com.github.sh0nk.matplotlib4j.NumpyUtils;

public class Examples {

	//@Test
	public void findExamplePCvsMC() {
		// seed 1
		TestItem crmpa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeClassic()), 
				new PriorityAssignmentCrMPO());

		TestItem dmpa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeClassic()), 
				new PriorityAssignmentDM());
		
		List<TestItem> tests = new ArrayList<TestItem>();
		tests.add(crmpa);
		tests.add(dmpa);

		double minimumUtilization = 0.3;
		double maximumUtilization = 0.6;
		double utilizationIncrement = 0.1;
		int n = 3;
		int nsets = 100;
		int tmin = 2;
		int tmax = 12;
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
				if (!tests.get(0).testFeasibility(tests.get(0).priorityAssignment.assign(set)) && 
						tests.get(1).testFeasibility(tests.get(1).priorityAssignment.assign(set))) {
					System.out.println(set);
					
					for (TestItem test : tests) {
						MCTaskSet orderedSet = test.priorityAssignment.assign(set);
						HashMap<Integer, Integer> priorityOrder = test.priorityAssignment.getPriorityOrdering(set, orderedSet);
						for (int i = 0; i < n; i++) { 
							IResponseTime responseTime = ((FeasibilityTestResponseTime) test.feasibilityTest).getResponseTime();
							System.out.println(responseTime.printResponseTime(i, priorityOrder, orderedSet));
						}
					}
					
					return;
				}
			}
		}
	}

	@Test
	public void findExampleDMPASMCnovsOPASMCno() {
		TestItem dmpa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMCno()), 
				new PriorityAssignmentDM());

		TestItem opa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMCno()), 
				new PriorityAssignmentOPA(new ResponseTimeSMCno()));
		
		List<TestItem> tests = new ArrayList<TestItem>();
		tests.add(dmpa);
		tests.add(opa);

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 3;
		int nsets = 1000;
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
				if (!tests.get(0).testFeasibility(tests.get(0).priorityAssignment.assign(set)) && 
						tests.get(1).testFeasibility(tests.get(1).priorityAssignment.assign(set))) {
					System.out.println(set);
					
					for (TestItem test : tests) {
						MCTaskSet orderedSet = test.priorityAssignment.assign(set);
						HashMap<Integer, Integer> priorityOrder = test.priorityAssignment.getPriorityOrdering(set, orderedSet);
						for (int i = 0; i < n; i++) { 
							IResponseTime responseTime = ((FeasibilityTestResponseTime) test.feasibilityTest).getResponseTime();
							//System.out.println(responseTime.printResponseTime(i, priorityOrder, orderedSet));
						}
					}
					
					return;
				}
			}
		}
	}
	
	//@Test
	public void findExampleSMCnovsSMC() {
		TestItem smcno = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMCno()), 
				new PriorityAssignmentOPA(new ResponseTimeSMCno()));

		TestItem smc = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMC()), 
				new PriorityAssignmentOPA(new ResponseTimeSMC()));
		
		List<TestItem> tests = new ArrayList<TestItem>();
		tests.add(smcno);
		tests.add(smc);

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 3;
		int nsets = 1000;
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
				if (!tests.get(0).testFeasibility(tests.get(0).priorityAssignment.assign(set)) && 
						tests.get(1).testFeasibility(tests.get(1).priorityAssignment.assign(set))) {
					System.out.println(set);
					
					for (TestItem test : tests) {
						MCTaskSet orderedSet = test.priorityAssignment.assign(set);
						HashMap<Integer, Integer> priorityOrder = test.priorityAssignment.getPriorityOrdering(set, orderedSet);
						for (int i = 0; i < n; i++) { 
							IResponseTime responseTime = ((FeasibilityTestResponseTime) test.feasibilityTest).getResponseTime();
							System.out.println(responseTime.printResponseTime(i, priorityOrder, orderedSet));
						}
					}
					
					return;
				}
			}
		}
	}
	
	//@Test
	public void findExampleAMCrtbvsSMC() {
		TestItem amcrtb = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCrtb()), 
				new PriorityAssignmentOPA(new ResponseTimeAMCrtb()));

		TestItem smc = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMC()), 
				new PriorityAssignmentOPA(new ResponseTimeSMC()));
		
		List<TestItem> tests = new ArrayList<TestItem>();
		tests.add(amcrtb);
		tests.add(smc);

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 3;
		int nsets = 1000;
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
				if (!tests.get(0).testFeasibility(tests.get(0).priorityAssignment.assign(set)) && 
						tests.get(1).testFeasibility(tests.get(1).priorityAssignment.assign(set))) {
					System.out.println(set);
					
					for (TestItem test : tests) {
						MCTaskSet orderedSet = test.priorityAssignment.assign(set);
						HashMap<Integer, Integer> priorityOrder = test.priorityAssignment.getPriorityOrdering(set, orderedSet);
						for (int i = 0; i < n; i++) { 
							IResponseTime responseTime = ((FeasibilityTestResponseTime) test.feasibilityTest).getResponseTime();
							System.out.println(responseTime.printResponseTime(i, priorityOrder, orderedSet));
						}
					}
					
					return;
				}
			}
		}
	}
	
	//@Test
	public void findExampleAMCrtbvsAMCmax() {
		TestItem amcrtb = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCrtb()), 
				new PriorityAssignmentOPA(new ResponseTimeAMCrtb()));

		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), 
				new PriorityAssignmentOPA(new ResponseTimeAMCmax()));
		
		List<TestItem> tests = new ArrayList<TestItem>();
		tests.add(amcrtb);
		tests.add(amcmax);
		
		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 3;
		int nsets = 1000;
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
				if (!tests.get(0).testFeasibility(tests.get(0).priorityAssignment.assign(set)) && 
						tests.get(1).testFeasibility(tests.get(1).priorityAssignment.assign(set))) {
					System.out.println(set);
					
					for (TestItem test : tests) {
						MCTaskSet orderedSet = test.priorityAssignment.assign(set);
						HashMap<Integer, Integer> priorityOrder = test.priorityAssignment.getPriorityOrdering(set, orderedSet);
						for (int i = 0; i < n; i++) { 
							IResponseTime responseTime = ((FeasibilityTestResponseTime) test.feasibilityTest).getResponseTime();
							System.out.println(responseTime.printResponseTime(i, priorityOrder, orderedSet));
						}
					}
					
					return;
				}
			}
		}
	}
	
	//@Test
	public void findExampleExactvsAMCmax() {
		TestItem exact = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeEfficientExact()), 
				new PriorityAssignmentNOPA());

		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), 
				new PriorityAssignmentOPA(new ResponseTimeAMCmax()));
		
		List<TestItem> tests = new ArrayList<TestItem>();
		tests.add(amcmax);
		tests.add(exact);
		
		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 3;
		int nsets = 1000;
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
				if (!tests.get(0).testFeasibility(tests.get(0).priorityAssignment.assign(set)) && 
						tests.get(1).testFeasibility(tests.get(1).priorityAssignment.assign(set))) {
					System.out.println(set);
					
					for (TestItem test : tests) {
						MCTaskSet orderedSet = test.priorityAssignment.assign(set);
						HashMap<Integer, Integer> priorityOrder = test.priorityAssignment.getPriorityOrdering(set, orderedSet);
						for (int i = 0; i < n; i++) { 
							IResponseTime responseTime = ((FeasibilityTestResponseTime) test.feasibilityTest).getResponseTime();
							System.out.println(responseTime.printResponseTime(i, priorityOrder, orderedSet));
						}
					}
					
					return;
				}
			}
		}
	}
	
}
