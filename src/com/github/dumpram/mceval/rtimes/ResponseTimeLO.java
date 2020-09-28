package com.github.dumpram.mceval.rtimes;

import java.util.HashMap;
import java.util.List;

import com.github.dumpram.mceval.interfaces.IResponseTime;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class ResponseTimeLO implements IResponseTime {

	@Override
	public int responseTime(int i, MCTaskSet set) {
		List<MCTask> tasks = set.getTasks();
		MCTask task = tasks.get(i);
		int R = 0;
		int C = task.getWCET(0);
		int D = task.getD();
		int t = C;

		while (R != t && R <= D) {
			R = t;
			t = C;
			for (int j = 0; j < i; j++) {
				MCTask taskJ = tasks.get(j);
				t = (int) (t + (int) Math.ceil((double) R / taskJ.getT()) * taskJ.getWCET(0));
			}
		}

		return R;
	}

	@Override
	public String printResponseTime(int i, HashMap<Integer, Integer> priorityOrder, MCTaskSet orderedSet) {
		// TODO Auto-generated method stub
		return null;
	}
}
