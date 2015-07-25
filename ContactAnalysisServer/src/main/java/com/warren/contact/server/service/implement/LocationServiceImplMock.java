package com.warren.contact.server.service.implement;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.warren.contact.server.domain.FriendLocation;
import com.warren.contact.server.domain.Location;
import com.warren.contact.server.domain.User;
import com.warren.contact.server.service.ContactService;
import com.warren.contact.server.service.LocationService;
import com.warren.contact.server.service.UserService;

public class LocationServiceImplMock implements LocationService {
	UserService userService = new UserServiceImpl();
	ContactService contactService = new ContactServiceImpl();
	private static Logger logger = Logger.getLogger(LocationServiceImplMock.class);



	@Override
	public Location getLatestLocation(String deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Location getLatestLocationByPhone(String phone) {
	Location location = new Location();
	location.setLocTime(new Date());
	location.setLatitude("30.5328628192244");
	location.setLogitude("104.06435421309348");
	location.setFullAddress("美年广场");
	return location;
	
	}
	
	private Location genRandomLocation(int i) {
		String suffix = String.valueOf(i);
		Location location = new Location();
		location.setLocTime(new Date());
		location.setLatitude("30.53"+suffix);
		location.setLogitude("104.06"+suffix);
		location.setFullAddress("美年广场");
		return location;
	}

	@Override
	public List<Location> getAllLocation(String deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FriendLocation> getFriendsLocation(String phone) {
		List<FriendLocation> friendLocationList = new ArrayList<FriendLocation>();
		
		for (int i = 0; i < 1; i++) {
			
			Location location = genRandomLocation(i);
			if(location==null) {
				continue;
			}
			User friend = new User();
			FriendLocation friendLocation= new FriendLocation();
			friendLocation.setUser(friend);
			friend.setName("小东哥");
			friend.setPhone("13546090876");
			friendLocation.setLocation(location);
			friendLocationList.add(friendLocation);

		}
		logger.info(phone+"的朋友最近的位置数据："+friendLocationList);
		return friendLocationList;
	}

	@Override
	public List<Location> getAllLocationByPhone(String phone) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FriendLocation> getFriendsLocationByUid(String uid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateLocation(User user, Location locaiton) {
		// TODO Auto-generated method stub
		return false;
	}

}
