package com.github.dumpram.mceval.rtimes;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class ResponseTimeExactPeriodic implements IResponseTime {

	ResponseTimeLO responseTimeLO = new ResponseTimeLO();

	ResponseTimeHI responseTimeHI = new ResponseTimeHI();

	List<List<Integer>> jobResponseTimesLO = new ArrayList<List<Integer>>();

	List<List<Integer>> jobResponseTimesMC = new ArrayList<List<Integer>>();

	@Override
	public int responseTime(int i, MCTaskSet set) {
		List<MCTask> tasks = set.getTasks();

		int Rlo = responseTimeLO.responseTime(i, set);

		if (tasks.get(i).getL() == 0) {
			return Rlo;
		}

		int Rhi = responseTimeHI.responseTime(i, set);
		int Rmax = Integer.max(Rlo, Rhi);

		if (Rmax > tasks.get(i).getD()) {
			return Rmax;
		}

		for (int j = 0; j < i; j++) {
			if (tasks.get(j).getL() == 1) {
				List<Integer> ss = getCriticalitySwitchInstances(j, i, set, Rlo);
				for (Integer s : ss) {
					int r0 = responseTimeMCForKthJob(i, 0, set, s);
					if (r0 > Rmax) {
						Rmax = r0;
					}
				}
			}
		}

		return Rmax;
	}

	private List<Integer> getCriticalitySwitchInstances(int e, int i, MCTaskSet set, int rlo) {
		List<Integer> ss = new ArrayList<Integer>();

		for (int k = 0, rk = 0; rk < rlo; k++) {
			rk = responseTimeLOForKthJob(e, k, set);
			if (rk < rlo) {
				ss.add(rk);
			}
		}

		return ss;
	}

	public int responseTimeLOForKthJob(int i, int k, MCTaskSet set) {

		List<MCTask> tasks = set.getTasks();
		MCTask task = set.getTasks().get(i);
		int R = 0;
		int offset = CarryOverJobInterferenceLO(i, k, set);
		int t = task.getWCET(0) + offset;
		int D = task.getD();

		while (t != R && R <= (k + 1) * D) {
			R = t;
			t = task.getWCET(0) + offset;
			for (int j = 0; j < i; j++) {
				t += (int) Math.ceil(1.0 * (R - offset) / tasks.get(j).getT()) * tasks.get(j).getWCET(0);
			}
		}

		return R;

	}

	private int CarryOverJobInterferenceLO(int i, int k, MCTaskSet set) {
		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int releaseTime = k * task.getT();

		for (int j = i - 1; j >= 0; j--) {
			int _k = Math.floorDiv(releaseTime, tasks.get(j).getT());
			int rkth = responseTimeLOForKthJob(j, _k, set);
			if (rkth > releaseTime && _k * tasks.get(j).getT() < releaseTime) {
				return rkth;
			}
		}

		return releaseTime;
	}

	private int responseTimeMCForKthJob(int i, int k, MCTaskSet set, int s) {
		List<MCTask> tasks = set.getTasks();
		MCTask task = set.getTasks().get(i);
		int R = 0;
		int offset = CarryOverJobInterferenceMC(i, k, set, s);
		int t = task.getWCET(1) + offset;
		int D = task.getD();

		while (t != R && R <= D) {
			R = t;
			t = task.getWCET(1) + offset;
			for (int j = 0; j < i; j++) {
				t += (int) Math.ceil(1.0 * (R - offset) / tasks.get(j).getT()) * tasks.get(j).getWCET(0);
			}
		}

		return R;
	}

	private int CarryOverJobInterferenceMC(int i, int k, MCTaskSet set, int switchTime) {
		List<MCTask> tasks = set.getTasks();

		for (int j = i - 1; j >= 0; j--) {
			int _k = Math.floorDiv(switchTime, tasks.get(j).getT());
			int rkth = responseMCForKthCarryOverJob(j, _k, set, switchTime);
			if (rkth > switchTime) {
				return rkth;
			}
		}

		return switchTime;
	}

	private int responseMCForKthCarryOverJob(int i, int _k, MCTaskSet set, int switchTime) {
		List<MCTask> tasks = set.getTasks();
		MCTask task = set.getTasks().get(i);
		int R = 0;
		int offset = CarryOverJobInterferenceForMC(i, _k, set, switchTime);
		int t = task.getWCET(1) + offset;
		int D = task.getD();

		while (t != R && R <= D) {
			R = t;
			t = task.getWCET(1) + offset;
			for (int j = 0; j < i; j++) {
				if (tasks.get(j).getL() == 1) {
					t += (int) Math.ceil(1.0 * (R - offset) / tasks.get(j).getT()) * tasks.get(j).getWCET(1);
				}
			}
		}

		return R;
	}

	private int CarryOverJobInterferenceForMC(int i, int _k, MCTaskSet set, int switchTime) {
		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int releaseTime = _k * task.getT();

		for (int j = i - 1; j >= 0; j--) {
			int __k = Math.floorDiv(releaseTime, tasks.get(j).getT());
			if (tasks.get(j).getL() == 1) {
				int rkthMC = responseTimeMCForKthJob(j, __k, set, switchTime);
				if (rkthMC > switchTime) {
					return rkthMC;
				}
			}
			int rkthLO = responseTimeLOForKthJob(releaseTime, __k, set);
			if (rkthLO > releaseTime && rkthLO < switchTime) {
				return rkthLO;
			} else if (rkthLO >= switchTime) {
				return switchTime;
			}
		}

		return releaseTime;
	}

	public void clearCache() {
		jobResponseTimesLO.clear();
		jobResponseTimesMC.clear();
	}

	@Override
	public String toString() {
		return "Exact Periodic";
	}

}
