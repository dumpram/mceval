package com.github.dumpram.mceval.results;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.assignments.PriorityAssignmentCrMPO;
import com.github.dumpram.mceval.assignments.PriorityAssignmentDM;
import com.github.dumpram.mceval.assignments.PriorityAssignmentNOPA;
import com.github.dumpram.mceval.assignments.PriorityAssignmentOPA;
import com.github.dumpram.mceval.evaluation.TestItem;
import com.github.dumpram.mceval.evaluation.TestUtils;
import com.github.dumpram.mceval.ftests.FeasibilityTestEfficientExact;
import com.github.dumpram.mceval.ftests.FeasibilityTestResponseTime;
import com.github.dumpram.mceval.ftests.FeasibilityTestUBHL;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCTight;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCrtb;
import com.github.dumpram.mceval.rtimes.ResponseTimeClassic;
import com.github.dumpram.mceval.rtimes.ResponseTimePeriodic;
import com.github.dumpram.mceval.rtimes.ResponseTimeSMC;
import com.github.dumpram.mceval.rtimes.ResponseTimeSMCno;
import com.github.sh0nk.matplotlib4j.Plot;
import com.github.sh0nk.matplotlib4j.PythonExecutionException;

public class SchedulabilityTestN8100 {
	
	
	public static final String PHDWORKSPACE = "/home/dumpram/Desktop/aktivnosti/doktorski/workspace/phd/images/results_rta_data/";

	public static void main(String[] args) throws IOException, PythonExecutionException {
		List<TestItem> tests = new ArrayList<TestItem>();
		TestItem crmpa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeClassic()),
				new PriorityAssignmentCrMPO());
		TestItem dmpa = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeClassic()),
				new PriorityAssignmentDM());
		TestItem ubhl = new TestItem(new FeasibilityTestUBHL(), new PriorityAssignmentDM());
		TestItem smcno = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMCno()),
				new PriorityAssignmentOPA(new ResponseTimeSMCno()));
		TestItem smc = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeSMC()),
				new PriorityAssignmentOPA(new ResponseTimeSMC()));
		TestItem amcmax = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCmax()),
				new PriorityAssignmentOPA(new ResponseTimeAMCmax()));
		TestItem amcrtb = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCrtb()),
				new PriorityAssignmentOPA(new ResponseTimeAMCrtb()));
		TestItem exact = new TestItem(new FeasibilityTestEfficientExact(), new PriorityAssignmentNOPA());
		TestItem amctight = new TestItem(new FeasibilityTestResponseTime(new ResponseTimeAMCTight()),
				new PriorityAssignmentNOPA());
		TestItem exactperiodic = new TestItem(new FeasibilityTestResponseTime(new ResponseTimePeriodic()),
				new PriorityAssignmentNOPA());
		
		
		//tests.add(ubhl);
		//tests.add(crmpa);
		//tests.add(dmpa);
		//tests.add(smcno);
		//tests.add(smc);
		tests.add(amcrtb);
		tests.add(amcmax);
		//tests.add(exact);
		tests.add(amctight);
		tests.add(exactperiodic);
		
		int minN = 8;
		int maxN = 100;
		int deltan = 5;
		double utilizationIncrement = 0.05;
		int nsets = 1000;
		int tmin = 2;
		int tmax = 10000;
		int criticality = 2;
		int DC = 1;
		int CF = 2;
		double CP = 0.5;
		double delta = utilizationIncrement / 2;
		boolean fixed = false;

		Plot result = TestUtils.runTestN(tests, minN, maxN, deltan, 0.6, nsets,
				tmin, tmax, criticality, DC, CF, CP, delta, fixed, Paths.get(PHDWORKSPACE, "schedulability_n8100").toString());
		
		result.show();
	}

}
