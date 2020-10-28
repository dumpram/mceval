package com.github.dumpram.mceval.ftests;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.interfaces.IFeasibilityTest;
import com.github.dumpram.mceval.models.MCState;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.models.TaskState;
import com.github.dumpram.mceval.rtimes.ResponseTimeAMCmax;
import com.github.dumpram.mceval.rtimes.ResponseTimeLO;

public class FeasibilityTestEfficientExactWrong implements IFeasibilityTest {
	
	private ResponseTimeLO responseTimeLO = new ResponseTimeLO();
	
	private ResponseTimeAMCmax amcmax = new ResponseTimeAMCmax();

	// pr(tau_i) = i
	public boolean isFeasible(MCTaskSet set) {
		int n = set.getTasks().size();
		MCState.id_cnt = 0;
		for (int k = 0; k < n; k++) {
			MCTask task = set.getTasks().get(k);
			if (task.getL() == 0 || allHiAreLo(set, k)) {
				int R = responseTimeLO.responseTime(k, set);
				if (R > task.getD()) {
					return false;
				}
			} else if (areAllSameCriticality(set, k)) {
				int R = responseTimeSAS(set, k);
				if (R > task.getD()) {
					return false;
				}
			} else {
				if (!stateBasedRTA(set, k)) {
					return false;
				}
			}
		}

		return true;
	}

	private boolean stateBasedRTA(MCTaskSet set, int k) {

		int Robs, Rsuff;
		
		Robs = responseTimeLO.responseTime(k, set);
		
		Rsuff = amcmax.responseTime(k, set);
		
		if (Robs > set.getTasks().get(k).getD()) {
			return false;
		}

		List<TaskState> taskStates = new ArrayList<TaskState>();

		for (int i = 0; i < k + 1; i++) {
			taskStates.add(new TaskState(0, 0, 0, 0));
		}

		MCState state = new MCState(0, 0, taskStates);
		state.root = true;
		
		int sp = 1;
		
		ArrayList<MCState> unexploredStack = new ArrayList<MCState>();
		unexploredStack.add(state);
		while (!unexploredStack.isEmpty()) {
			MCState s = unexploredStack.get(unexploredStack.size() - 1);
			s.sp = sp++;
			unexploredStack.remove(unexploredStack.size() - 1);
			List<MCState> successorStates = getSuccessorStates(s, state, set, k);
			//s.successorStates.addAll(successorStates);
			for (MCState _s : successorStates) {
				TaskState tk = _s.taskStates.get(k);
				MCTask task = set.getTasks().get(k);
				if (tk.c == 0) {
					Robs = Math.max(Robs, task.getT() - tk.p);
					if (Robs == Rsuff) {
						//System.out.println(state.draw());
						return true;
					} else {
						continue;
					}
				} else if (tk.c > tk.q) {
					//System.out.println(state.draw());
					return false;
				} else if (isPruned(_s, set, k, state, Robs)) {
					continue;
				} else if (!unexploredStack.contains(_s)) {
					unexploredStack.add(_s);
					//_s.sp = sp++;
				}
			}
		}
		
		//System.out.println(state.draw());
		
		return true;
	}

