package com.vnc;

public class VncViewerInfo {
	protected String host;
	protected int port;
	protected String password;
	protected String user;
	protected String userdomain;
	
	public VncViewerInfo(String host, int port, String password, String user, String userdomain) {
		setHost(host);
		setPort(port);
		setPassword(password);
		setUser(user);
		setUserdomain(userdomain);
	}
	
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getUserdomain() {
		return userdomain;
	}
	public void setUserdomain(String userdomain) {
		this.userdomain = userdomain;
	}
}
