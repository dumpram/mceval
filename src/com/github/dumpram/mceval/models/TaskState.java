package com.github.dumpram.mceval.models;

public class TaskState {
	
	public int c;
	
	public int q;
	
	public int p;
	
	public int e;
	
	public int phi;

	public TaskState(int i, int j, int k, int l) {
		c = i;
		q = j;
		p = k;
		e = l;
		phi = 0;
	}
	
	public TaskState(int i, int j, int k, int l, int m) {
		c = i;
		q = j;
		p = k;
		e = l;
		phi = m;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + c;
		result = prime * result + e;
		result = prime * result + p;
		result = prime * result + q;
		result = prime * result + phi;
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
		TaskState other = (TaskState) obj;
		if (c != other.c)
			return false;
		if (e != other.e)
			return false;
		if (p != other.p)
			return false;
		if (q != other.q)
			return false;
		if (phi != other.phi)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "(" + c + ", " + q + ", " + p + ", " + e + ", " + phi + ")";
	}

}
