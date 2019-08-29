package com.github.dumpram.mceval.evaluation;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExact;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.ftests.FeasibilityTestUBHL;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCTight;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCTight2;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class PerformanceTests {
	
	@Test
	public void testDominanceWithTheBestAssignmentFloating() throws IOException, PythonExecutionException {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem ubhl = new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM());
		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()), new PriorityAssignmentOPA(new
				ResponseTimeAMCmax()));
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentNOPA());
		TestItem amctight2 = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight2()),
				new PriorityAssignmentNOPA());
		tests.add(ubhl);
		tests.add(amcmax);
		tests.add(exact);
		tests.add(amctight);
		tests.add(amctight2);

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 6;
		int nsets = 10000;
		int tmin = 1;
		int tmax = 50;
		int criticality = 2;
		int DC = 1;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		Plot result = TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed);
		
		assertTrue(amcmax.score() <= amctight.score() && amcmax.score() <= exact.score() && amcmax.score() <= ubhl.score());
		assertTrue(amctight.score() <= exact.score() && amctight.score() <= ubhl.score());
		assertTrue(exact.score() <= ubhl.score());
	
		System.out.println(amctight.score() - amcmax.score());
		System.out.println(exact.score() - amctight.score());
		System.out.println(exact.score() - amcmax.score());
		System.out.println(amctight2.score() - amctight.score());
		
		result.show();
	}

}
