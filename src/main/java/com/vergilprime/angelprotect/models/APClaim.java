package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.claimparts.Protections;
import com.vergilprime.angelprotect.models.claimparts.Permissions;

import java.util.UUID;

public class APClaim {
	public String Address;
	public UUID Owner;
	public Permissions Permissions;
	public com.vergilprime.angelprotect.models.claimparts.Protections Protections;

	public APClaim(String address,UUID owner, boolean isTown){
		Address = address;
		Owner = owner;
		Protections = new Protections();
		Permissions = new Permissions(isTown);
	}
}
