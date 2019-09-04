package com.github.dumpram.mceval.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeEfficientExact;
import com.github.dumpram.mceval.rtimes.ResponseTimePeriodic;

public class ExactPeriodicTest {
	
	
	//@Test
	public void CounterExample1() {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 1, 2 }, 5, new int[] { 5, 5 }, 1)); 
		tasks.add(new MCTask(new int[] { 2, 2 }, 7, new int[] { 7, 7 }, 0)); 
		tasks.add(new MCTask(new int[] { 4, 4 }, 14, new int[] { 14, 14 }, 0)); 
		tasks.add(new MCTask(new int[] { 3, 4 }, 18, new int[] { 18, 18 }, 1)); 

		MCTaskSet set = new MCTaskSet(tasks);

		System.out.println(new ResponseTimePeriodic().responseTime(3, set));
		
	}
	
	//@Test
	public void CounterExample2() {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 4, 8 }, 12, new int[] { 12, 12 }, 1)); 
		tasks.add(new MCTask(new int[] { 1, 1 }, 10, new int[] { 10, 10 }, 0)); 
		tasks.add(new MCTask(new int[] { 1, 2 }, 9, new int[] { 9, 9 }, 1)); 
		
		MCTaskSet set = new MCTaskSet(tasks);
		
		set = new PriorityAssignmentNOPA().assign(set);
		
		System.out.println(set);
	
		System.out.println(new ResponseTimePeriodic().responseTime(1, set));
		System.out.println(new ResponseTimeEfficientExact().responseTime(1, set));

		
	}
	
	
	@Test
	public void CounterExample3() {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 1, 1 }, 9, new int[] { 4, 4 }, 0)); 
		tasks.add(new MCTask(new int[] { 2, 4 }, 11, new int[] { 5, 5 }, 1)); 
		tasks.add(new MCTask(new int[] { 1, 1 }, 4, new int[] { 2, 2 }, 0)); 
		
		MCTaskSet set = new MCTaskSet(tasks);
		
		set = new PriorityAssignmentNOPA().assign(set);
		
		System.out.println(set);
	
		System.out.println(new ResponseTimePeriodic().responseTime(1, set));
		System.out.println(new ResponseTimeEfficientExact().responseTime(1, set));

		
	}

}
