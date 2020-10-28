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
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.dumpram.mceval.rtimes.ResponseTimePeriodic;
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
		TestItem exactperiodic = new TestItem(new FeasibilityTestResponseTime(new ResponseTimePeriodic()),
				new PriorityAssignmentNOPA());

//		tests.add(ubhl);
		tests.add(amcmax);
//		tests.add(exact);
		//tests.add(amctight);
		tests.add(exactperiodic);

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 20;
		int nsets = 100;
		int tmin = 1;
		int tmax = 10000;
		int criticality = 2;
		int DC = 2;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = false;

		Plot result = TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets, tmin, tmax, criticality,
				DC, CF, CP, delta, fixed, null);
		
//		assertTrue(amcmax.score() <= exact.score() && amcmax.score() <= ubhl.score());
////		assertTrue(amctight.score() <= exact.score() && amctight.score() <= ubhl.score());
//		assertTrue(exact.score() <= exactperiodic.score() && exact.score() <= ubhl.score());
//		assertTrue(exactperiodic.score() <= ubhl.score());
	
//		System.out.println(amctight.score() - amcmax.score());
//		System.out.println(exact.score() - amctight.score());
//		System.out.println(exact.score() - amcmax.score());
//		System.out.println(exactperiodic.score() - exact.score());
//		
//		for (int i = 0; i < exact.schedulableSets.size(); i++) {
//			if (!exactperiodic.schedulableSets.contains(exact.schedulableSets.get(i))) {
//				System.out.println(exact.schedulableSets.get(i));
//				break;
//			}
//		}
		
		for (int i = 0; i < tests.size(); i++) {
			System.out.println(tests.get(i).feasibilityTest + ": " + tests.get(i).averageTime());
		}
		
		result.show();
	}

}
