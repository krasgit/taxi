package com.matin.taxi.sc;

import java.io.*;
import java.util.*;

import com.matin.taxi.CustomServletRequest;
import jakarta.servlet.*;
import jakarta.servlet.http.*;

public class SuperCookieFilter implements Filter {

	private static final long VALUE = 3;
	private static final int WRITES = 4;
	private static final int SPEED = 5001;
	static int version = 32;
	public static String urlPatterns = "/SuperCookie/*";
    //---------------------------------------------------------------------------
	private final Map<String, BitSetMode> visited = new HashMap<>();
	
	String parseRefer(HttpServletRequest httpRequest)
	{
		String ref="";
		try {
			String referer=httpRequest.getHeader("referer");
			String[] args=referer.split(",");
			ref=args[1];
		}
		catch (Exception  e) {
         return "";
        }
	return ref;
	}
	
	boolean processICO(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException
	{
		boolean geticon=false;
		String ff = httpRequest.getRequestURI();
			String ss = ff.replace("/SuperCookie/" + version + "/", "");

			String ind = ss.replace("/favicon.ico", "");

			int index = Integer.valueOf(ind);
					
			String ref = parseRefer(httpRequest);
			System.err.println("Get favicon.svg  uid " + ref + " qs" + index);

			BitSetMode bs = visited.get(ref);
			if (bs != null) 
			{
				if (bs.mode == BitSetMode.READ) {
					bs.bs.set(index);
					geticon= false;
				} else {
					
					geticon= (bs.bs.get(index) == true); 
				}	
			}
			return geticon;
		}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

	
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String ff = httpRequest.getRequestURI();
		//String queryString = httpRequest.getQueryString();
		System.out.println("59GET ==" + ff+ " referer:"+httpRequest.getHeader("referer"));
		
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		//String uid = getCookieValue(httpRequest, "uid");
		
		
		//---------------------------------------------------------------------
		if (ff.contains(".ico")) {

			boolean mode =processICO(httpRequest,httpResponse);
			
			
			if(mode){
				System.out.println("200");
				CustomServletRequest customRequest = new CustomServletRequest(httpRequest);
				String reqURI = httpRequest.getRequestURI();
				String newURI = "/resources/images/favicon.png";
				customRequest.setRequestURI(newURI);
				chain.doFilter(request, response);
						
			} else
			{
				System.out.println("404");
			httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
			}
			return;
		}
        //-----------------------------------------------------------------------
		
		if (ff.equals("/SuperCookie/result")||ff.equals("/SuperCookie/")) {
			String uids = "";
			result(response, uids);
		
		}
        //------------------------------------------------------------------------
		if (ff.equals("/SuperCookie/read")) {
			
			visited.clear();
			String ruid =utils.getRandomUUID();
			visited.put(ruid, new BitSetMode(BitSetMode.READ,0));
			
			startWrite(response, 0,ruid);
			return;
		}
		//-------------------------------------------------------------------------
		if (ff.startsWith("/SuperCookie/write")) {
			String a = ff.replace("/SuperCookie/write/", "");
			String[] arg=a.split(",");
			
			try {
				String referer=arg[1];
				if(referer.equals("/SuperCookie/write"))
					throw new Exception("todo");
				String ind=arg[2];
				int index = Integer.valueOf(ind);
				
				if (index >= WRITES) {
					
					result(response, referer);
					return;
				}

				startWrite(response, ++index,referer);
			} catch (Exception e) {
				String nuid = utils.getRandomUUID();
				visited.put(nuid, new BitSetMode(BitSetMode.WRITE, VALUE));
				System.err.println("new Cookie uid " + nuid);
				startWrite(response, 0,nuid);
				return;
			}
		}
	}

	void startWrite(ServletResponse response, int index,String referer) throws IOException {
		ServletOutputStream outputStream = response.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		
		BitSetMode bsm = visited.get(referer);
		String body=Bits.getBS(bsm,referer);
		
		String write = write2()
				.replace("%version%", "" + version)
				.replace("%referer%", "" + referer)
				.replace("%index%", "" + index)
				.replace("%speed%", "" + SPEED)
				.replace("%body%", "" + body);
		printStream.print(write);
		printStream.close();
	}

	public String write2() {
		return """
				<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8"/>
        <title>supercookie • %index%</title>
        <meta name="author" content="Jonas Strehle"/>
        <meta name="robots" content="noindex"/>
        <meta name="viewport" content="viewport-fit=cover, user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
        <style>
            html, body {
                margin: 0px;
                width: 100%;
                height: 100%;
                padding: 0px;
                display: flex;
                justify-content: center;
                align-items: center;
                background-color: rgb(32, 32, 32);
                color: #ff0358;
                font-size: large;
                font-family: sans-serif;
            }
        </style>
    </head>
    
    <body>
        <h1>{{index}}</h1>
        <script type="module">
            
           // if (JSON.parse("{{bit}}"))
                document.location.href = `${document.location.origin}/SuperCookie/write/%referer%`;
            //https://192.168.196.191:8443/df518607-c0c5-4418-8208-35294724dd2a
            const favicon = document.createElement('link');
            favicon.rel = "icon preload";
            favicon.as = "image";
            favicon.type = "image/x-icon";
            favicon.onload = favicon.onerror = () => 
               // document.location.href = `${document.location.origin}/%referer%`;
                document.location.href = `${document.location.origin}/SuperCookie/write/%referer%`;
            document.head.appendChild(favicon);
            globalThis.f = favicon;
            favicon.href = `//${window.location.host}/SuperCookie/%version%/%index%/favicon.ico`;
        </script>
    </body>
</html>
				""";
		}
	
	public String write1() {
		return """
			<!DOCTYPE html><html><head>
			<title>TODO</title><link rel="shortcut icon"  type="image/x-icon" href="/SuperCookie/%version%/%index%/favicon.ico"></head>
			<body>%body%</body>
     	   <script>
            console.info("%version%");

            window.addEventListener("DOMContentLoaded", () => {
			setTimeout(()=>{
			    document.location.href = `${document.location.origin}`+"/SuperCookie/write/,%referer%,%index%";
            }, %speed%);        });
				  </script>
				</html>
						  """;
	};
	
	void result(ServletResponse response, String uid) throws IOException {

		ServletOutputStream outputStream = response.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		
		String ret = "<!DOCTYPE html>\n" + "<html lang=\"en\">\n" + "\n" + "<head>";

		ret+="<meta name='robots' content='noindex'/>";
		ret+="<meta name='viewport' content='viewport-fit=cover, user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1'>";
		
		 ret+="<style>  html, body { margin: 0px; width: 100%; height: 100%; padding: 0px;"
		 		+ " font-size: large; font-family: sans-serif;}"
		 		+ "</style>";
		
		ret += "</head><body>";
		BitSetMode bsm = visited.get(uid);
		ret+=	 Bits.getBS(bsm,uid);
		ret += "<div> <a href='/SuperCookie/read'>Read</a>  <a href='write'>Write</a> </div>";

		ret += "</body>";

		printStream.print(ret);
		printStream.close();
		return;
	}
}
