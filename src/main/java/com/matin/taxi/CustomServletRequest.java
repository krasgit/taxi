package com.matin.taxi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CustomServletRequest  extends HttpServletRequestWrapper{
	private String modifiedURI;

	public CustomServletRequest(HttpServletRequest request) {
	    super(request);
	}

	public void setRequestURI(String uri) {
	    this.modifiedURI = uri;
	}

	@Override
	public String getRequestURI() {
	    return modifiedURI;
	}

	@Override
	public StringBuffer getRequestURL() {
	    StringBuffer url = new StringBuffer();
	    url.append(getScheme()).append("://").append(getServerName()).append(modifiedURI);
	    return url;
	}
}
