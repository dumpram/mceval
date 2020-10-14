package com.github.dumpram.mceval.models;

import java.util.Arrays;
import java.util.List;

public class MCTask {
	
	private int[] C;
	
	private int T;
	
	private int[] D;
	
	private int L;

	public MCTask(int[] c, int t, int d, int l) {
		super();
		C = c;
		T = t;
		D = new int[c.length];
		for (int i = 0; i < D.length; i++) {
			D[i] = d;
		}
		L = l;
	}
	
	public MCTask(int[] c, int t, int d[], int l) {
		super();
		C = c;
		T = t;
		D = d;
		L = l;
	}

	public MCTask(List<Integer> C, Integer t, Integer d, Integer l) {
		this.C = new int[C.size()];
		
		for (int i = 0; i < this.C.length; i++) {
			this.C[i] = C.get(i);
		}
		
		this.T = t;
		D = new int[C.size()];
		for (int i = 0; i < C.size(); i++) {
			D[i] = d;
		}
		D[D.length - 1] = d;
		this.L = l;
	}

	public MCTask(String string) {
		String[] params = string.split(" ");
		
		C = new int[2];
		C[0] = Integer.parseInt(params[0]);
		C[1] = Integer.parseInt(params[1]);
		T = Integer.parseInt(params[2]);
		D = new int[2];
		D[0] = Integer.parseInt(params[3]);
		D[1] = Integer.parseInt(params[4]);
		L = Integer.parseInt(params[5]);
	}

	public int getWCET(int level) {
		return C[level];
	}

	public int getT() {
		return T;
	}

	public int getD() {
		return D[D.length - 1];
	}
	
	public int getD(int l) {
		return D[l];
	}
	
	public void setD(int l, int val) {
		D[l] = val;
	}

	public int getL() {
		return L;
	}
	
	@Override
	public String toString() {
		return "[" + Arrays.toString(C) + ", " + T + ", " + Arrays.toString(D) + ", " + L + "]"; 
	}

	public int[] getWCET() {
		return C;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(C);
		result = prime * result + Arrays.hashCode(D);
		result = prime * result + L;
		result = prime * result + T;
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
		MCTask other = (MCTask) obj;
		if (!Arrays.equals(C, other.C))
			return false;
		if (!Arrays.equals(D, other.D))
			return false;
		if (L != other.L)
			return false;
		if (T != other.T)
			return false;
		return true;
	}
	
	
}
