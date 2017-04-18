package com.utils.url.proxy;

/**
 * Http代理对象
 * 
 * @author liaolh
 *
 */
public class HttpProxy {
	private String proxy = "http";
	private String host;
	private int port;
	private String user;
	private String password;

	public String getProxy() {
		return proxy;
	}

	public void setProxy(String proxy) {
		this.proxy = proxy;
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

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public int hashCode() {
		return host.hashCode() + port;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final HttpProxy other = (HttpProxy) obj;
		if (host == null) {
			if (other.getHost() != null)
				return false;
		} else if (!host.equals(other.getHost()))
			return false;
		if (port != other.getPort())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return host + ":" + port;
	}
}
