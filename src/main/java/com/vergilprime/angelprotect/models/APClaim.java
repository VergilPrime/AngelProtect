package com.vergilprime.angelprotect.models;
import com.vergilprime.angelprotect.models.claimparts.Protections;
import com.vergilprime.angelprotect.models.claimparts.Permissions;
import java.util.HashMap;

public abstract class APClaim {
	public String address;
	public Permissions permissions;
	public Protections protections;

	// This holds the last timestamp where the player's data was accessed and is used to determine when to unload the player.
	public Long lastAccessed = System.currentTimeMillis();

	//
	//  Constructors
	//

	APClaim(boolean isTown){
		protections = new Protections();
		permissions = new Permissions(isTown);
	}

	//
	// Serialization
	//

	public static APClaim loadClaim(String address) {
		// TODO all of this method
		APClaim claim = new APClaim(false) {
		};

		claim.lastAccessed = System.currentTimeMillis();

		return claim;
	}
}
