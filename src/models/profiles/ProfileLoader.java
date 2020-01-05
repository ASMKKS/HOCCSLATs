package models.profiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import views.items.ProfileListItem;

public class ProfileLoader {
	public static HashMap<ProfileListItem, Profile> profiles;
	final static String PROFILE_PATH = "./src/resources/profiles/";
	
	public void refreshProfileList() {
		profiles = new HashMap<ProfileListItem, Profile>();
		ProfileReader reader = new ProfileReader();
		for (File file : new File(PROFILE_PATH).listFiles()) {
			Profile profile = reader.readProfileFromXML(file.getPath());
			profiles.put(profile.toProfileListItem(), profile);
		}
	}
	
	public static void deleteProfiles() {
		for (ProfileListItem item : profiles.keySet()) {
			if (item.isSelectedForDelete) {
				new File(PROFILE_PATH + profiles.get(item).getProfileName()).delete();
			}
		}
	}
	
	public void writeProfileToFile() {
		
	}

}
