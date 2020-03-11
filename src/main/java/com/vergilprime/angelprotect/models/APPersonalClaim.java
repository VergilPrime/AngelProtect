package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Permissions;
import com.vergilprime.angelprotect.models.claimparts.Protections;

import java.util.UUID;

public class APPersonalClaim extends APClaim {
	public UUID Owner;

	//
	//  Constructors
	//

	public APPersonalClaim(String address, UUID owner){
		super(false);
		Address = address;
		Owner = owner;
		Protections = new Protections();
		Permissions = new Permissions(false);
	}
}
