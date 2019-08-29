package com.github.dumpram.mceval.evaluation;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.dumpram.mceval.rtimes.ResponseTimeEfficientExact;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class ResponseTimeTests {
	
	@Test
	public void testResponseTime() throws IOException, PythonExecutionException {
		List<TestItem> tests = new ArrayList<TestItem>();

		TestItem amctight = new TestItem(new ResponseTimeAMCmax());
		TestItem exactrt = new TestItem(new ResponseTimeEfficientExact());

		tests.add(amctight);
		tests.add(exactrt);

		double minimumUtilization = 0.5;
		double maximumUtilization = 0.9;
		double utilizationIncrement = 0.1;
		int n = 4;
		int nsets = 10000;
		int tmin = 1;
		int tmax = 60;
		int criticality = 2;
		int DC = 1;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = true;

		TestUtils.runTestResponseTime(tests, minimumUtilization, maximumUtilization, utilizationIncrement, n, nsets,
				tmin, tmax, criticality, DC, CF, CP, delta, fixed);


		for (int i = 0; i < tests.get(0).responseTimes.size(); i++) {
			for (int j = 0; j < n; j++) {
				List<Integer> first = tests.get(0).responseTimes.get(i);
				List<Integer> second = tests.get(1).responseTimes.get(i);
				assertTrue(first.get(j) >= second.get(j),"i = " + i + " " + first + " "  + second);//first.toString() + second.toString());
			}
		}
	}
}
