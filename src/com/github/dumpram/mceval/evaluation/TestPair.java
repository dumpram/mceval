package com.github.dumpram.mceval.evaluation;

import com.github.dumpram.mceval.interfaces.IFeasibilityTest;
import com.github.dumpram.mceval.interfaces.IPriorityAssignment;

public class TestPair {
	
	public IFeasibilityTest feasibilityTest;
	
	public IPriorityAssignment priorityAssignment;

	public TestPair(IFeasibilityTest feasibilityTest, IPriorityAssignment priorityAssignment) {
		super();
		this.feasibilityTest = feasibilityTest;
		this.priorityAssignment = priorityAssignment;
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
