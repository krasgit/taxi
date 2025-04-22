package com.matin.taxi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ReverceProxyFilterRoute implements Filter {

	private static final int BUFFER_SIZE = 4096;
	static String urlPatterns="/route/*";
	public static String getURL(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
		String queryString = request.getQueryString();

		String ret = null;

		if (queryString == null) {
			ret = requestURL.toString();
		} else {
			ret = requestURL.append('?').append(queryString).toString();
		}

		return ret;
	}

	public static String getFullURL(HttpServletRequest request) throws Exception {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());

		//URL url = new URL(requestURL.toString());
		//URI u = url.toURI();

		String queryString = request.getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	public static URL fixRequest(ServletRequest request,	int newPort) throws MalformedURLException
	{
      String url = ((HttpServletRequest) request).getRequestURL().toString();
      URL originalUrl = new URL(url);
      URL newURL = new URL("http", "127.0.0.1", newPort, originalUrl.getFile());
	return newURL;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
	//	url =bb.replace("https://192.168.196.191:8443", "http://127.0.0.1:5000");
	//	url =url.replace("https://localhost:8443", "http://127.0.0.1:5000");
	//	url =url.replace("https://127.0.0.1:8443", "http://127.0.0.1:5000");

		// url=bb.replace("https://localhost:8443", "http://127.0.0.1:5000");
	//	System.out.println("url Address : " + url);
		URL url=fixRequest(request,5000);

		connectRelay(url, (HttpServletResponse) response);
	}
	
	
	private void connectRelay(URL url, HttpServletResponse response) throws IOException {
		// System.out.println("target Address : " + remoteAddress);

		//URL url = new URL(remoteAddress);
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

			// System.out.println("Content-Type = " + contentType);
			// System.out.println("Content-Disposition = " + disposition);
			// System.out.println("Content-Length = " + contentLength);
			// System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			// String saveFilePath = remoteAddress + File.separator + fileName;

			// opens an output stream to save into file

			response.getOutputStream();

			ServletOutputStream outputStream = response.getOutputStream();
			// FileOutputStream outputStream =new FileOutputStream(saveFilePath);
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

			// System.out.println("Content-Type = " + contentType);
			// System.out.println("Content-Disposition = " + disposition);
			// System.out.println("Content-Length = " + contentLength);
			// System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			// String saveFilePath = remoteAddress + File.separator + fileName;

			// opens an output stream to save into file

			response.getOutputStream();

			ServletOutputStream outputStream = response.getOutputStream();
			// FileOutputStream outputStream =new FileOutputStream(saveFilePath);
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
