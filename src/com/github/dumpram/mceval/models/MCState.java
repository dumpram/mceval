package com.github.dumpram.mceval.models;

import java.util.ArrayList;
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
	
	private static int id_cnt = 0;
	
	public boolean root = false;

	public MCState(int t, int _gamma, List<TaskState> taskStates) {
		id = id_cnt++;
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
	
	public String draw() {
		String forExport = "";
		
		forExport += defineText();
		
		forExport += drawTree(0);
		forExport += ";\n";
		
		forExport += drawPaths(0);
		
		forExport += drawPruning();
		
		return forExport;
	}
	
	private String drawPruning() {
		String forExport = "";
		
		String id = Character.toString(65 + this.id);
		
		if (pr != 0) {
			forExport += "\\node [below, text width=1cm, align=center] at (" + id + ".south) {\\Cross\\\\(PR"
					+ pr + ")};\n";
		} else {
			forExport += "\\node [draw, circle, above, align=center, ultra thick] at (" + id + ".north east)"
					+ "{" + sp + "};\n";
		}
		
		for (MCState child : successorStates) {
			forExport += child.drawPruning();
		}
		return forExport;
	}

	private String drawPaths(int i) {
		String forExport = "";
		
		String id = Character.toString(65 + this.id);
		
		for (MCState child : successorStates) {
			String childId = Character.toString(65 + child.id);
			forExport += "\\path [draw, ->, ultra thick] (" + id + ") edge node[left]{$(0, 0, 0)$}  (" + childId + ");\n";
			forExport += child.drawPaths(i);
		}
		return forExport;
	}

	private String defineText() {
		String forExport = "";
		
		String id = Character.toString(65 + this.id);
		
		String tasks = "";
		for (TaskState ts : taskStates) {
			tasks += ts.toString() + "\\\\";
		}
		
		forExport += "\\def\\nodetext" + id + "{" + tasks + "};\n";
		
		for (MCState child : successorStates) {
			forExport += child.defineText();
		}
		
		return forExport;
	}

	public String drawTree(int depth) {
		String forExport = "";
		String id = Character.toString(65 + this.id);
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
	

}
