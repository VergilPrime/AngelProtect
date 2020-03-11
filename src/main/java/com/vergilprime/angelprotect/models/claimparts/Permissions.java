package com.vergilprime.angelprotect.models.claimparts;

import java.util.Arrays;
import java.util.List;

public class Permissions {

	// If a player can break or place blocks, frames, armor stands, trophies, etc here.
	public List<String> Build;

	// If a player can activate stone pressure plates, stone buttons and switches here.
	public List<String> Switch;

	// If a player can teleport into this claim.
	public List<String> Teleport;

	// If a player can change the permissions, protections, or unclaim this chunk.
	public List<String> Manage;

	// If the player can open containers in this chunk (only if container protections are enabled here).
	public List<String> Container;

	//
	//  Constructors
	//

	public Permissions(boolean isTown) {

		if(isTown){
			// These are only the default settings if the claim is for a town, but this can be overridden if the town has different default settings;
			Build = Arrays.asList("Members");
			Switch = Arrays.asList("Members", "Allies");
			Teleport = Arrays.asList("Members", "Allies");
			Manage = Arrays.asList("Assistants");
			Container = Arrays.asList("Members", "Allies");
		}else{
			// These are the default settings for a personal claim but are overridden by the owner's default settings.
			Build = Arrays.asList("Friends");
			Switch = Arrays.asList("Friends");
			Teleport = Arrays.asList("Friends");
			Manage = Arrays.asList();
			Container = Arrays.asList("Friends");
		}
	}
}
