package com.github.dumpram.mceval.interfaces;

import java.util.HashMap;

import com.github.dumpram.mceval.models.MCTaskSet;

public interface IResponseTime {

	int responseTime(int i, MCTaskSet set);

	String printResponseTime(int i, HashMap<Integer, Integer> priorityOrder, MCTaskSet orderedSet);
	
}
