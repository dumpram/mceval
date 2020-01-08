package com.github.dumpram.mceval.models;

import java.util.List;

import com.github.dumpram.mceval.misc.MiscFunctions;

public class MCTaskSet {
	
	private List<MCTask> tasks;
	
	private int[] periods;
	
	private long hyperperiod;
	
	private double averageWCET = 0;

	public MCTaskSet(List<MCTask> tasks) {
		super();
		this.tasks = tasks;
		periods = new int[tasks.size()];
		for (int i = 0; i < tasks.size(); i++) {
			periods[i] = tasks.get(i).getT();
		}
		
		int maxD = 0;
		for (int i = 0; i < tasks.size(); i++) {
			int tmp = tasks.get(i).getD(0);
			if (tmp > maxD) {
				maxD = tmp;
			}
			averageWCET += tasks.get(i).getWCET(1);
		}
		averageWCET /= tasks.size();
		this.hyperperiod = MiscFunctions.lcm(periods) + maxD;
	}

	public List<MCTask> getTasks() {
		return tasks;
	}
	
	public double getUtilizationLO() {
		double util = 0;
		
		for (MCTask task : tasks) {
			util += 1.0 * task.getWCET(0) / task.getT();
		}
		
		return util;
	}
	
	public double getUtilizationHIHI() {
		double util = 0;
		
		for (MCTask task : tasks) {
			if (task.getL() == 1) {
				util += 1.0 * task.getWCET(1) / task.getT();
			}
		}
		
		return util;
	}
	
	public double getUtilizationHILO() {
		double util = 0;
		
		for (MCTask task : tasks) {
			if (task.getL() == 1) {
				util += 1.0 * task.getWCET(0) / task.getT();
			}
		}
		
		return util;
	}
	
	public double getUtilizationLOLO() {
		double util = 0;
		
		for (MCTask task : tasks) {
			if (task.getL() == 0) {
				util += 1.0 * task.getWCET(0) / task.getT();
			}
		}
		
		return util;
	}
	
	public double getUtilizationLOHI() {
		double util = 0;
		
		for (MCTask task : tasks) {
			if (task.getL() == 0) {
				util += 1.0 * task.getWCET(1) / task.getT();
			}
		}
		
		return util;
	}
	
	public long getSchedulabilityBoundHI() {
		int[] periods = new int[tasks.size()];
		for (int i = 0; i < tasks.size(); i++) {
			periods[i] = tasks.get(i).getT();
		}
		
		long hyperperiod = MiscFunctions.lcm(periods);
		
		double u = getUtilizationHIHI();
		int max = 0;
		int maxD = 0;
		
		for (int i = 0; i < tasks.size(); i++) {
			int tmp = tasks.get(i).getT() - (tasks.get(i).getD(1) - tasks.get(i).getD(0));
			if (tmp > max) {
				max = tmp;
			}
			tmp = tasks.get(i).getD(1);
			if (tmp > maxD) {
				maxD = tmp;
			}
		}

		if (hyperperiod <= 0) {
			return (long)(max * u / (1 - u));
		} else {
			return Long.min(hyperperiod + maxD, (long)(max * u / (1 - u)));
		}
		
		
	}
	
	public long getSchedulabilityBoundLO() {
		int[] periods = new int[tasks.size()];
		for (int i = 0; i < tasks.size(); i++) {
			periods[i] = tasks.get(i).getT();
		}

		long hyperperiod = MiscFunctions.lcm(periods);
		
		double u = getUtilizationHIHI();
		int max = 0;
		int maxD = 0;
		for (int i = 0; i < tasks.size(); i++) {
			int tmp = tasks.get(i).getT() - tasks.get(i).getD(0);
			if (tmp > max) {
				max = tmp;
			}
			tmp = tasks.get(i).getD(0);
			if (tmp > maxD) {
				maxD = tmp;
			}
		}
		if (hyperperiod <= 0) {
			return (long)(max * u / (1 - u));
		} else {
			return Long.min(hyperperiod + maxD, (long)(max * u / (1 - u)));
		}
	}
	
	public long hyperperiod() {
		return hyperperiod;
	}
	
	public void reorder(int taskIndex, int priorityLevel) {
		MCTask tmp = tasks.get(taskIndex);
		tasks.set(taskIndex, tasks.get(priorityLevel));
		tasks.set(priorityLevel, tmp);
	}
	
	public double getAverageWCET() {
		return averageWCET;
	}
	
	@Override
	public String toString() {
		return tasks.toString();
	}
	
	public String export() {
		String export = "";
		
		for (MCTask task : tasks) {
			export += task.getWCET(0) + " " + task.getWCET(1) + " " + task.getT() + " " + task.getD(0) + " " + task.getD(1) + 
					" " + task.getL() + "\n";
		}
		
		return export;
	}

	public double getCP() {
		double sum = 0.0;
		for (MCTask task : tasks) {
			sum += (task.getL() == 1) ? 1 : 0;
		}
		return sum / tasks.size();
	}

	public int loCount() {
		int count = 0;
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getL() == 0) {
				count++;
			}
		}
		return count;
	}
	
	public int hiCount() {
		int count = 0;
		for (int i = 0; i < tasks.size(); i++) {
			if (tasks.get(i).getL() == 1) {
				count++;
			}
		}
		return count;
	}
}
