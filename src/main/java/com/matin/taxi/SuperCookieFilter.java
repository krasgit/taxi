package com.matin.taxi;

import java.io.*;
import java.security.*;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.xml.bind.DatatypeConverter;

public class SuperCookieFilter implements Filter {

	static String urlPatterns="/SuperCookie/*";
	
	static int version=2;
	static int nbits=32;
	public class Bits {

		  public static BitSet fromInt(int value) 
		  {
		    BitSet bitSet = new BitSet(32);
		    for(int i =0 ;i<32;i++)
		    {
		    	if((value&(1<<i))!=0)
		    		bitSet.set(i);
		    }
		    
		    return bitSet;
		  }
		  
		  public static int toInt(BitSet bitSet) 
		  {
			  int value=0;
		    
		    for(int i =0 ;i<32;i++)
		    {
		    	if(bitSet.get(i))
		    		value|=(1<<i);
		    	
		    }
		    
		    return value;
		  }
		  
		  public static BitSet invert(BitSet bitSet) 
		  {
			 
			  BitSet bitSetNew  = new BitSet(32);
		    for(int i =0 ;i<32;i++)
		    {
		    	if(!bitSet.get(i))
		    		bitSetNew.set(i);
		    	
		    }
		    
		    return bitSetNew;
		  }
		  
	}
		
	public class BitSetMode{
		
		static boolean READ=true;
		static boolean WRITE=false;
		public BitSet bs =null;
		public boolean mode;
		public BitSetMode(boolean mide) {
			super();
			this.mode = mide;
			bs =new BitSet();
		}
		
		public BitSetMode(boolean mide,int value) {
			super();
			this.mode = mide;
			bs= Bits.fromInt(value);
		
		}
		
	}

	private final Map<String, BitSetMode> visited = new HashMap<>();
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String ff = httpRequest.getRequestURI();
		String queryString = httpRequest.getQueryString();
	//	System.out.println("GET ==" + ff+  "qs "+queryString );
		
		 HttpServletResponse httpResponse=(HttpServletResponse)response;
		 
		if(ff.contains(".ico")) {
			
			 String uid = getCookieValue(httpRequest,"uid");
			 
			int index   =Integer.valueOf(queryString);
			 
			System.err.println("Get favicon.svg  uid "+uid +" qs"+index);

			BitSetMode bs = visited.get(uid);
			if(bs!=null) {
				
				if(bs.mode==BitSetMode.READ) {
					httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
					bs.bs.set(index);
				}
				else
				{
				if(	bs.bs.get(index)==true)
						sendFav( httpResponse);
				}
			
			}
			return;
		}
		
		if(ff.equals("/SuperCookie/result"))
		{
			 String uid = getCookieValue(httpRequest,"uid");
			 result(response,uid);
		}
		
		if(ff.equals("/SuperCookie/read"))
		{
			  String  uuid = UUID.randomUUID().toString();
			  visited.put(uuid, new BitSetMode(BitSetMode.READ));
			  httpResponse.addCookie(new Cookie("uid", uuid));
			  System.err.println("new Cookie uid "+uuid);
			
			  startRead(response);
			 return;
		}
		
