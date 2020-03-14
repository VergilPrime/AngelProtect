package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Permissions;
import com.vergilprime.angelprotect.models.claimparts.Protections;

import java.util.HashMap;
import java.util.UUID;

public class APPersonalClaim extends APClaim {
	public UUID owner;

	//
	//  Constructors
	//

	public APPersonalClaim(String address, UUID owner){
		super(false);
		this.address = address;
		this.owner = owner;
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
		serializedClaim.put("owner",this.owner);
		serializedClaim.put("town",null);

		return serializedClaim;
	}
}
