package com.github.dumpram.mceval.evaluation;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExact;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.rtimes.ResponseTimeEfficientExact;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class ExactTests {

	@Test
	public void testEquivalence() throws IOException, PythonExecutionException {
		List<TestItem> tests = new ArrayList<TestItem>();

		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());
		TestItem exact2 = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeEfficientExact()),
				new PriorityAssignmentNOPA());

		tests.add(exact);
		tests.add(exact2);

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 6;
		int nsets = 10000;
		int tmin = 1;
		int tmax = 60;
		int criticality = 2;
		int DC = 1;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		Plot result = TestUtils.runTest(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets,
				tmin, tmax, criticality, DC, CF, CP, delta, fixed);


		assertTrue(exact.score() == exact.score());

		result.show();
	}

}