		if(ff.equals("/SuperCookie/write"))
		{
			String  uuid = UUID.randomUUID().toString();
			  visited.put(uuid, new BitSetMode(BitSetMode.WRITE,396
					  
					  
					  
					  ));
			  httpResponse.addCookie(new Cookie("uid", uuid));
			  System.err.println("new Cookie uid "+uuid);
			
			  startRead(response);
		   return;
		}
	}

	
	void startRead( ServletResponse response) throws IOException{
	       
	      ServletOutputStream outputStream = response.getOutputStream();
			 PrintStream printStream = new PrintStream(outputStream);
			 printStream.print(read());
			 printStream.close();
	}
	
	
	private String getCookieValue(HttpServletRequest req, String cookieName) {
	    return Arrays.stream(req.getCookies())
	            .filter(c -> c.getName().equals(cookieName))
	            .findFirst()
	            .map(Cookie::getValue)
	            .orElse(null);
	}
	
    String read(){
		String ret="<!DOCTYPE html>\n"
				+ "<html>\n"
				+ "    <head>\n"
				+ "        <meta charset=\"utf-8\"/>\n"
				+ "        <title>supercookie • </title>\n"
				+ "        <meta name=\"robots\" content=\"noindex\"/>\n"
				+ "        <meta name=\"viewport\" content=\"viewport-fit=cover, user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1\">\n"
				+ "    </head>\n"
				+ "    <body>\n"
				+ "        <h1>"+version+"</h1>\n"
				+ "        <script type=\"module\">\n"
				+ "   var version="+version+";  \n"
				+ "   var index=0;  \n"
				+ "            const favicon = document.createElement('link');\n"
				+ "            favicon.rel = \"icon preload\";\n"
				+ "            favicon.as = \"image\";\n"
				+ "            favicon.type = \"image/x-icon\";\n"
	            + "            favicon.href = location.origin+'/SuperCookie/favicon.ico?'+index;\n"
				+"function onEvent() {\n"
				+ "		index++;\n"
				+ "		if(index>="+nbits+"){"
				+ "      window.location.href = location.origin+'/SuperCookie/result';"
				+ "       return; "
				+ "}\n"
				+ "   favicon.href = location.origin+'/SuperCookie/"+version+"/favicon.ico?'+index;\n"
				+ "};  "
				+ ""
				+ "  favicon.onerror = function(){ onEvent() ;};"
				+ "  favicon.onload = function(){onEvent() ;};"
				+ "            document.head.appendChild(favicon);\n"
				+ "        </script>\n"
				+ "    </body>\n"
				+ "</html>";
		return ret;
	}
    
    /*
    private void sendFav(HttpServletResponse response) throws IOException {
		String str="<?xml version=\"1.0\" encoding=\"utf-8\"?><!-- Uploaded to: SVG Repo, www.svgrepo.com, Generator: SVG Repo Mixer Tools -->\n"
				+ "<svg width=\"800px\" height=\"800px\" viewBox=\"0 0 16 16\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
				+ "<path d=\"M5.6906 6.00001L3.16512 1.62576C4.50811 0.605527 6.18334 0 8 0C8.37684 0 8.74759 0.0260554 9.11056 0.076463L5.6906 6.00001Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M5.11325 9L1.69363 3.07705C0.632438 4.43453 0 6.14341 0 8C0 8.33866 0.0210434 8.67241 0.0618939 9H5.11325Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M4.89635 15.3757C2.93947 14.5512 1.37925 12.9707 0.581517 11H7.42265L4.89635 15.3757Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M8 16C7.62316 16 7.25241 15.9739 6.88944 15.9235L10.3094 10L12.8349 14.3742C11.4919 15.3945 9.81666 16 8 16Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M16 8C16 9.85659 15.3676 11.5655 14.3064 12.9229L10.8868 7H15.9381C15.979 7.32759 16 7.66134 16 8Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M11.1036 0.624326C13.0605 1.44877 14.6208 3.02927 15.4185 5H8.57735L11.1036 0.624326Z\" fill=\"#000000\"/>\n"
				+ "</svg>";
		
		response.setContentType("image/svg+xml");
		
		response.addHeader("Cache-Control", "max-age=2592000");
		response.addHeader("Expires", "Tue, 03 Jul 2031 06:00:00 GMT");
		response.addHeader("Content-Type", "image/svg+xml");
		response.addHeader("Last-Modified","Tue, 03 Jul 2030 06:00:00 GMT");
		
		ServletOutputStream outputStream = response.getOutputStream();
		 PrintStream printStream = new PrintStream(outputStream);
		 printStream.print(str);
		 printStream.close();
	}
    */
    
    
    
    
    private void sendFav(HttpServletResponse response) throws IOException {
    	 String FILE = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII=";
		
    	 byte[] decodedBytes = Base64.getDecoder().decode(FILE);
    	 String decodedString = new String(decodedBytes);
    	 
		response.setContentType("image/svg+xml");
		
		response.addHeader("Cache-Control", "max-age=2592000");
		response.addHeader("Expires", "Tue, 03 Jul 2031 06:00:00 GMT");
		response.addHeader("Content-Type", "image/png");
		response.addHeader("Last-Modified","Tue, 03 Jul 2030 06:00:00 GMT");
		
		response.addHeader("Content-Length",""+decodedString.length());
		ServletOutputStream outputStream = response.getOutputStream();
		 PrintStream printStream = new PrintStream(outputStream);
		 printStream.print(decodedString);
		 printStream.close();
	}
    
    public String  hashNumber(String password) 	  throws NoSuchAlgorithmException {
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(password.getBytes());
	    byte[] digest = md.digest();
	    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
	    return myHash;
	}
    
    void result(ServletResponse response,String uid) throws IOException {
		
		 BitSetMode bs = visited.get(uid);
		
   
   ServletOutputStream outputStream = response.getOutputStream();
	 PrintStream printStream = new PrintStream(outputStream);
	 String hash=null;
		
		
		
		BitSet res=Bits.invert(bs.bs);
		
		int value=Bits.toInt(res);
		try {
		 hash=hashNumber(""+value);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	String ret=	"<div>bs  : " + bs.bs + "<br/> value > "+value + " <br/> "+hash+"</div>";
		ret+="<div> <a href='read'>Read</a>  <a href='write'>write</a> </div>";
	 printStream.print(ret);
	 printStream.close();
	 return;
	}
}
