package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Permissions;
import com.vergilprime.angelprotect.models.claimparts.Protections;

import java.util.HashMap;

public class APTownClaim extends APClaim {
	public String town;

	//
	//  Constructors
	//

	public APTownClaim(String address, String town){
		super(true);
		this.address = address;
		this.town = town;
		protections = new Protections();
		permissions = new Permissions(false);
	}

	//
	// Serialization
	//

	public HashMap<String, Object> serialize(){
		HashMap<String, Object> serializedClaim = new HashMap<>();
		serializedClaim.put("address",this.address);
		serializedClaim.put("permissions",this.permissions.serialize());
		serializedClaim.put("protections",this.protections.serialize());
		serializedClaim.put("owner",null);
		serializedClaim.put("town",this.town);

		return serializedClaim;
	}
}

