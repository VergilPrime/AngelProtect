package com.vergilprime.angelprotect.models.claimparts;

import java.util.HashMap;

public class Protections {
	public boolean fire = true;
	public boolean tnt = true;
	public boolean mob = false;
	public boolean pvp = false;
	public boolean container = false;

	//
	// Serialization
	//

	public HashMap<String, Boolean> serialize() {
		HashMap<String, Boolean> serializedProtections = new HashMap<>();
		serializedProtections.put("fire",this.fire);
		serializedProtections.put("tnt",this.tnt);
		serializedProtections.put("mob",this.mob);
		serializedProtections.put("pvp",this.pvp);
		serializedProtections.put("container",this.container);

		return serializedProtections;
	}
}
