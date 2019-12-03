package com.github.dumpram.mceval.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MCState {
	
	public int gamma;
	
	public int t;
	
	public List<TaskState> taskStates;

	public int tardiness = 0;
	
	public int current = -1;
	
	public List<MCState> successorStates = new ArrayList<MCState>();
	
	public List<Integer[]> releasePatterns = new ArrayList<Integer[]>();
	
	public int pr = 0;
	
	public int sp = 0;
	
	public int id = 0;
	
	public static int id_cnt = 0;
	
	public boolean root = false;
	
	public boolean success = false;
	
	public boolean fail = false;

	public MCState(int t, int gamma, List<TaskState> taskStates) {
		this(t, gamma, taskStates, null);
	}

	public MCState(int t, int gamma, List<TaskState> taskStates, Integer[] rp) {
		if (rp != null) {
			releasePatterns.add(rp);
		}
		id = id_cnt++;
		this.gamma = gamma;
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
	
	public String draw() {
		String forExport = "";
		
		forExport += defineText();
		
		forExport += drawTree(0);
		forExport += ";\n";
		
		forExport += drawPaths();
		
		forExport += drawPruning();
		
		return forExport;
	}
	
	private String drawPruning() {
		String forExport = "";
		
		String id = getId();
		
		if (pr != 0) {
			forExport += "\\node [prune_style] at (" + id + ".south) {\\Cross\\\\(PR"
					+ pr + ")};\n";
		} else {
			String _sp = (sp == 0)? "U" : "" + sp;
			forExport += "\\node [enumerate_style] at (" + id + ".north east)"
					+ "{" + _sp + "};\n";
		}
		
		for (MCState child : successorStates) {
			forExport += child.drawPruning();
		}
		return forExport;
	}

	private String drawPaths() {
		String forExport = "";
		
		String id = getId();
		
		

		for (MCState child : successorStates) {
			String childId = child.getId();
			String rps = "";
			for (int i = 0; i < child.releasePatterns.size(); i++) {
				Integer[] rp = child.releasePatterns.get(i);
				String r = Arrays.toString(rp).replace("[", "(").replace("]", ")");
				rps += "$" + r + "$";
				if (i < child.releasePatterns.size() - 1) {
					rps += "\\\\";
				}
			}
			
			forExport += "\\path [draw, ->, ultra thick] (" + id + ") edge node[path_style]{" + rps + "}  (" + childId + ");\n";
			forExport += child.drawPaths();
		}
		return forExport;
	}

	private String defineText() {
		String forExport = "";
		
		String id = getId();
		
		String tasks = "";
		for (TaskState ts : taskStates) {
			tasks += ts.toString() + "\\\\";
		}
		
		tasks += (gamma == 0)? "LO" : "HI";
		
		forExport += "\\def\\nodetext" + id + "{" + tasks + "};\n";
		
		for (MCState child : successorStates) {
			forExport += child.defineText();
		}
		
		return forExport;
	}

	public String drawTree(int depth) {
		String forExport = "";
		String id = getId();
		boolean pruned = pr > 0;
		String style = (pruned) ? "state" : "state_s";
		
		if (root) {
			forExport += "\\node[" + style + "] (" + id + ") {\\nodetext" + id + "}\n";
		} else {
			forExport += "child {node [" + style + "] (" + id + ") {\\nodetext" + id +"}\n";
		}
		String ident = "";
		for (int i = 0; i < depth + 1; i++) {
			ident += "\t";
		}
		for (MCState child : successorStates) {
			
			forExport += ident + child.drawTree(depth + 1);
		}
		if (!root) {
			forExport += "}";
		}
		
		return forExport;
	}
	
	private String getId() {
		String forExport = "";
		
		int count = id / 26;
		int offset = id % 26;
		
		for (int i = 0; i < count + 1; i++) {
			forExport += Character.toString(65 + offset);
		}
		
		return forExport;
		
	}
	

}
