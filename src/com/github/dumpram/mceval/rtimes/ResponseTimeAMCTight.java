package com.github.dumpram.mceval.rtimes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class ResponseTimeAMCTight implements IResponseTime {

	private IResponseTime responseTimeHI = new ResponseTimeHI();

	private IResponseTime responseTimeLO = new ResponseTimeLO();

	@Override
	public int responseTime(int i, MCTaskSet set) {
		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int n = tasks.size();
		int R = 0;
		int Rmax = 0;
		int L = task.getL();
		int C = task.getWCET(1);
		int D = task.getD();
		int t = C;

		int R_lo = responseTimeLO.responseTime(i, set);

		if (L == 0 || R_lo > D) {
			return R_lo;
		}
		
		Rmax = R_lo;

		int R_hi = responseTimeHI.responseTime(i, set);
		if (R_hi > D) {
			return R_hi;
		}
		
		if (R_hi > R_lo) {
			Rmax = R_hi;
		}
		
		List<Integer> S = getS(i, set, R_lo);
		if (!S.isEmpty()) {
			for (int ss = 0; ss < n; ss++) {
				MCTask taskS = tasks.get(ss);
				if (!(taskS.getL() == 0 || ss > i)) {
					for (Integer s : S) {
						int TS = taskS.getT();
						int	KS = (int) Math.floor(1.0 * s / TS);
						
						R = 0;
						t = C;
						while (R != t && R <= D) {
							R = t;
							t = C;
							for (int j = 0; j < i; j++) {
								MCTask taskJ = tasks.get(j);
								int LJ = taskJ.getL();
								int CHI = taskJ.getWCET(1);
								int CLO = taskJ.getWCET(0);
								int TJ = taskJ.getT();
								int DJ = taskJ.getD();
								int KJ = (int) Math.floor(1.0 * s / TJ);
								if (LJ == 0) {
									t += (KJ + 1) * CLO;
								}
								if (ss < j && LJ == 0) {
									if (KJ * TJ >= KS * TS) {
										t-=CLO;
									} else {
										t-=CLO;
										t += Math.min(KS * TS - KJ * TJ, CLO);
									}
								} 
								if (j < ss && LJ == 1) {
									int m = getm(TJ, DJ, s, R);
									t += (m * CHI + ((int) Math.ceil(1.0 * R / TJ) - m) * CLO);
								} else if (j >= ss && LJ == 1) {
									int m = getM(TJ, DJ, s, R);
									t += (m * CHI + ((int) Math.ceil(1.0 * R / TJ) - m) * CLO);
								}
							}
						}
						if (R > Rmax) {
							Rmax = R;
						}
					}
				}
			}
		}
		
		return Rmax;
	}

	private int getM(int Tk, int Dk, Integer s, int t) {
		return Math.min((int) Math.ceil((((double) (t - s - (Tk - Dk))) / Tk) + 1), (int) Math.ceil(((double) t) / Tk));
	}

	private int getm(int Tk, int Dk, Integer s, int t) {
		return (int) Math.ceil((((double) (t - s - (Tk - Dk))) / Tk));
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
	
	@SuppressWarnings("unused")
	private List<Integer> getSfromS(int s, MCTaskSet set, int r_lo) {
		List<Integer> forExport = new ArrayList<Integer>();
		List<MCTask> tasks = set.getTasks();
		MCTask taskS = tasks.get(s);
		int Ts = taskS.getT();
		int Cs = taskS.getWCET(0);
		int Cshi = taskS.getWCET(1);
		int Ds = taskS.getD();
//		List<Integer> forbidden = new ArrayList<Integer>();
//		for (int j = 0; j < s; j++) {
//			int Tj = tasks.get(j).getT();
//			int Cj = tasks.get(j).getWCET(0);
//			for (int k = 0; k * Tj < r_lo; k++) {
//				for (int p = k * Tj; p < r_lo && p < k * Tj + Cj; p++) {
//					forbidden.add(p);
//				}
//			}
//		}
		for (int k = 0; k * Ts < r_lo; k++) {
//			if(k * Ts + Ds - (Cshi - Cs) < r_lo) {
//				forExport.add(k * Ts + Ds - (Cshi - Cs));
//				forExport.add(k * Ts + Cs);
//			}
			for (int j = k * Ts + Cs; j < k * Ts + Ds - (Cshi - Cs) && j < r_lo; j++) {
				boolean flag = true;
				for (int hp = 0; hp < s; hp++) {
					MCTask taskhp = tasks.get(hp);
					int Thp = taskhp.getT();
					int Chp = taskhp.getWCET(0);
					if (j % Thp < Chp) { 
						flag = false;
						break;
					}
				}
				if (flag) {
					forExport.add(j);
				}
			}
		}
	
		
		return forExport;
	}

	@Override
	public String toString() {
		return "AMC-tight";
	}

	@Override
	public String printResponseTime(int i, HashMap<Integer, Integer> priorityOrder, MCTaskSet orderedSet) {
		// TODO Auto-generated method stub
		return null;
	}

}
