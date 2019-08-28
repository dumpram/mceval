package com.github.dumpram.mceval.evaluation;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.assignments.PriorityAssignmentDynamic;
import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExact;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.ftests.FeasibilityTestUBHL;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCTight;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class ValidityTests {

	@Test
	public void testDominanceWithRandomAssignmentImplicit() throws IOException, PythonExecutionException {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem ubhl = new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM());
		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), new PriorityAssignmentDynamic());
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentDynamic());
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentDynamic());
		tests.add(ubhl);
		tests.add(amcmax);
		tests.add(exact);
		tests.add(amctight);

		double minimumUtilization = 0.1;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.05;
		int n = 6;
		int nsets = 1000;
		int tmin = 10;
		int tmax = 100;
		int criticality = 2;
		int DC = 1;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed);
		
		assertTrue(amcmax.score() <= amctight.score() && amcmax.score() <= exact.score() && amcmax.score() <= ubhl.score());
		assertTrue(amctight.score() <= exact.score() && amctight.score() <= ubhl.score());
		assertTrue(exact.score() <= ubhl.score());
	}

	@Test
	public void testDominanceWithRandomAssignmentConstrained() {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem ubhl = new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM());
		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), new PriorityAssignmentDynamic());
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentDynamic());
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentDynamic());
		tests.add(ubhl);
		tests.add(amcmax);
		tests.add(exact);
		tests.add(amctight);

		double minimumUtilization = 0.1;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.05;
		int n = 6;
		int nsets = 1000;
		int tmin = 10;
		int tmax = 100;
		int criticality = 2;
		int DC = 2;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed);
		
		assertTrue(amcmax.score() <= amctight.score() && amcmax.score() <= exact.score() && amcmax.score() <= ubhl.score());
		assertTrue(amctight.score() <= exact.score() && amctight.score() <= ubhl.score());
		assertTrue(exact.score() <= ubhl.score());
	}

	@Test
	public void testDominanceWithTheBestAssignmentImplicit() {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem ubhl = new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM());
		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), new PriorityAssignmentOPA(new
				ResponseTimeAMCmax()));
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentNOPA());
		tests.add(ubhl);
		tests.add(amcmax);
		tests.add(exact);
		tests.add(amctight);

		double minimumUtilization = 0.1;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.05;
		int n = 6;
		int nsets = 1000;
		int tmin = 10;
		int tmax = 100;
		int criticality = 2;
		int DC = 1;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed);
		
		assertTrue(amcmax.score() <= amctight.score() && amcmax.score() <= exact.score() && amcmax.score() <= ubhl.score());
		assertTrue(amctight.score() <= exact.score() && amctight.score() <= ubhl.score());
		assertTrue(exact.score() <= ubhl.score());
	}

	@Test
	public void testDominanceWithTheBestAssignmentConstrained() {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem ubhl = new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM());
		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), new PriorityAssignmentOPA(new
				ResponseTimeAMCmax()));
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentNOPA());
		tests.add(ubhl);
		tests.add(amcmax);
		tests.add(exact);
		tests.add(amctight);

		double minimumUtilization = 0.1;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.05;
		int n = 6;
		int nsets = 1000;
		int tmin = 10;
		int tmax = 100;
		int criticality = 2;
		int DC = 2;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed);
		
		assertTrue(amcmax.score() <= amctight.score() && amcmax.score() <= exact.score() && amcmax.score() <= ubhl.score());
		assertTrue(amctight.score() <= exact.score() && amctight.score() <= ubhl.score());
		assertTrue(exact.score() <= ubhl.score());
	}
	
	@Test
	public void testDominanceWithTheBestAssignmentFloating() {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem ubhl = new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM());
		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), new PriorityAssignmentOPA(new
				ResponseTimeAMCmax()));
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentNOPA());
		tests.add(ubhl);
		tests.add(amcmax);
		tests.add(exact);
		tests.add(amctight);

		double minimumUtilization = 0.1;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.05;
		int n = 6;
		int nsets = 1000;
		int tmin = 10;
		int tmax = 100;
		int criticality = 2;
		int DC = 2;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = false;

		TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed);
		
		assertTrue(amcmax.score() <= amctight.score() && amcmax.score() <= exact.score() && amcmax.score() <= ubhl.score());
		assertTrue(amctight.score() <= exact.score() && amctight.score() <= ubhl.score());
		assertTrue(exact.score() <= ubhl.score());
	}
	
	@Test
	public void testAMCMaxWithNOPA() throws IOException, PythonExecutionException {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem amcmaxnopa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), new PriorityAssignmentNOPA());
		TestItem amcmaxopa = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());
		tests.add(amcmaxnopa);
		tests.add(amcmaxopa);


		double minimumUtilization = 0.1;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.05;
		int n = 6;
		int nsets = 1000;
		int tmin = 10;
		int tmax = 100;
		int criticality = 2;
		int DC = 2;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed);
		
		assertTrue(amcmaxnopa.score() <= amcmaxopa.score());
	}
	
	@Test
	public void testDominanceWithTheBestAssignmentFloatingForThreeTasks() {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem ubhl = new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM());
		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), new PriorityAssignmentOPA(new
				ResponseTimeAMCmax()));
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentNOPA());
		tests.add(ubhl);
		tests.add(amcmax);
		tests.add(exact);
		tests.add(amctight);

		double minimumUtilization = 0.1;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.05;
		int n = 3;
		int nsets = 1000;
		int tmin = 10;
		int tmax = 100;
		int criticality = 2;
		int DC = 2;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = false;

		TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed);
		
		assertTrue(amcmax.score() <= amctight.score() && amcmax.score() <= exact.score() && amcmax.score() <= ubhl.score());
		assertTrue(amctight.score() <= exact.score() && amctight.score() <= ubhl.score());
		assertTrue(exact.score() <= ubhl.score());
	}
	
	@Test
	public void testDominanceWithTheBestAssignmentFloatingForEightTasks() {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem ubhl = new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM());
		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), new PriorityAssignmentOPA(new
				ResponseTimeAMCmax()));
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentNOPA());
		tests.add(ubhl);
		tests.add(amcmax);
		tests.add(exact);
		tests.add(amctight);

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 8;
		int nsets = 100;
		int tmin = 10;
		int tmax = 100;
		int criticality = 2;
		int DC = 2;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = false;

		TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed);
		
		assertTrue(amcmax.score() <= amctight.score() && amcmax.score() <= exact.score() && amcmax.score() <= ubhl.score());
		assertTrue(amctight.score() <= exact.score() && amctight.score() <= ubhl.score());
		assertTrue(exact.score() <= ubhl.score());
	}



}
