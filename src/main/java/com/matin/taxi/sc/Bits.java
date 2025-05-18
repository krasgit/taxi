package com.matin.taxi.sc;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

import jakarta.xml.bind.DatatypeConverter;

public class Bits {
	public static String dumb(BitSet bs) {
		String ret = "";

		for (int i = bs.length(); i >= 0; --i) {
			if (bs.get(i))
				ret += "<font color='red'>," + i + "</font>";
			else
				ret += "<font color='green'>," + i + "</font>";
		}
		return ret;
	}

	public static BitSet convert(long value) {
		BitSet bits = new BitSet();
		int index = 0;
		while (value != 0L) {
			if (value % 2L != 0) {
				bits.set(index);
			}
			++index;
			value = value >>> 1;
		}
		return bits;
	}

	public static long convert(BitSet bits) {
		long value = 0L;
		for (int i = 0; i < bits.length(); ++i) {
			value += bits.get(i) ? (1L << i) : 0L;
		}
		return value;
	}
	
	public static String hashNumber(String password) {
		String myHash;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] digest = md.digest();
			myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			myHash = e.getMessage();
		}

		return myHash;
	}

	
	public static String  getBS(BitSetMode bsm,String uid){
		String ret="";
		
		if (bsm != null) {

			long value = Bits.convert(bsm.bs);

			String hash = hashNumber("" + value);

			ret += "<div>"+uid+"<hr/>bs  : " + Bits.dumb(bsm.bs) + "<br/> value > " + value + "  hash:" + hash + "<br/> </div>";
		} else {
			ret += "<div>No Result</div>";
		}
		return ret;
	}
}