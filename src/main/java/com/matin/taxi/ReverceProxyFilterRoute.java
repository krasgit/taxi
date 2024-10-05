package com.matin.taxi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
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

		URL url = new URL(requestURL.toString());

		URI u = url.toURI();

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
		// System.out.println("ReverceProxyFilterSearch doFilter() Method !");

		// Get remote data

		HttpServletRequest dd = (HttpServletRequest) request;

		String bb = getURL((HttpServletRequest) request);

		String url = bb.replace("https://192.168.196.191:8443", "http://127.0.0.1:5000");

		// url=bb.replace("https://localhost:8443", "http://127.0.0.1:5000");
		System.out.println("url Address : " + url);

		connectRelayqaz(url, (HttpServletResponse) response);

		// PrintWriter out = response.getWriter();
		// response.setContentType("application/json");
		// response.setCharacterEncoding("UTF-8");
		// out.print("rterte");
		// out.flush();

	}
	// Code for 1st Filter

	public static void downloadFile(String fileURL, String saveDir) throws IOException {
		URL url = new URL(fileURL);
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		int responseCode = httpConn.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			String fileName = "";
			String disposition = httpConn.getHeaderField("Content-Disposition");
			String contentType = httpConn.getContentType();
			int contentLength = httpConn.getContentLength();
			if (disposition != null) {
				// extracts file name from header field
				int index = disposition.indexOf("filename=");
				if (index > 0) {
					fileName = disposition.substring(index + 10, disposition.length() - 1);
				}
			} else {
				// extracts file name from URL
				fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1, fileURL.length());
			}

			// System.out.println("Content-Type = " + contentType);
			// System.out.println("Content-Disposition = " + disposition);
			// System.out.println("Content-Length = " + contentLength);
			// System.out.println("fileName = " + fileName);

			// opens input stream from the HTTP connection
			InputStream inputStream = httpConn.getInputStream();
			String saveFilePath = saveDir + File.separator + fileName;

			// opens an output stream to save into file
			FileOutputStream outputStream = new FileOutputStream(saveFilePath);
			int bytesRead = -1;
			byte[] buffer = new byte[BUFFER_SIZE];
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			outputStream.close();
			inputStream.close();

			// System.out.println("File downloaded");
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();
	}

	private void connectRelay(String remoteAddress, HttpServletResponse response) throws IOException {

		// Check if the RemoteAddress is on the BlockList

		// Get URL of remoteAddress
		URL remoteURL = new URL(remoteAddress);

		// Create a connection to the remote Address
		HttpURLConnection proxyToServer = ((HttpURLConnection) remoteURL.openConnection());

		/*
		 * for (Map.Entry<String, List<String>> entries :
		 * proxyToServer.getHeaderFields().entrySet()) { String values = ""; for (String
		 * value : entries.getValue()) { values += value + ","; }
		 * System.out.println("Response"+ entries.getKey() + " - " + values ); }
		 */

		// Optional
		proxyToServer.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		proxyToServer.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		// Setup connection
		proxyToServer.setUseCaches(false);
		proxyToServer.setDoOutput(true);

		HttpServletResponse resp = (HttpServletResponse) response;

		resp.setStatus(proxyToServer.getResponseCode());
		resp.setStatus(proxyToServer.getResponseCode());
		if (proxyToServer.getResponseCode() != 200) {
			return;
		}

		resp.setContentType(proxyToServer.getContentType());
		PrintWriter writer = response.getWriter();

		byte[] buffer = new byte[4096];
		int read = 0;

	}

	public static void main(String[] args) {
		try {

			downloadFile("http://localhost:8080/tile/0/0/0.png", "/home/kivanov/Desktop/diff");
			// downloadFile("http://localhost:8080/tile/0/0/0.png",
			// "/home/kivanov/Desktop/diff");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void connectRelayqaz(String remoteAddress, HttpServletResponse response) throws IOException {
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

			// System.out.println("File downloaded");
		} else {
			System.out.println("No file to download. Server replied HTTP code: " + responseCode);
		}
		httpConn.disconnect();

	}

}
