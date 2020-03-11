package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Permissions;
import com.vergilprime.angelprotect.models.claimparts.Protections;

import java.util.UUID;

public class APTownClaim extends APClaim {
	public String Town;

	//
	//  Constructors
	//

	public APTownClaim(String address, String town){
		super(true);
		Address = address;
		Town = town;
		Protections = new Protections();
		Permissions = new Permissions(false);
	}
}
