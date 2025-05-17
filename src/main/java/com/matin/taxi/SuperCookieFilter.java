package com.matin.taxi;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.*;
import java.util.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.xml.bind.DatatypeConverter;


import org.apache.batik.transcoder.Transcoder;

import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;




public class SuperCookieFilter implements Filter {

	private static final long VALUE = 1;
	static int version = 18;
	static String urlPatterns = "/SuperCookie/*";

	
	
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
		 System.out.println("GET ==" + ff+ " qs "+queryString );

		
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		/*
		if (ff.contains("favicon.png")) {
			//connectRelay("https://localhost:8443/resources/images/favicon.png",httpResponse);
			CustomServletRequest customRequest = new CustomServletRequest(httpRequest);
            String reqURI = httpRequest.getRequestURI();
            String newURI = "/resources/images/favicon.png";
            customRequest.setRequestURI(newURI);
			 chain.doFilter(request, response);
			return;
		}
		*/
		if (ff.contains(".ico")) {

			String uid = getCookieValue(httpRequest, "uid");

			try {
				String ss=ff.replace("/SuperCookie/"+version+"/", "");
				
				String ind=ss.replace("/favicon.ico", "");
				
				int index = Integer.valueOf(ind);

				System.err.println("Get favicon.svg  uid " + uid + " qs" + index);

				BitSetMode bs = visited.get(uid);
 				if (bs != null) {
					if (bs.mode == BitSetMode.READ) {
						httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
						bs.bs.clear(index); 
					} else {
					if (bs.bs.get(index) == true) {
						//sendFav(httpResponse);
						{
							System.err.println("send  favicon.svg  uid " + uid + " qs" + index);
							CustomServletRequest customRequest = new CustomServletRequest(httpRequest);
				            String reqURI = httpRequest.getRequestURI();
				            String newURI = "/resources/images/favicon.png";
				            customRequest.setRequestURI(newURI);
							 chain.doFilter(request, response);
							
						}
						
						return;
					}
					else 
						httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
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

			//startRead(response);
			startWrite(response,0);
			return;
		}

		if(ff.startsWith("/SuperCookie/write")) {
			String ind=ff.replace("/SuperCookie/write/", "");
			
			try {
			int index = Integer.valueOf(ind);
			
			if(index==2)
			{
				String uid = getCookieValue(httpRequest, "uid");
				result( response,  uid);
				return ;
			}	
				
			startWrite(response,++index);
			} catch (Exception e) {
				String uuid = UUID.randomUUID().toString();
				visited.put(uuid, new BitSetMode(BitSetMode.WRITE, VALUE));
				httpResponse.addCookie(new Cookie("uid", uuid));
				System.err.println("new Cookie uid " + uuid);

				startWrite(response,0);
				return;
			}
			
		}
		/*
		if (ff.equals("/SuperCookie/write")) {
			String uuid = UUID.randomUUID().toString();
			visited.put(uuid, new BitSetMode(BitSetMode.WRITE, 11));
			httpResponse.addCookie(new Cookie("uid", uuid));
			System.err.println("new Cookie uid " + uuid);

			startWrite(response,0);
			return;
		}
		*/
	}

	void startWrite(ServletResponse response,int index) throws IOException {
		ServletOutputStream outputStream = response.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		String write=write1().replace("%version%", ""+version).replace("%index%", ""+index);
		printStream.print(write);
		printStream.close();
	
	}
	
	
	public String write() {
		return """

 <!DOCTYPE html>
<html>
<head>
  <title>My Page Title</title>
   <link rel="icon" type="image/x-icon" href="/SuperCookie/favicon.png">
</head>
<body>

<h1>This is a Heading</h1>
<p>This is a paragraph.</p>

</body>
</html> 

				
				""";
		
	}
	
	public String write1() {return """ 
		 <!DOCTYPE html>
			<html>
			<head>
			<title>My Page Title</title>
			<link rel="shortcut icon"  type="image/x-icon" href="/SuperCookie/%version%/%index%/favicon.ico">
			</head>
			<body>


</body>

   <script type="module">
   
    
            console.info("%version%");

            window.addEventListener("DOMContentLoaded", () => {
                setTimeout(()=>{
                    document.location.href = `${document.location.origin}`+"/SuperCookie/write/%index%";
                }, 5001);
            });

        </script>
   
   
  </script>
</html> 
		  """;
		  		    };  
	
	
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
  function sleep(num) {
        let now = new Date();
        let stop = now.getTime() + num;
        while(true) {
            now = new Date();
            if(now.getTime() > stop) return;
        }
    }
        var version=%version%;  
        
        var index=0;  
        const favicon = document.createElement('link');

        favicon.rel = "icon preload";
        favicon.as = "image";
        favicon.type = "image/x-icon";
        favicon.href = location.origin+'/SuperCookie/favicon.ico';
     
        function onEvent() {
          sleep(1000);   // 1 second 
        
        
        
	     if(index>=32){
            window.location.href = location.origin+'/SuperCookie/result';       
            return;
           }
	console.log(index);
    favicon.href = location.origin+'/SuperCookie/%version%/'+index+'/favicon.ico';
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
	

	private void ssendFav(HttpServletResponse response) throws IOException {
		String FILE = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mP8/x8AAwMCAO+ip1sAAAAASUVORK5CYII=";

		byte[] decodedBytes = Base64.getDecoder().decode(FILE);
		String decodedString = new String(decodedBytes);

		//response.setContentType("image/svg+xml");
		response.setContentType("image/png");
		
		response.addHeader("Vary","Origin");
		response.addHeader("Vary","Access-Control-Request-Method");
		response.addHeader("Vary","Access-Control-Request-Headers");
		
		response.addHeader("Cache-Control","max-age=43200");
		response.addHeader("Accept-Ranges","bytes");
		response.addHeader("Content-Type","image/png");
		response.addHeader("Content-Length","528");
		response.addHeader("Last-Modified","Fri, 16 May 2025 08:15:53 GMT");
		response.addHeader("Date",         "Fri, 16 May 2025 19:58:20 GMT"      );
		response.addHeader("Keep-Alive","timeout=60");
		response.addHeader("Connection","keep-alive");
		
		
		/*
		
		response.addHeader("Cache-Control", "max-age=2592000, public");
		//response.addHeader("Expires", "Tue, 03 Jul 2021 06:00:00 GMT");
		response.addHeader("Content-Type", "image/png");
		response.addHeader("Last-Modified", "Tue, 03 Jul 2030 06:00:00 GMT");

		response.addHeader("Content-Length", "" + decodedString.length());
		
		*/
		
		
		try {
			svg2jpg(response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		ServletOutputStream outputStream = response.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		printStream.print(decodedString);
		printStream.close();
		*/
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
		
		String ret="<!DOCTYPE html>\n"
				+ "<html lang=\"en\">\n"
				+ "\n"
				+ "<head>";
		
		//ret+="<link rel='icon' type='image/x-icon' href='/SuperCookie/sc1/favicon11.ico'>";
		//ret+="<link rel='icon' type='image/x-icon' href='/resources/images/favicon2.png'>";
		ret+= "</head>";
		
		ret+= "<body>";
		
		if(bs!=null)
		{
		
		long value = Bits.convert(bs.bs);
		   
			  String hash = hashNumber(""+value);
			  
			  
			  
			  
		 ret+= "<div>bs  : " + Bits.dumb(bs.bs) + "<br/> value > " + value +"  hash:"+hash+"<br/> </div>";
		}else {
			 ret+= "<div>No Result</div>";
		}
		ret += "<div> <a href='/SuperCookie/read'>Read</a>  <a href='write'>Write</a> </div>";
		
		ret+= "</body>";
		
		printStream.print(ret);
		printStream.close();
		return;
	}

	private static final int BUFFER_SIZE = 4096;
	private void connectRelay(String remoteAddress, HttpServletResponse response) throws IOException {
		// System.out.println("target Address : " + remoteAddress);

		URL url = new URL(remoteAddress);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		// System.out.println("responseCode : " + responseCode);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			// String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();
			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					// fileName = disposition.substring(index + 10, disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				// fileName = remoteAddress.substring(remoteAddress.lastIndexOf("/") + 1,
				// remoteAddress.length());
			}

			response.setContentType(contentType);
			response.setContentLength(contentLength);
			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			

			// opens an output stream to save into file

			response.getOutputStream();

			ServletOutputStream outputStream = response.getOutputStream();
			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();

	}

	
	    public static void svg2jpg(ServletResponse response) throws Exception {
	        //Step -1: We read the input SVG document into Transcoder Input
	       
	        /*
	        String ="<svg height='140' width='500' xmlns='http://www.w3.org/2000/svg'> "
	        		+ ""
	        		+ "<ellipse rx='100' ry='50' cx='120' cy='80'      style='fill:yellow;stroke:green;stroke-width:3' />	      </svg>"; 

	        */
	    	
	    	String svg="<svg class='w-6 h-6 text-gray-800 dark:text-white' aria-hidden='true' xmlns='http://www.w3.org/2000/svg' width='24' height='24' fill='none' viewBox='0 0 24 24'>";
	    	
	    	svg+="<circle cx=\"16\" cy=\"16\" r=\"14\" stroke=\"black\" stroke-width=\"2\" fill=\"white\"/>\n"
	    	 		+ "  <text x=\"50%\" y=\"50%\" dominant-baseline=\"middle\" text-anchor=\"middle\" font-size=\"20\" font-family=\"sans-serif\" fill=\"black\">1</text>"; 
	    	
	    	  svg+="</svg>";
	    	
	    	
	    	
	        
	        
	       StringReader  reader=new StringReader(svg);
	       TranscoderInput input = new TranscoderInput(reader);
	        
	        InputStream inputStream = new ByteArrayInputStream(svg.getBytes());
	        
	        String  imageType="png";
	        
	        Transcoder transcoder = getTranscoder(imageType, 0.7f);
	        
	       // ByteArrayOutputStream out=new ByteArrayOutputStream();
	        //FileOutputStream out=new FileOutputStream("/home/kivanov/Desktop/test.png");
	        
			//TranscoderOutput output = new TranscoderOutput(out);
	        
	        //transcoder.transcode(input, output);
	        
	        
	        OutputStream outputStream = response.getOutputStream();
	        
	        
	        
	        
	        TranscoderOutput sss = new TranscoderOutput(outputStream);
	        
	        transcoder.transcode(input, sss);
	        
	      
	            outputStream.flush();
	       
	        
	        
/*
 * 
	        
	        TranscoderOutput output = new TranscoderOutput(outputStream);
	        
	        try {
	            
				Transcoder transcoder = getTranscoder(imageType, 0.7f);
	            transcoder.transcode(input, output);
	        } catch (TranscoderException e) {
	            e.printStackTrace();
	        }
	*/               
	    }
	
	   
	    
	    private static Transcoder getTranscoder(String transcoderType, float keyQuality) {
	        Transcoder transcoder = null;
	        if (transcoderType.equals("jpeg")) {
	            transcoder = getJPEGTranscoder(keyQuality);
	        } else if (transcoderType.equals("png")) {
	            transcoder = getPNGTranscoder();
	        }
	        return transcoder;
	    }

	
	    private static JPEGTranscoder getJPEGTranscoder(float keyQuality) {
	        JPEGTranscoder jpeg = new JPEGTranscoder();
	        jpeg.addTranscodingHint(JPEGTranscoder.KEY_QUALITY, new Float(    keyQuality));
	        jpeg.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, 500f);
	        return jpeg;
	    }

	    private static PNGTranscoder getPNGTranscoder() {
	        return new PNGTranscoder();
	    }
	
}

