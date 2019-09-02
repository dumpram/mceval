package com.github.dumpram.mceval.evaluation;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.rtimes.ResponseTimeExactPeriodic;

public class ExactPeriodicTest {
	
	
	@Test
	public void CounterExample1() {
		List<MCTask> tasks = new ArrayList<MCTask>();

		tasks.add(new MCTask(new int[] { 1, 2 }, 5, new int[] { 5, 5 }, 1)); 
		tasks.add(new MCTask(new int[] { 2, 2 }, 7, new int[] { 7, 7 }, 0)); 
		tasks.add(new MCTask(new int[] { 4, 4 }, 14, new int[] { 14, 14 }, 0)); 
		tasks.add(new MCTask(new int[] { 3, 4 }, 18, new int[] { 18, 18 }, 1)); 

		MCTaskSet set = new MCTaskSet(tasks);

		System.out.println(new ResponseTimeExactPeriodic().responseTimeLOForKthJob(2, 1, set));
		
	}

}
