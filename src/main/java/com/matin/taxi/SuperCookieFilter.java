package com.matin.taxi;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.DatatypeConverter;

public class SuperCookieFilter implements Filter {

	private static final int BUFFER_SIZE = 4096;
	
	static String urlPatterns="/SuperCookie/*";
	
	public static String getFullURL(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
		String queryString = request.getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	
	public String  hashNumber(String password) 	  throws NoSuchAlgorithmException {
	        
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    md.update(password.getBytes());
	    byte[] digest = md.digest();
	    String myHash = DatatypeConverter.printHexBinary(digest).toUpperCase();
	        
	    return myHash;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//System.out.println("Procces UrlPatterns "+urlPatterns);
		try {
			String identifier="4";
			String hash=hashNumber(identifier);
			System.err.println("SuperCookie"+hash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		 Cookie[] cookies = httpRequest.getCookies();
		String rs = httpRequest.getRequestedSessionId();
		 Enumeration<String> hs = httpRequest.getHeaderNames();
		 
		
		
		
		
		System.err.printf("The current date is: %tF", new Date());  // '%tF': format as yyyy-mm-dd
		System.err.println();
		//System.err.printf("The last mo date is: %tF", getLastModified(httpRequest));  // '%tF': format as yyyy-mm-dd
		
		
		//Otherwise the server responds with the status code 304 (Not Modified). 
		
		String ff = httpRequest.getRequestURI();
		//String bb = getFullURL((HttpServletRequest) request);
		String queryString = httpRequest.getQueryString();
		String url = "http://localhost:8181/search?" + queryString;

		
		
		//chain.doFilter(request, response);
		connectRelay(url, (HttpServletResponse) response);

	}
	
	
	
	/*
	 private void writeOutputStream(String value, OutputStream outputStream) throws IOException {
	        BASE64Decoder decoder = new BASE64Decoder();
	        byte[] imgBytes = decoder.decodeBuffer(value);
	        BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imgBytes));
	        ImageIO.write(bufImg, "png", outputStream);
	    }
	*/
	private void connectRelay(String remoteAddress, HttpServletResponse response) throws IOException {
		System.out.println("target Address : " + remoteAddress);
		
		
		String str="<?xml version=\"1.0\" encoding=\"utf-8\"?><!-- Uploaded to: SVG Repo, www.svgrepo.com, Generator: SVG Repo Mixer Tools -->\n"
				+ "<svg width=\"800px\" height=\"800px\" viewBox=\"0 0 16 16\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
				+ "<path d=\"M5.6906 6.00001L3.16512 1.62576C4.50811 0.605527 6.18334 0 8 0C8.37684 0 8.74759 0.0260554 9.11056 0.076463L5.6906 6.00001Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M5.11325 9L1.69363 3.07705C0.632438 4.43453 0 6.14341 0 8C0 8.33866 0.0210434 8.67241 0.0618939 9H5.11325Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M4.89635 15.3757C2.93947 14.5512 1.37925 12.9707 0.581517 11H7.42265L4.89635 15.3757Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M8 16C7.62316 16 7.25241 15.9739 6.88944 15.9235L10.3094 10L12.8349 14.3742C11.4919 15.3945 9.81666 16 8 16Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M16 8C16 9.85659 15.3676 11.5655 14.3064 12.9229L10.8868 7H15.9381C15.979 7.32759 16 7.66134 16 8Z\" fill=\"#000000\"/>\n"
				+ "<path d=\"M11.1036 0.624326C13.0605 1.44877 14.6208 3.02927 15.4185 5H8.57735L11.1036 0.624326Z\" fill=\"#000000\"/>\n"
				+ "</svg>";
		
		//response.setContentType("image/ico");
		response.setContentType("image/svg+xml");
		//response.setHeader("Pragma", "");
		//response.setHeader("Cache-Control", "");
		
		response.addHeader("Cache-Control", "public, max-age=31536000");
		response.addHeader("Expires", "Tue, 03 Jul 2031 06:00:00 GMT");
		response.addHeader("Content-Type", "image/svg+xml");
		response.addHeader("Content-Length", ""+str.length());
		
	    /*
	          "Cache-Control": "public, max-age=31536000",
        "Expires": new Date(Date.now() + 31536000000).toUTCString(),
        "Content-Type": "image/png",
        "Content-Length": str.length
	     */
		

		response.setHeader("Content-Disposition", "inline; fileName=image.ico");
		
		response.setHeader("Expires", "Tue, 03 Jul 2026 06:00:00 GMT");         
		response.setHeader("Last-Modified", new Date().toString());   
		
		
		//response.setStatus(BUFFER_SIZE)
		
		ServletOutputStream outputStream = response.getOutputStream();
		
		//outputStream.write(str.getBytes(Charset.forName("UTF-8")));
		
		 PrintStream printStream = new PrintStream(outputStream);
		 printStream.print(str);
		 printStream.close();
		 
/*
		URL url = new URL(remoteAddress);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		System.out.println("responseCode  : " + responseCode);
		*/
/*
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
			// String saveFilePath = remoteAddress + File.separator + fileName;
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
			System.out.println("Server replied HTTP code: " + responseCode);
		}
		
		
		*/
		//httpConn.disconnect();

	}

}
