package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Protections;
import com.vergilprime.angelprotect.models.claimparts.Permissions;

import java.util.HashMap;

public abstract class APClaim {
	public String address;
	public Permissions permissions;
	public com.vergilprime.angelprotect.models.claimparts.Protections protections;

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

		return claim;
	}
}
