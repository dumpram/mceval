package com.github.dumpram.mceval.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExact;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.ftests.FeasibilityTestUBHL;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCTight;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;

public class ExampleTests {

	@Test
	public void Example1() {
		List<MCTask> tasks = new ArrayList<MCTask>();

		// pocetni skup D(HI) = D(LO)
		tasks.add(new MCTask(new int[] { 2, 4 }, 15, new int[] { 7, 7 }, 1)); // 7 7
		tasks.add(new MCTask(new int[] { 2, 2 }, 8, new int[] { 4, 4 }, 0)); // 4 4
		tasks.add(new MCTask(new int[] { 2, 2 }, 19, new int[] { 9, 9 }, 0)); // 9 9
		tasks.add(new MCTask(new int[] { 1, 2 }, 18, new int[] { 9, 9 }, 1)); // 9 9

		MCTaskSet set = new MCTaskSet(tasks);

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
		
		for (TestItem test : tests) {
			System.out.println(test.feasibilityTest.toString() + " " + test.feasibilityTest.isFeasible(set));
		}
		
	}
	
	@Test
	public void Example2() {
		List<MCTask> tasks = new ArrayList<MCTask>();

		// pocetni skup D(HI) = D(LO)
		tasks.add(new MCTask(new int[] { 1, 2 }, 10, new int[] { 10, 10 }, 1)); // 7 7
		tasks.add(new MCTask(new int[] { 1, 1 }, 5, new int[] { 2, 2 }, 0)); // 4 4
		tasks.add(new MCTask(new int[] { 4, 8 }, 13, new int[] { 13, 13 }, 1)); // 9 9

		MCTaskSet set = new MCTaskSet(tasks);

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
		
		for (TestItem test : tests) {
			System.out.println(test.feasibilityTest.toString() + " " + test.feasibilityTest.isFeasible(set));
		}
		
	}
	
	
	@Test
	public void CounterExample1() {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 1, 2 }, 5, new int[] { 5, 5 }, 1)); 
		tasks.add(new MCTask(new int[] { 2, 2 }, 7, new int[] { 7, 7 }, 0)); 
		tasks.add(new MCTask(new int[] { 4, 4 }, 14, new int[] { 14, 14 }, 0)); 
		tasks.add(new MCTask(new int[] { 3, 4 }, 18, new int[] { 18, 18 }, 1)); 

		MCTaskSet set = new MCTaskSet(tasks);

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
		
		for (TestItem test : tests) {
			System.out.println(test.feasibilityTest.toString() + " " + test.feasibilityTest.isFeasible(set));
		}
		
	}

}
