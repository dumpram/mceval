package com.github.dumpram.mceval.evaluation;

import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.rtimes.ResponseTimeAMCTight;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.dumpram.mceval.rtimes.ResponseTimePeriodic;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class ResponseTimeTests {

	@Test
	public void testResponseTime() throws IOException, PythonExecutionException {
		List<TestItem> tests = new ArrayList<TestItem>();

		TestItem amctight = new TestItem(new ResponseTimeAMCTight());
		TestItem amcmax = new TestItem(new ResponseTimeAMCmax());
		TestItem exactrt = new TestItem(new ResponseTimePeriodic());

		tests.add(amctight);
		tests.add(amcmax);
		tests.add(exactrt);

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 4;
		int nsets = 1000;
		int tmin = 1;
		int tmax = 20;
		int criticality = 2;
		int DC = 1;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		TestUtils.runTestResponseTime(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets,
				tmin, tmax, criticality, DC, CF, CP, delta, fixed);

		
		int f, s = 0, t = 0;
		for (int i = 0; i < tests.get(0).setResults.size(); i++) {
			for (int j = 0; j < n; j++) {
				MCTaskSetResult first = tests.get(0).setResults.get(i);
				MCTaskSetResult second = tests.get(1).setResults.get(i);
				MCTaskSetResult third = tests.get(2).setResults.get(i);

				if (first.isFeasible() && !second.isFeasible()) {
					t++;
					System.out.println(first.set);
					System.out.println(first.responseTimes);
					System.out.println(second.responseTimes);
					return;
				}
				
				if (third.isFeasible() && !first.isFeasible()) {
					s++;
				}
				assertFalse( "Ne smije se dogoditi.", !first.isFeasible() && second.isFeasible());
				
				
				
				if (first.isFeasible() && second.isFeasible()) {
					assertTrue(first.responseTimes.get(j) <= second.responseTimes.get(j), 
					"i = " + i + " " + first.responseTimes + " " + second.responseTimes + 
					" set: " + first.set);
					assertTrue(first.responseTimes.get(j) >= third.responseTimes.get(j),
							"i = " + i + " " + first.responseTimes + " " + third.responseTimes + 
							" set: " + first.set);
			
				}
			}
		}
		
		System.out.println(t);
		System.out.println(s);
	}
}
