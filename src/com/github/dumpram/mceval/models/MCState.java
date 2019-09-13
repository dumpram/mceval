package com.github.dumpram.mceval.models;

import java.util.List;

public class MCState {
	
	public int gamma;
	
	public int t;
	
	public List<TaskState> taskStates;

	public int tardiness = 0;

	public MCState(int t, int _gamma, List<TaskState> taskStates) {
		gamma = _gamma;
		this.t = t;
		this.taskStates = taskStates;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + gamma;
		result = prime * result + t;
		result = prime * result + ((taskStates == null) ? 0 : taskStates.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCState other = (MCState) obj;
		if (gamma != other.gamma)
			return false;
		if (t != other.t)
			return false;
		if (taskStates == null) {
			if (other.taskStates != null)
				return false;
		} else if (!taskStates.equals(other.taskStates))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		String forExport = "";
		
		for (TaskState s : taskStates) {
			forExport += s.toString() + "\n";
		}
		
		forExport += (gamma == 0)? "LO" : "HI";
		
		return forExport;
	}
	

}
