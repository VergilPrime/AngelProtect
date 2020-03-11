package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Protections;
import com.vergilprime.angelprotect.models.claimparts.Permissions;

import java.util.UUID;

public abstract class APClaim {
	public String Address;
	public Permissions Permissions;
	public com.vergilprime.angelprotect.models.claimparts.Protections Protections;

	//
	//  Constructors
	//

	APClaim(boolean isTown){
		Protections = new Protections();
		Permissions = new Permissions(isTown);
	}
}
