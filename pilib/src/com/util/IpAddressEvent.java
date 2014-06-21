package com.util;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpAddressEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private InetAddress ipAddress;
	private String ip = "192.168.2.101";
	
	public IpAddressEvent() {
	}

	public InetAddress getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(InetAddress ipAddress) {
		this.ip = ipAddress.toString();
		this.ipAddress = ipAddress;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		try {
			this.ipAddress = InetAddress.getByName(ip);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.ip = ip;
	}
}
