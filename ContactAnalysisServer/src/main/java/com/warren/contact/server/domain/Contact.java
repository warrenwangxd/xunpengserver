package com.warren.contact.server.domain;

import java.io.Serializable;

import com.warren.contact.server.util.StringUtils;

public class Contact  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4039635936070017631L;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		//对手机号做归一化处理
		//对手机号做归一化处理
				phoneNumber = StringUtils.replaceBlank(phoneNumber);
				if (phoneNumber.startsWith("+86")) {
					phoneNumber = phoneNumber.substring(3);
				}
		
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		//对手机号做归一化处理
		phoneNumber = StringUtils.replaceBlank(phoneNumber);
		if (phoneNumber.startsWith("+86")) {
			phoneNumber = phoneNumber.substring(3);
		}
		this.phoneNumber = phoneNumber;
	}

	private String phoneNumber;
	
	public boolean equals(Contact contact) {
		return this.phoneNumber.equals(contact.getPhoneNumber());
	}
	
	public int hashCode() {
		return this.phoneNumber.hashCode();
	}
	
	public String toString() {
		return this.name+":"+this.phoneNumber+"\n";
	}

}
