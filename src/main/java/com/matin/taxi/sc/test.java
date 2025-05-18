package com.matin.taxi.sc;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class test {

	
	private final static Map<String, BitSetMode> visited = new HashMap<>();
	
	public static void main(String[] args) {

		
		
		String  ref="1";
		
		visited.put(ref, new BitSetMode(BitSetMode.READ,0));
		
		BitSetMode bs = visited.get(ref);
	
		//bs.bs.set(0);
		bs.bs.set(0);
		bs.bs.set(1);
		bs.bs.set(2);
		dumb(bs.bs);

		

		
	
		
	}

	public static String dumb(BitSet bs) {
		String ret = "";
		long value = Bits.convert(bs);
		
		System.out.print(" "+value+" "); 
		for (int i = bs.length(); i >= 0; --i) {
			if (bs.get(i))
				System.err.print(","+i);
			else
				System.out.print(","+i);
		}
		return ret;
	}
	
}
