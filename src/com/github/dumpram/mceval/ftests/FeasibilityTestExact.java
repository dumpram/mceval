package com.github.dumpram.mceval.ftests;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.dumpram.mceval.models.MCState;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.models.TaskState;

public class FeasibilityTestExact {

	public static boolean test(MCTaskSet set, int[] pr) {
		int n = pr.length;

		List<TaskState> taskStates = new ArrayList<TaskState>();

		for (int i = 0; i < n; i++) {
			taskStates.add(new TaskState(0, 0, 0, 0));
		}

		MCState state = new MCState(0, 0, taskStates);

		HashSet<MCState> generatedStates = new HashSet<MCState>();
		generatedStates.add(state);
		HashSet<MCState> unexploredStack = new HashSet<MCState>();
		unexploredStack.add(state);

		while (!unexploredStack.isEmpty()) {
			System.out.println("Unexplored: " + unexploredStack.size());
			MCState s = unexploredStack.iterator().next();
			unexploredStack.remove(s);
			Set<MCState> successorStates = getSuccessorStates(s, set, pr);

			for (MCState _s : successorStates) {
				for (TaskState t : _s.taskStates) {
					if (t.c > 0 && t.q == 0 || t.c > t.e) {
						return false;
					}
				}
				if (!generatedStates.contains(_s)) {
					generatedStates.add(_s);
					unexploredStack.add(_s);
				}
			}
		}

		return true;

	}

	private static Set<MCState> getSuccessorStates(MCState s, MCTaskSet set, int[] pr) {
		HashSet<MCState> states = new HashSet<MCState>();
		List<Integer[]> releasePatterns = new ArrayList<Integer[]>();
		getReleasePatterns(s, set, releasePatterns, 0, new Integer[pr.length]);

		for (Integer[] rp : releasePatterns) {
			List<TaskState> taskStates = new ArrayList<TaskState>();
			int _gamma = s.gamma;
			for (int i = 0; i < rp.length; i++) {
				int alpha = (Sch(s, pr) == i) ? 1 : 0;
				int beta = (rp[i] > 0) ? 1 : 0;
				TaskState ts = s.taskStates.get(i);
				int _c = ts.c - alpha + rp[i];
				int _q = Math.max(ts.q - 1, 0) + beta * set.getTasks().get(i).getD();
				int _p = Math.max(ts.p - 1, 0) + beta * set.getTasks().get(i).getT();
				int _e = 0;
				if (beta == 0 && ts.c - alpha == 0) {
					_e = 0;
				} else if (beta == 0 && ts.c - alpha > 0) {
					_e = ts.e;
				} else {
					_e = rp[i];
				}
				if (s.gamma == 0 && anyHighCriticalityDidNotSignalCompletion(s, set, pr)) {
					_gamma = 1;
				}
				if (_gamma == 1 && set.getTasks().get(i).getL() == 0) {
					_c = _e = _p = _q = 0;
				}
				taskStates.add(new TaskState(_c, _q, _p, _e));
			}
			states.add(new MCState(0, _gamma, taskStates));
		}

		return states;
	}

	private static boolean anyHighCriticalityDidNotSignalCompletion(MCState s, MCTaskSet set, int[] pr) {

		int n = set.getTasks().size();

		for (int i = 0; i < n; i++) {
			MCTask task = set.getTasks().get(i);
			TaskState state = s.taskStates.get(i);

			int alpha = (Sch(s, pr) == i) ? 1 : 0;

			if (task.getL() == 1 && state.e > task.getWCET(0) && state.e - (state.c - alpha) == task.getWCET(0)) {
				return true;
			}
		}

		return false;
	}

	private static void getReleasePatterns(MCState s, MCTaskSet set, List<Integer[]> patterns, int depth,
			Integer[] current) {
		int n = set.getTasks().size();

		if (depth == n) {
			Integer[] dispose = new Integer[n];
			for (int i = 0; i < n; i++) {
				dispose[i] = current[i];
			}
			patterns.add(dispose);
			return;
		}

		MCTask task = set.getTasks().get(depth);

		if (s.taskStates.get(depth).p - 1 > 0 || s.gamma == 0 && task.getL() == 0) {
			current[depth] = 0;
			getReleasePatterns(s, set, patterns, depth + 1, current);
		} else {
			for (int i = 0; i < task.getWCET(task.getL()); i++) {
				current[depth] = i;
				getReleasePatterns(s, set, patterns, depth + 1, current);
			}
		}
	}

	private static int Sch(MCState s, int[] pr) {
		for (int i = 0; i < s.taskStates.size(); i++) {
			if (s.taskStates.get(i).c > 0 && hpi(s.taskStates, i, pr) == 0) {
				return i;
			}
		}
		return 0;
	}

	private static int hpi(List<TaskState> taskStates, int i, int[] pr) {
		int sum = 0;

		for (int j = 0; j < taskStates.size(); j++) {
			if (j != i && pr[j] > pr[i]) {
				sum += taskStates.get(j).c;
			}
		}

		return sum;
	}

}