	private boolean isPruned(MCState _s, MCTaskSet set, int k, MCState initialState, int Robs) {
		
		TaskState tsk = _s.taskStates.get(k);
		MCTask taskK = set.getTasks().get(k);
		
		// Pr-1
		for (int i = 0; i < k; i++) {
			MCTask task = set.getTasks().get(i);
			TaskState ts = _s.taskStates.get(i);
			if (-ts.p >= task.getT()) {
				_s.pr = 1;
				return true;
			}
		}
		
		// Pr-2
		boolean indicator = true;
		
		for (int i = 0; i < k; i++) {	
			TaskState ts = _s.taskStates.get(i);
			if (!(taskK.getT() == tsk.p && ts.c == 0)) {
				indicator = false;
				break;
			}
		}
		
		if (indicator) {
			_s.pr = 2;
			return true;
		}
		
		// Pr-3
		for (int i = 0; i < k; i++) {
			MCTask task = set.getTasks().get(i);
			TaskState ts = _s.taskStates.get(i);
			if (_s.gamma == 1 && task.getL() == 1 && (ts.p <= 0 || 
					(ts.c > 0 && ts.e < task.getWCET(1)))) {
				_s.pr = 3;
				return true;
			}
		}

		// check state, pruning rules Pr-4 - Pr-11 apply only to LO critical not initial state 
		if (!(_s.gamma == 0 && !_s.equals(initialState))) {
			return false;
		}
		
		// get highest priority task exhibiting HI-critical behavior 
		int m = -1;
		
		for (int i = 0; i < k + 1; i++) {
			TaskState ts = _s.taskStates.get(i);
			MCTask task = set.getTasks().get(i);
			if (ts.e > task.getWCET(0)) {
				m = i;
				break;
			}
		}
		
		// pruning rules cannot apply
		if (m == -1) {
			return false;
		}
		
		TaskState tsm = _s.taskStates.get(m);
		MCTask taskM = set.getTasks().get(m);
		
		// Pr-4
		for (int i = m + 1; i < k + 1; i++) {
			TaskState ts = _s.taskStates.get(i);
			MCTask task = set.getTasks().get(i);
			if (task.getL() == 1 && ts.p <= 0) {
				_s.pr = 4;
				return true;
			}
		}
		
		// Pr-5
		for (int i = m + 1; i < k + 1; i++) {
			TaskState ts = _s.taskStates.get(i);
			MCTask task = set.getTasks().get(i);
			if (task.getL() == 1 && ts.c > 0 && ts.e < task.getWCET(1)) {
				_s.pr = 5;
				return true;
			}
		}
		
		// Pr-6
		for (int i = m + 1; i < k; i++) {
			TaskState ts = _s.taskStates.get(i);
			MCTask task = set.getTasks().get(i);
			if (task.getL() == 0 && ts.p <= 0) {
				_s.pr = 6;
				return true;
			}
		}
		
		//Pr-7
		int _m = -1;
		
		for (int i = m - 1; i >= 0; i--) {
			MCTask task = set.getTasks().get(i);
			if (task.getL() == 1) {
				_m = i;
				break;
			}
		}
		
		if (_m >= 0) {
			for (int i = _m + 1; i < m; i++) {
				TaskState ts = _s.taskStates.get(i);
				MCTask task = set.getTasks().get(i);
				if (task.getL() == 0 && ts.p <= 0) {
					_s.pr = 7;
					return true;
				}
			}
		}
		
		
		//Pr-8
		for (int i = m + 1; i < k + 1; i++) {
			TaskState ts = _s.taskStates.get(i);
			MCTask task = set.getTasks().get(i);
			if (tsm.p == taskM.getT() && task.getL() == 0 && ts.c > 0 && 
					sumUpToWithoutM(_s, m, i) == 0) {
				_s.pr = 8;
				return true;
			}
		}
		
		int tcms = getTCMS(_s, set, m, k);
		int _rsk = getBound(_s, set, m, k, tcms);
		int rsk = _rsk + taskK.getT() - tsk.p;
		//Pr-9
		if (rsk <= Robs) {
			_s.pr = 9;
			return true;
		}
		
		
		//Pr-10
		for (int i = 0; i < m; i++) {
			TaskState ts = _s.taskStates.get(i);
			MCTask task = set.getTasks().get(i);
			boolean valid = true;
			if (task.getL() == 1 && (ts.p <= 0 || (ts.c > 0 && ts.e < task.getWCET(1)))) {
				for (int j = 0; j < m; j++) {
					MCTask taskJ = set.getTasks().get(j);
					if (taskJ.getL() == 1) {
						continue;
					}
					TaskState tsj = _s.taskStates.get(j);
					if (!(j < i && tsj.p >= tcms) || !(j > i && tsj.c == 0 && tsj.p >= tcms)) {
						valid = false;
						break;
					} 
				}
				if (valid) {
					_s.pr = 10;
					return true;
				}
			}
		}
		
		//Pr-11
		_m = -1;
		int minp = 0;
		boolean firstpass = true;
		for (int i = 0; i < m; i++) {
			TaskState ts = _s.taskStates.get(i);
			MCTask task = set.getTasks().get(i);
			if (firstpass && task.getL() == 1) {
				minp = ts.p;
				_m = i;
				firstpass = false;
				continue;
			}
			
			if (ts.p < minp && task.getL() == 1) {
				minp = ts.p;
				_m = i;
			}
		}
		
		if (_m == -1) {
			return false;
		}
		
		TaskState ts_m = _s.taskStates.get(_m);
		
		int t = -1;
		
		if (tsk.p == 0) {
			t = -1;
		} else {
			t = taskK.getT() - tsk.p;
		}
		
		if (tcms <= ts_m.p) {
			for (int i = 0; i < m; i++) {
				TaskState ts = _s.taskStates.get(i);
				MCTask task = set.getTasks().get(i);
				int ri = t - (task.getT() - ts.p);
				int rm = t - (taskM.getT() - tsm.p);
				if (ts.p <= 0 || (rm < ri && ts.phi > 0)) {
					_s.pr = 11;
					return true;
				}
			}
		}
		
		for (int i = 0; i < m; i++) {
			TaskState ts = _s.taskStates.get(i);
			MCTask task = set.getTasks().get(i);
			int ri = t - (task.getT() - ts.p);
			int rm = t - (taskM.getT() - tsm.p);
			if (ts.phi > 0 && ts.p <= ts_m.p && rm < ri) {
				_s.pr = 11;
				return true;
			}
		}
		
		for (int i = 0; i < m; i++) {
			TaskState ts = _s.taskStates.get(i);
			MCTask task = set.getTasks().get(i);
			int ri = t - (task.getT() - ts.p);
			int rm = t - (taskM.getT() - tsm.p);
			if (ts.phi > 0 && rm < ri && ts.c == 0) {
				_s.pr = 11;
				return true;
			}
		}
//		
		return false;
	}

