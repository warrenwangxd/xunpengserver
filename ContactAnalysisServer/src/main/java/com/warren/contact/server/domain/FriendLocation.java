package com.warren.contact.server.domain;

public class FriendLocation {
	private User user;
	private Location location;


	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String toString() {
		return this.user.getName() + "|" + this.user.getPhone() + "|"
				+ this.location.getFullAddress() + "|"
				+ this.location.getLocTime();
	}

}
