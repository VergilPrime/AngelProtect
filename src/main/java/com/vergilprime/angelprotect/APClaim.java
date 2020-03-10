package com.vergilprime.angelprotect;

import com.vergilprime.angelprotect.claimparts.Protections;
import com.vergilprime.angelprotect.claimparts.Permissions;

import java.util.UUID;

public class APClaim {
	public String Address;
	public UUID Owner;
	public Permissions Permissions;
	public com.vergilprime.angelprotect.claimparts.Protections Protections;

	public APClaim(String address,UUID owner, boolean isTown){
		Address = address;
		Owner = owner;
		Protections = new Protections();
		Permissions = new Permissions(isTown);
	}
}
