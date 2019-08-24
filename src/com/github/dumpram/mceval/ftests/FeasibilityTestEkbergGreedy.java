package com.github.dumpram.mceval.ftests;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.github.dumpram.mceval.interfaces.IFeasibilityTest;
import com.github.dumpram.mceval.models.MCTask;
import com.github.dumpram.mceval.models.MCTaskSet;

public class FeasibilityTestEkbergGreedy implements IFeasibilityTest {
	
	
	public boolean isSetCorrected(MCTaskSet set) {
		long lmax = getLmax(set);
		int mod = -1;
		List<MCTask> tasks = set.getTasks();
		Set<Integer> candidates = getCandidates(set);
		
		//int k = 0;
		
		while (true) {
			boolean end = true;
			//System.out.println("Step: " + k);
			lmax = getLmax(set);
			//System.out.println(lmax);
			if (lmax < 0) {
				System.out.println("Lmax error");
				return false;
			}
			for (int l = 0; l <= lmax; l++) {
				//System.out.println("l = " + l);
				if (!condA(set, l)) {
					if (mod == -1) {
						//System.out.println("Fails at A");
						return false;
					}
					//System.out.println("l = " + l + " condA reverses change");
					tasks.get(mod).setD(0, tasks.get(mod).getD(0) + 1);
					candidates.remove(mod);
					mod = -1;
					end = false;
					break;
				} else if (!condB(set, l)) {
					if (candidates.isEmpty()) {
						return false;
					}
					mod = maxDBF(set, l, candidates);
					tasks.get(mod).setD(0, tasks.get(mod).getD(0) - 1);
					//System.out.println("l = " + l + " condB makes change to " + mod);
					if (tasks.get(mod).getD(0) == tasks.get(mod).getWCET(0)) {
						candidates.remove(mod);
						//System.out.println("l = " + l + " condB removes " + mod);
					}
					end = false;
					break;
				}
			}
			
			if (end) {
				return true;
			}
			//k++;
		}
	}

	private boolean condA(MCTaskSet set, int l) {
		int demand = 0;
		int n = set.getTasks().size();
		
		for (int i = 0; i < n; i++) {
			demand += dbfLO(i, set, l);
		}
		return demand <= l;
	}

	private boolean condB(MCTaskSet set, int l) {
		int demand = 0;
		int n = set.getTasks().size();
		
		for (int i = 0; i < n; i++) {
			demand += dbfHI(i, set, l);
		}
		
		return demand <= l;
	}

	private int maxDBF(MCTaskSet set, int l, Set<Integer> candidates) {
		List<MCTask> tasks = set.getTasks();
		int idx = -1;
		int max = 0;
		int n = tasks.size();
		
		for (int i = 0; i < n; i++) {
			if (candidates.contains(i)) {
				if (idx == -1) {
					idx = i;
					max = dbfHI(i, set, l) - dbfHI(i, set, l - 1);
					continue;
				}
				int tmp = dbfHI(i, set, l) - dbfHI(i, set, l - 1);
				if (tmp > max) {
					max = tmp;
					idx = i;
				}
			}
		}
		
		return idx;
	}
	
	private int dbfHI(int idx, MCTaskSet set, int l) {
		MCTask i = set.getTasks().get(idx);
		int full = 0;
		int done = 0;
		
		if (i.getL() == 1) {
			full += Integer.max(Math.floorDiv(l - (i.getD(1) - i.getD(0)), i.getT()) + 1, 
					0) * i.getWCET(1);
			int n = l % i.getT();
			if (n < i.getD(1) && n >= (i.getD(1) - i.getD(0))) {
				done += Integer.max(i.getWCET(0) - (n - (i.getD(1) - i.getD(0))), 0);
			}
		}
		
		return full - done;
	}
	
	private int dbfLO(int idx, MCTaskSet set, int l) {
		MCTask i = set.getTasks().get(idx);
		return Integer.max(Math.floorDiv(l - i.getD(0), i.getT()) + 1, 0) * i.getWCET(0);
	}
	

	private Set<Integer> getCandidates(MCTaskSet set) {
		HashSet<Integer> candidates = new HashSet<Integer>();
		int n = set.getTasks().size();
		
		for (int i = 0; i < n; i++) {
			if (set.getTasks().get(i).getL() == 1) {
				candidates.add(i);
			}
		}
		
		return candidates;
	}

	private long getLmax(MCTaskSet set) {
		return Long.max(set.getSchedulabilityBoundHI(), set.getSchedulabilityBoundLO());
	}

	@Override
	public boolean isFeasible(MCTaskSet set) {
		return isSetCorrected(set);
	}

	@Override
	public String toString() {
		return "EkbergGreedy";
	}
}
