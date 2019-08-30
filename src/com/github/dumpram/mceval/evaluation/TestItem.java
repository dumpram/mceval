package com.github.dumpram.mceval.evaluation;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.interfaces.IFeasibilityTest;
import com.github.dumpram.mceval.interfaces.IPriorityAssignment;
import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTaskSet;

public class TestItem {
	
	public IFeasibilityTest feasibilityTest;
	
	public IPriorityAssignment priorityAssignment;
	
	public List<MCTaskSet> schedulableSets = new ArrayList<MCTaskSet>();
	
	public List<MCTaskSetResult> setResults = new ArrayList<MCTaskSetResult>();

	public IResponseTime responseTime;
	
	public TestItem(IFeasibilityTest feasibilityTest, IPriorityAssignment priorityAssignment) {
		super();
		this.feasibilityTest = feasibilityTest;
		this.priorityAssignment = priorityAssignment;
	}
	
	public TestItem(IResponseTime responseTime) {
		this.responseTime = responseTime;
	}
	
	public int score() {
		return schedulableSets.size();
	}
	
	@Override
	public String toString() {
		String forExport = feasibilityTest.toString();
		if (!priorityAssignment.toString().isEmpty()) {
			forExport += "+" + priorityAssignment.toString();
		}
		return forExport;
	}
}
