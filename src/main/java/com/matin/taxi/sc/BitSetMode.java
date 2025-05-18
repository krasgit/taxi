package com.matin.taxi.sc;

import java.util.BitSet;

public class BitSetMode {

	static boolean READ = true;
	static boolean WRITE = false;
	public BitSet bs = null;
	public boolean mode;

	

	public BitSetMode(boolean mide, long value) {
		super();
		this.mode = mide;
		bs = Bits.convert(value);

	}

}