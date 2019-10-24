package com.github.dumpram.mceval.ftests;

import com.github.dumpram.mceval.interfaces.IFeasibilityTest;
import com.github.dumpram.mceval.models.MCTaskSet;

public class FeasibilityTestEDFWithVD implements IFeasibilityTest {
	
	public boolean isFeasible(MCTaskSet set) {
		double x = set.getUtilizationLOHI() / (1 - set.getUtilizationLOLO());
		
		if (x * set.getUtilizationLOLO() + set.getUtilizationHIHI() <= 1) {
			return true;
		}
		
		return false;
	}
	
	@Override
	public String toString() {
		return "EDF_VD";
	}
}