	private int getBound(MCState _s, MCTaskSet set, int m, int k, int tcms) {
		TaskState tsk = _s.taskStates.get(k);
		
		int t = tsk.c;
		int _rsk = t - 1;
		MCTask taskK = set.getTasks().get(k);
		
		while (t != _rsk && _rsk + taskK.getT() - tsk.p <= taskK.getD())  {
			_rsk = t;
			t = tsk.c;
			for (int j = 0; j < m; j++) {
				TaskState tsj = _s.taskStates.get(j);
				MCTask taskJ = set.getTasks().get(j);
				if (taskJ.getL() == 0) {
					t += tsj.c + (int) Math.ceil(1.0 * Math.max(tcms - Math.max(1, tsj.p), 0) / taskJ.getT()) *
							taskJ.getWCET(0);
				}
			}
			
			for (int j = 0; j < k; j++) {
				TaskState tsj = _s.taskStates.get(j);
				MCTask taskJ = set.getTasks().get(j);
				if (taskJ.getL() == 1) {
					t += tsj.c + (int) Math.ceil(1.0 * Math.max(_rsk - Math.max(1, tsj.p), 0) / taskJ.getT()) *
							taskJ.getWCET(1);
				}
			}
		}
		
		return _rsk;
	}

	private int getTCMS(MCState _s, MCTaskSet set, int m, int k) {
		MCTask taskM = set.getTasks().get(m);
		TaskState tsm = _s.taskStates.get(m);
		int t = tsm.c - (taskM.getWCET(1) - taskM.getWCET(0));
		int tcms = t - 1;
		
		while (t != tcms) {
			tcms = t;
			t = tsm.c - (taskM.getWCET(1) - taskM.getWCET(0));
			for (int j = 0; j < m; j++) {
				TaskState tsj = _s.taskStates.get(j);
				MCTask taskJ = set.getTasks().get(j);
				t += tsj.c + (int) Math.ceil(1.0 * Math.max(tcms - Math.max(1, tsj.p), 0) / taskJ.getT()) * 
						taskJ.getWCET(0);
			}
		}
		
		return tcms;
	}

	private int sumUpToWithoutM(MCState _s, int m, int i) {
		int sum = 0;
		for (int j = 0; j < i; j++) {
			if (j != m) {
				sum += _s.taskStates.get(j).c;
			}
		}
		return sum;
	}

	private int responseTimeSAS(MCTaskSet set, int i) {
		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int R = 0;
		int C = task.getWCET(task.getL());
		int D = task.getD();
		int t = C;

		while (R != t && R <= D) {
			R = t;
			t = C;
			for (int j = 0; j < i; j++) {
				MCTask taskJ = tasks.get(j);
				t = (int) (t + 
						(int) Math.ceil((double) R / taskJ.getT()) * taskJ.getWCET(task.getL()));
			}
		}

		return R;
	}

	private boolean areAllSameCriticality(MCTaskSet set, int k) { 
		for (int i = 0; i < k + 1; i++) {
			MCTask task = set.getTasks().get(i);
			if (task.getL() != 1) {
				return false;
			}
		}
		return true;
	}

	private boolean allHiAreLo(MCTaskSet set, int k) {
		for (int i = 0; i < k + 1; i++) {
			MCTask task = set.getTasks().get(i);
			if (!(task.getWCET(1) == task.getWCET(0))) {
				return false;
			}
		}
		return true;
	}

