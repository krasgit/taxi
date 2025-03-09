package com.matin.taxi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
			System.err.println(hash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpServletRequest httpRequest = (HttpServletRequest) request;

		 Cookie[] cookies = httpRequest.getCookies();
		String rs = httpRequest.getRequestedSessionId();
		
		String ff = httpRequest.getRequestURI();
		//String bb = getFullURL((HttpServletRequest) request);
		String queryString = httpRequest.getQueryString();
		String url = "http://localhost:8181/search?" + queryString;

		
		
		//chain.doFilter(request, response);
		//connectRelay(url, (HttpServletResponse) response);

	}
	
	private void connectRelay(String remoteAddress, HttpServletResponse response) throws IOException {
		System.out.println("target Address : " + remoteAddress);

		URL url = new URL(remoteAddress);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		System.out.println("responseCode  : " + responseCode);

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
		httpConn.disconnect();

	}

}
