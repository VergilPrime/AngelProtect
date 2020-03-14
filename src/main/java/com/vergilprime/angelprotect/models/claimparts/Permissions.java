package com.vergilprime.angelprotect.models.claimparts;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Permissions {

	// If a player can break or place blocks, frames, armor stands, trophies, etc here.
	public List<String> build;

	// If a player can activate stone pressure plates, stone buttons and switches here.
	public List<String> Switch;

	// If a player can teleport into this claim.
	public List<String> teleport;

	// If a player can change the permissions, protections, or unclaim this chunk.
	public List<String> manage;

	// If the player can open containers in this chunk (only if container protections are enabled here).
	public List<String> container;

	//
	//  Constructors
	//

	public Permissions(boolean isTown) {

		if(isTown){
			// These are only the default settings if the claim is for a town, but this can be overridden if the town has different default settings;
			build = Arrays.asList("Members");
			Switch = Arrays.asList("Members", "Allies");
			teleport = Arrays.asList("Members", "Allies");
			manage = Arrays.asList("Assistants");
			container = Arrays.asList("Members", "Allies");
		}else{
			// These are the default settings for a personal claim but are overridden by the owner's default settings.
			build = Arrays.asList("Friends");
			Switch = Arrays.asList("Friends");
			teleport = Arrays.asList("Friends");
			manage = Arrays.asList();
			container = Arrays.asList("Friends");
		}
	}

	//
	// Serialization
	//

	public HashMap<String, List<String>> serialize() {
		HashMap<String, List<String>> serializedPermissions = new HashMap<>();
		serializedPermissions.put("build",this.build);
		serializedPermissions.put("switch",this.Switch);
		serializedPermissions.put("teleport",this.teleport);
		serializedPermissions.put("manage",this.manage);
		serializedPermissions.put("container",this.container);

		return serializedPermissions;
	}
}
