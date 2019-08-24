package com.github.dumpram.mceval.rtimes;

import java.util.List;

import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class ResponseTimeAMCrtb implements IResponseTime {

	private IResponseTime responseTimeHI = new ResponseTimeHI();

	private IResponseTime responseTimeLO = new ResponseTimeLO();

	@Override
	public int responseTime(int i, MCTaskSet set) {
		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int R = 0;
		int L = task.getL();
		int C = task.getWCET(L);
		int D = task.getD();
		int t = task.getWCET(task.getL());

		int R_lo = responseTimeLO.responseTime(i, set);
		if (L == 0) {
			//System.out.println(printResponseTimeLO(i, set, R_lo));
			return R_lo;
		}

		while (R != t && R <= D) {
			R = t;
			t = C;
			for (int j = 0; j < i; j++) {
				MCTask taskJ = tasks.get(j);
				if (taskJ.getL() < L) {
					t = (int) (t + (int) Math.ceil((double) R_lo / taskJ.getT()) * taskJ.getWCET(0));
				} else {
					t = (int) (t + (int) Math.ceil((double) R / taskJ.getT()) * taskJ.getWCET(1));
				}
			}
		}

		int R_hi = responseTimeHI.responseTime(i, set);

		if (R < R_lo && R_lo > R_hi) {
			// System.out.println(printResponseTimeMC(i, set, p, R_lo));
			R = R_lo;
		} else if (R < R_hi && R_hi > R_lo) {
			// System.out.println(printResponseTimeHI(i, set, p, R_hi));
			R = R_hi;
		} else {
			// System.out.println(printResponseTimeMC(i, set, p, R));
		}

		return R;
	}

	public String printResponseTimeLO(int i, MCTaskSet set, int R) {
		String forExport = "";

		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int n = tasks.size();
		int C = task.getWCET(0);

		forExport += "R_" + (i + 1) + "^{LO} = " + C + "+";

		for (int j = 0; j < n; j++) {
			MCTask taskJ = tasks.get(j);
			forExport += "\\lceil\\frac{R_" + (i + 1) + "}{" + taskJ.getT() + "}\\rceil \\cdot" + taskJ.getWCET(0)
					+ "+";
		}

		return forExport.substring(0, forExport.length() - 1) + " = " + R;
	}

	public String printResponseTimeHI(int i, MCTaskSet set, int R) {
		String forExport = "";

		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int C = task.getWCET(1);

		forExport += "R_" + (i + 1) + "^{HI} = " + C + "+";

		for (int j = 0; j < i; j++) {
			if (tasks.get(j).getL() == 1) {
				MCTask taskJ = tasks.get(j);
				forExport += "\\lceil\\frac{R_" + (i + 1) + "}{" + taskJ.getT() + "}\\rceil \\cdot" + taskJ.getWCET(1)
						+ "+";
			}
		}

		return forExport.substring(0, forExport.length() - 1) + " = " + R;
	}

	public String printResponseTimeMC(int i, MCTaskSet set, int R) {
		String forExport = "";

		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int C = task.getWCET(1);

		forExport += "R_" + (i + 1) + "^{LO}= " + responseTimeLO.responseTime(i, set) + "," + "R_" + (i + 1)
				+ "^{MC} = " + C + "+";

		for (int j = 0; j < i; j++) {

			MCTask taskJ = tasks.get(j);
			if (taskJ.getL() == 0) {
				forExport += "\\lceil\\frac{R_" + (i + 1) + "^{LO}}{" + taskJ.getT() + "}\\rceil \\cdot"
						+ taskJ.getWCET(0) + "+";
			} else {
				forExport += "\\lceil\\frac{R_" + (i + 1) + "^{MC}}{" + taskJ.getT() + "}\\rceil \\cdot"
						+ taskJ.getWCET(1) + "+";
			}

		}

		return forExport.substring(0, forExport.length() - 1) + " = " + R;
	}
	
	@Override
	public String toString() {
		return "AMC-rtb";
	}
}