	private List<MCState> getSuccessorStates(MCState s, MCState initialState, 
			MCTaskSet set, int k) {
		List<MCState> states = new ArrayList<MCState>();
		List<Integer[]> releasePatterns = new ArrayList<Integer[]>();
		int _t = s.t + 1;
		getReleasePatterns(s, set, releasePatterns, 0, new Integer[s.taskStates.size()], k, 
				initialState);

		for (Integer[] rp : releasePatterns) {
			List<TaskState> taskStates = new ArrayList<TaskState>();
			int _gamma = s.gamma;
			for (int i = 0; i < k + 1; i++) {
				int alpha = (Sch(s, k) == i) ? 1 : 0;
				int beta = (rp[i] > 0) ? 1 : 0;
				TaskState ts = s.taskStates.get(i);
				MCTask task = set.getTasks().get(i);
				int _c = ts.c - alpha + rp[i];
				int _q = Math.max(ts.q - 1, 0) + beta * set.getTasks().get(i).getD();
				int _e = 0;
				if (beta == 0 && ts.c - alpha == 0) {
					_e = 0;
				} else if (beta == 0 && ts.c - alpha > 0) {
					_e = ts.e;
				} else {
					_e = rp[i];
				}
				int _phi = 0;
				int _p = 0;
				if (rp[i] == 0) {
					_phi = ts.phi;
				} else if (s.equals(initialState)) {
					_phi = 0;
				} else {
					_phi = Math.abs(ts.p - 1);
				}
				
				if (rp[i] == 1) { // upitno...
					_p = task.getT();
				} else if (s.equals(initialState)) {
					_p = 0;
				} else {
					_p = ts.p - 1;
				}
				
				if (s.gamma == 0 && anyHighCriticalityDidNotSignalCompletion(s, set, k)) {
					_gamma = 1;
				}
				if (_gamma == 1 && set.getTasks().get(i).getL() == 0) {
					_c = _e = _p = _q = _phi = 0;
				}
				taskStates.add(new TaskState(_c, _q, _p, _e, _phi));
			}
			MCState newOne = new MCState(_t, _gamma, taskStates, rp);
			if (!states.contains(newOne)) {
				states.add(newOne);
			} else {
				states.get(states.indexOf(newOne)).releasePatterns.add(rp);
			}
			//System.out.println(Arrays.toString(rp) + "\n" + newOne.toString());
		}

		return states;
	}

	private boolean anyHighCriticalityDidNotSignalCompletion(MCState s, MCTaskSet set, int k) {

		for (int i = 0; i < k + 1; i++) {
			MCTask task = set.getTasks().get(i);
			TaskState state = s.taskStates.get(i);

			int alpha = (Sch(s, k) == i) ? 1 : 0;

			if (task.getL() == 1 && state.e > task.getWCET(0) && state.e - (state.c - alpha) == task.getWCET(0)) {
				return true;
			}
		}

		return false;
	}

	private void getReleasePatterns(MCState s, MCTaskSet set, List<Integer[]> patterns, int depth,
			Integer[] current, int k, MCState initialState) {

		if (depth == k + 1) {
			Integer[] dispose = new Integer[depth];
			for (int i = 0; i < depth; i++) {
				dispose[i] = current[i];
			}
			patterns.add(dispose);
			return;
		}

		MCTask task = set.getTasks().get(depth);
		
		TaskState ts = s.taskStates.get(depth);

		if (depth == k) {
			if (s.equals(initialState)) {
				current[depth] = task.getWCET(1);
			} else {
				current[depth] = 0;
			}
			getReleasePatterns(s, set, patterns, depth + 1, current, k, initialState);
		} else {
			if (ts.p - 1 > 0 || s.gamma == 1 && task.getL() == 0) {
				current[depth] = 0;
				getReleasePatterns(s, set, patterns, depth + 1, current, k, initialState);
			} else if (ts.p - 1 <= 0 && task.getWCET(0) == task.getWCET(1) && s.gamma == 0){		
				current[depth] = 0;
				getReleasePatterns(s, set, patterns, depth + 1, current, k, initialState);
				current[depth] = task.getWCET(0);
				getReleasePatterns(s, set, patterns, depth + 1, current, k, initialState);
				// sumnjivo trebalo bi biti samo za HI criticality
			} else if (ts.p - 1 <= 0 && task.getWCET(0) < task.getWCET(1) && s.gamma == 0)  {
				current[depth] = 0;
				getReleasePatterns(s, set, patterns, depth + 1, current, k, initialState);
				current[depth] = task.getWCET(0);
				getReleasePatterns(s, set, patterns, depth + 1, current, k, initialState);
				current[depth] = task.getWCET(1);
				getReleasePatterns(s, set, patterns, depth + 1, current, k, initialState);
			} else if (ts.p - 1 <= 0 && s.gamma == 1) {
				current[depth] = task.getWCET(1);
				getReleasePatterns(s, set, patterns, depth + 1, current, k, initialState);
			}
		}
	}

	private int Sch(MCState s, int k) {
		for (int i = 0; i < k + 1; i++) {
			if (s.taskStates.get(i).c > 0 && hpi(s.taskStates, i) == 0) {
				return i;
			}
		}
		return -1;
	}

	private int hpi(List<TaskState> taskStates, int i) {
		int sum = 0;

		for (int j = 0; j < i; j++) {
			sum += taskStates.get(j).c;
		}

		return sum;
	}
	
	@Override
	public String toString() {
		return "Exact";
	}

}
