package com.matin.taxi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ReverceProxyFilterSearchPhoton implements Filter {

	private static final int BUFFER_SIZE = 4096;
	//java -jar photon-0.6.0.jar -nominatim-import -host localhost -port 5433 -database nominatim -user nominatim -password very_secure_password
	static String urlPatterns="/api/*";
	
	public static String getFullURL(HttpServletRequest request) {
		StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
		String queryString = request.getQueryString();

		if (queryString == null) {
			return requestURL.toString();
		} else {
			return requestURL.append('?').append(queryString).toString();
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//System.out.println("Procces UrlPatterns "+urlPatterns);

		HttpServletRequest dd = (HttpServletRequest) request;
		String queryString = dd.getQueryString();
		String url = "http://localhost:2322/api?" + queryString;

		System.out.println("getRequestURL : " + dd.getRequestURL());
		//System.out.println("Remote Host : " + request.getRemoteHost());
		//System.out.println("Remote Address : " + request.getRemoteAddr());

		connectRelay(url, (HttpServletResponse) response);
	}
	
	private void connectRelay(String remoteAddress, HttpServletResponse response) throws IOException {

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
