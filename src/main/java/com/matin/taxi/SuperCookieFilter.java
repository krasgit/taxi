package com.matin.taxi;

import java.io.*;
import java.security.*;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.xml.bind.DatatypeConverter;

public class SuperCookieFilter implements Filter {

	static String urlPatterns = "/SuperCookie/*";

	static int version = 8;
	
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
	}

	public class BitSetMode {

		static boolean READ = true;
		static boolean WRITE = false;
		public BitSet bs = null;
		public boolean mode;

		public BitSetMode(boolean mide) {
			super();
			this.mode = mide;
			bs = new BitSet();
			bs.set(0, 31);
		}

		public BitSetMode(boolean mide, long value) {
			super();
			this.mode = mide;
			bs = Bits.convert(value);

		}

	}

	private final Map<String, BitSetMode> visited = new HashMap<>();

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String ff = httpRequest.getRequestURI();
		String queryString = httpRequest.getQueryString();
		// System.out.println("GET ==" + ff+ "qs "+queryString );

		HttpServletResponse httpResponse = (HttpServletResponse) response;

		if (ff.contains(".ico")) {

			String uid = getCookieValue(httpRequest, "uid");

			try {
				int index = Integer.valueOf(queryString);

				System.err.println("Get favicon.svg  uid " + uid + " qs" + index);

				BitSetMode bs = visited.get(uid);
				if (bs != null) {
					if (bs.mode == BitSetMode.READ) {
						httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
						bs.bs.clear(index); 
					} else {
					if (bs.bs.get(index) == true)
						sendFav(httpResponse);
				}

			}
			}
			catch (Exception e)		{
			
			}
			return;
		}

		if (ff.equals("/SuperCookie/result")) {
			String uid = getCookieValue(httpRequest, "uid");
			result(response, uid);
		}

		if (ff.equals("/SuperCookie/read")) {
			String uuid = UUID.randomUUID().toString();
			visited.put(uuid, new BitSetMode(BitSetMode.READ));
			httpResponse.addCookie(new Cookie("uid", uuid));
			System.err.println("new Cookie uid " + uuid);

			startRead(response);
			return;
		}

		if (ff.equals("/SuperCookie/write")) {
			String uuid = UUID.randomUUID().toString();
			visited.put(uuid, new BitSetMode(BitSetMode.WRITE, 3));
			httpResponse.addCookie(new Cookie("uid", uuid));
			System.err.println("new Cookie uid " + uuid);

			startRead(response);
			return;
		}
	}

	void startRead(ServletResponse response) throws IOException {

		ServletOutputStream outputStream = response.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		printStream.print(read().replace("%version%", ""+version));
		printStream.close();
	}

	private String getCookieValue(HttpServletRequest req, String cookieName) {
		return Arrays.stream(req.getCookies()).filter(c -> c.getName().equals(cookieName)).findFirst()
				.map(Cookie::getValue).orElse(null);
	}

	public String read() {return """ 
      <!DOCTYPE html>
        <html>
		  <body>
           <h1>%version%</h1>
        
     <script type="module">
        var version=%version%;  
        
        var index=0;  
        const favicon = document.createElement('link');
        favicon.rel = "icon preload";
        favicon.as = "image";
        favicon.type = "image/x-icon";
        favicon.href = location.origin+'/SuperCookie/favicon.ico';
     
        function onEvent() {
	     if(index>=32){
            window.location.href = location.origin+'/SuperCookie/result';       
            return;
           }
	
    favicon.href = location.origin+'/SuperCookie/%version%/favicon.ico?'+index;
    index++;
    };  
  
  favicon.onerror = function(){ 
			onEvent() ;
			};
  
  favicon.onload = function(){
			onEvent() ;
		};
  
  document.head.appendChild(favicon);
    </script>
    </body></html>""";
	}
	

	private void sendFav(HttpServletResponse response) throws IOException {
		String FILE = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII=";

		byte[] decodedBytes = Base64.getDecoder().decode(FILE);
		String decodedString = new String(decodedBytes);

		response.setContentType("image/svg+xml");

		response.addHeader("Cache-Control", "max-age=2592000");
		response.addHeader("Expires", "Tue, 03 Jul 2031 06:00:00 GMT");
		response.addHeader("Content-Type", "image/png");
		response.addHeader("Last-Modified", "Tue, 03 Jul 2030 06:00:00 GMT");

		response.addHeader("Content-Length", "" + decodedString.length());
		ServletOutputStream outputStream = response.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		printStream.print(decodedString);
		printStream.close();
	}

	public String hashNumber(String password)  {
		String myHash;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte[] digest = md.digest();
			 myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
		} catch (NoSuchAlgorithmException e) {
			myHash=e.getMessage();
		}
		
		return myHash;
	}

	void result(ServletResponse response, String uid) throws IOException {

		ServletOutputStream outputStream = response.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		BitSetMode bs = visited.get(uid);
		
		String ret="";
		if(bs!=null)
		{
		
		long value = Bits.convert(bs.bs);
		   
			  String hash = hashNumber(""+value);
		 ret+= "<div>bs  : " + Bits.dumb(bs.bs) + "<br/> value > " + value +"  hash:"+hash+"<br/> </div>";
		}else {
			 ret+= "<div>No Result</div>";
		}
		ret += "<div> <a href='read'>Read</a>  <a href='write'>Write</a> </div>";
		printStream.print(ret);
		printStream.close();
		return;
	}

}
