package com.github.dumpram.mceval.rtimes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class ResponseTimeAMCmax implements IResponseTime {

	private IResponseTime responseTimeHI = new ResponseTimeHI();

	private IResponseTime responseTimeLO = new ResponseTimeLO();
	
	private int smax;
	
	private int M;

	@Override
	public int responseTime(int i, MCTaskSet set) {
		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int R = 0;
		int Rmax = 0;
		int L = task.getL();
		int C = task.getWCET(1);
		int D = task.getD();
		int t = C;

		int R_lo = responseTimeLO.responseTime(i, set);
		if (L == 0) {
			return R_lo;
		}

		if (R_lo > D) {
			return R_lo;
		}

		List<Integer> S = getS(i, set, R_lo);

		Rmax = C;
		int m = 0;

		for (Integer s : S) {
			R = 0;
			t = C;
			while (R != t && R <= D) {
				R = t;
				t = C;
				for (int j = 0; j < i; j++) {
					MCTask taskJ = tasks.get(j);
					if (taskJ.getL() == 0) {
						t = (int) (t + (int) (Math.floor((double) s / taskJ.getT()) + 1) * taskJ.getWCET(0));
					} else {
						m = getM(taskJ.getT(), taskJ.getD(), s, R);
						t = (int) (t + m * taskJ.getWCET(1)
								+ ((int) Math.ceil((double) R / taskJ.getT()) - m) * taskJ.getWCET(0));
					}
				}
			}
			if (R > Rmax) {
				Rmax = R;
				smax = s;
				M = m;
			}
		}

		int R_hi = responseTimeHI.responseTime(i, set);

		if (Rmax < R_lo) {
			Rmax = R_lo;
		}
		if (Rmax < R_hi) {
			Rmax = R_hi;
		}

		return Rmax;
	}

	private int getM(int Tk, int Dk, Integer s, int t) {
		return Math.min((int) Math.ceil((((double) (t - s - (Tk - Dk))) / Tk) + 1), (int) Math.ceil(((double) t) / Tk));
	}

	private List<Integer> getS(int i, MCTaskSet set, int r_lo) {
		List<Integer> forExport = new ArrayList<Integer>();
		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		for (int j = 0; j < i; j++) {
			MCTask taskJ = tasks.get(j);
			if (taskJ.getL() < task.getL() && j != i) {
				for (int s = 0; s <= ((int) Math.ceil((double) r_lo / taskJ.getT()) - 1) * taskJ.getT(); s += taskJ
						.getT()) {
					if (!forExport.contains(s)) {
						forExport.add(s);
					}
				}
			}
		}
		return forExport;
	}
	
	@Override
	public String toString() {
		return "AMC-max";
	}

	@Override
	public String printResponseTime(int i, HashMap<Integer, Integer> priorityOrder, MCTaskSet set) {
		String forExport = "";

		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int L = task.getL();
		int C = task.getWCET(L);
		int CLO = task.getWCET(0);
		int R = responseTime(i, set);
		int RLO = responseTimeLO.responseTime(i, set);
		int RHI = responseTimeHI.responseTime(i, set);
		
		int idx = priorityOrder.get(i);
		
		forExport += "\n";
				
		forExport += "R_" + (idx + 1) + "^{LO} = " + CLO + "+";

		for (int j = 0; j < i; j++) {
			MCTask taskJ = tasks.get(j);
			forExport += "\\lceil\\frac{R_" + (idx + 1) + "}{" + taskJ.getT() + "}\\rceil \\cdot" + taskJ.getWCET(0)
					+ "+";
		}

		forExport = forExport.substring(0, forExport.length() - 1) + " = " + RLO;

		forExport += "\n";
		
		forExport += "R_" + (idx + 1) + "^{HI} = " + C + "+";

		for (int j = 0; j < i; j++) {
			if (tasks.get(j).getL() == 1) {
				MCTask taskJ = tasks.get(j);
				forExport += "\\lceil\\frac{R_" + (idx + 1) + "}{" + taskJ.getT() + "}\\rceil \\cdot" + taskJ.getWCET(1)
						+ "+";
			}
		}
		
		forExport = forExport.substring(0, forExport.length() - 1) + " = " + RHI;
		
		forExport += "\n";
		
		forExport += "R_" + (idx + 1) + "^{MC} = " + C + "+";

		for (int j = 0; j < i; j++) {

			MCTask taskJ = tasks.get(j);
			if (taskJ.getL() == 0) {
				forExport += "\\bigg(\\bigg\\lfloor\\frac{" + smax + "}{" + taskJ.getT() + "}\\bigg\\rfloor + 1 \\bigg) \\cdot"
						+ taskJ.getWCET(0) + "+";
			} else {
				forExport += M + "\\cdot " + taskJ.getWCET(1) + " + \\bigg(\\lceil\\frac{R_" + (idx + 1) + "^{MC}}{" + taskJ.getT() + "}\\rceil - " + M + "\\bigg) \\cdot"
						+ taskJ.getWCET(0) + "+";
			}

		}
		
		forExport = forExport.substring(0, forExport.length() - 1) + " = " + R;
		
		return forExport;
	}
}
