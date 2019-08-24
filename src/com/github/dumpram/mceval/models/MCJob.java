package com.github.dumpram.mceval.models;

public class MCJob {

	public int r;
	
	public int c;
	
	public int d;
	
	public int prio;
	
	public int task;

	public int[] C;
	
	public MCJob(int r, int d, int[] C, int task, int prio) {
		super();
		this.r = r;
		this.d = d;
		this.task = task;
		this.C = C;
		this.prio = prio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + prio;
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
		MCJob other = (MCJob) obj;
		if (task != other.task)
			return false;
		return true;
	}
	
	
}
