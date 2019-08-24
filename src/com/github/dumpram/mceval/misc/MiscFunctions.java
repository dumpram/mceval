package com.github.dumpram.mceval.misc;

public class MiscFunctions {
	
	public static long gcd(long a, long b)
	{
	    while (b > 0)
	    {
	        long temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}

	public static long gcd(long[] input)
	{
	    long result = input[0];
	    for(int i = 1; i < input.length; i++) result = gcd(result, input[i]);
	    return result;
	}
	
	public static long lcm(long a, long b)
	{
	    return a * (b / gcd(a, b));
	}

	public static long lcm(int[] periods)
	{
	    long result = periods[0];
	    for(int i = 1; i < periods.length; i++) result = lcm(result, periods[i]);
	    return result;
	}

}
