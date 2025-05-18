package com.matin.taxi.sc;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.UUID;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

public class utils {

	
	private String getCookieValue(HttpServletRequest req, String cookieName) {
		return Arrays.stream(req.getCookies())
				.filter(c -> c.getName()
				.equals(cookieName))
				.findFirst()
				.map(Cookie::getValue).orElse(null);
	}
 
	
	public static String getRandomUUID() {
	String id = UUID.randomUUID().toString();
	return id;
	}
	
	void startRead(ServletResponse response) throws IOException {

		ServletOutputStream outputStream = response.getOutputStream();
		PrintStream printStream = new PrintStream(outputStream);
		printStream.print(read().replace("%version%", "" + 1));
		printStream.close();
	}

	public String read() {
		return """
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
}
