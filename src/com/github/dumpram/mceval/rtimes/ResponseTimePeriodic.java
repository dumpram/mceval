package com.github.dumpram.mceval.rtimes;

import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCState;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;
import com.github.dumpram.mceval.models.TaskState;

public class ResponseTimePeriodic implements IResponseTime {

	private IResponseTime responseTimeHI = new ResponseTimeHI();

	private IResponseTime responseTimeLO = new ResponseTimeLO();

	@Override
	public int responseTime(int i, MCTaskSet set) {
		List<MCTask> tasks = set.getTasks();

		int Rlo = responseTimeLO.responseTime(i, set);

		if (Rlo > tasks.get(i).getD() || tasks.get(i).getL() == 0) {
			return Rlo;
		}

		int Rmax = Integer.max(Rlo, responseTimeHI.responseTime(i, set));

		if (Rmax > tasks.get(i).getD()) {
			return Rmax;
		}

		List<TaskState> initialTaskStates = new ArrayList<TaskState>();

		for (int j = 0; j < i + 1; j++) {
			initialTaskStates.add(new TaskState(tasks.get(j).getWCET(0), tasks.get(j).getT(), tasks.get(j).getD(), 0));
		}

		MCState initialState = new MCState(0, 0, initialTaskStates);

		List<MCState> unexploredStates = new ArrayList<MCState>();

		unexploredStates.add(initialState);

		while (!unexploredStates.isEmpty()) {

			MCState state = unexploredStates.get(unexploredStates.size() - 1);
			unexploredStates.remove(state);
			List<TaskState> taskStates = state.taskStates;

			while (state.t <= tasks.get(i).getD() && taskStates.get(i).c > 0) {
				int hp = getHighestPriorityTask(state);
				taskStates.get(hp).c--;
				for (int j = 0; j < i + 1; j++) {
					taskStates.get(j).p--;
					taskStates.get(j).q--;
				}

				int gamma = 0;
				if (taskStates.get(hp).c == 0 && tasks.get(hp).getL() == 1 && state.gamma == 0) {
					gamma = 1;
				}
				
				state.t++;
				for (int j = 0; j < i; j++) {
					if (taskStates.get(j).p == 0) {
						taskStates.get(j).p = tasks.get(j).getT();
						taskStates.get(j).q = tasks.get(j).getD();
						if (state.gamma == 1) {
							if (tasks.get(j).getL() == 1) {
								taskStates.get(j).c = tasks.get(j).getWCET(1);
							} else {
								taskStates.get(j).c = 0;
							}
						} else {
							taskStates.get(j).c = tasks.get(j).getWCET(0);
						}
					}
				}

				if (gamma == 1) {
					unexploredStates.add(new MCState(state.t, 1, getSwitchStates(taskStates, tasks, hp)));
				}
			}
			if (state.t > Rmax) {
				Rmax = state.t;
			}

		}

		return Rmax;

	}

	private List<TaskState> getSwitchStates(List<TaskState> taskStates, List<MCTask> tasks, int hp) {
		List<TaskState> switchStates = new ArrayList<TaskState>();

		for (int i = 0; i < taskStates.size(); i++) {
			TaskState ts = taskStates.get(i);
			MCTask task = tasks.get(i);
			if (task.getL() == 0) {
				switchStates.add(new TaskState(0, 0, 0, 0));
			} else {
				int c = (i == hp || ts.c > 0) ? ts.c + task.getWCET(1) - task.getWCET(0) : 0;
				switchStates.add(new TaskState(c, ts.p, ts.q, 0));
			}
		}

		return switchStates;
	}

	private int getHighestPriorityTask(MCState state) {
		List<TaskState> states = state.taskStates;

		for (int i = 0; i < states.size(); i++) {
			if (states.get(i).c > 0) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public String toString() {
		return "ExactPeriodic";
	}
}
