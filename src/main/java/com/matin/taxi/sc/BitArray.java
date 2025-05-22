package com.matin.taxi.sc;


public class BitArray {

   
    private static final int WORD_SIZE = 32;
    private boolean bits[] = null;

    public BitArray() {
        bits = new boolean [WORD_SIZE];
    }

    public BitArray(long value) {
    	bits = new boolean [WORD_SIZE];
    	init(value);
	}

	public boolean get(int pos) {
        return bits[pos];
    }

    public void setBit(int pos, boolean b) {
    	
    	
    	bits[pos] = b;
    	
        
    }

    public  void init(long value) {
    
		int index = 0;
		while (value != 0L) {
			if (value % 2L != 0) {
				setBit(index,true);
			}
			++index;
			value = value >>> 1;
		}
		
	}

    public  long convert() {
		long value = 0L;
		for (int i = 0; i < bits.length; ++i) {
			value += bits[i] ? (1L << i) : 0L;
		}
		return value;
	}
    
    
    
    
    public void  revert() {
		
		for (int i = 0; i < bits.length; ++i) {
			
			bits[i]=!bits[i];
		
			
		}
		
		
	}
    
    public void  revert(int WRITES) {
		
  		for (int i = 0; i < WRITES; ++i) {
  			
  			bits[i]=!bits[i];
  		
  			
  		}
  		
  		
  	}
    
    
public String   dumpbin() {
		
	String ret="value: " +this.convert()+"    ";
	
	
		for (int i =bits.length-1;  i>=0; --i) {
			
		boolean 	b=bits[i];
		
		
			if (b)
				ret+="1";
			else
				ret+="0";
		}
		return ret;
	}
    
    public void  dump() {
		
		for (int i = 0; i < bits.length; ++i) {
			
		boolean 	b=bits[i];
		
			if (b)
				System.err.print(","+i);
			else
				System.out.print(","+i);
		}
		
		System.out.println();
	}
    
    
public static void main(String[] args) {

		
		long value=0;
		BitArray ba=new BitArray(value);
	
		ba.setBit(2, true);
		//ba.revert();
		
	//	ba.init(value);
		
	//	ba.setBit(0, !false);
		
		String ret=ba.dumpbin(); 
		
		System.out.println(ret);
		
		ba.revert(4);
		
		 ret=ba.dumpbin();

		 System.out.println(ret);
		
	}
    
}
