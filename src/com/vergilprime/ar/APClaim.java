package com.vergilprime.ar;

import com.vergilprime.ar.claim_parts.Permissions;
import com.vergilprime.ar.claim_parts.Protections;

import java.util.UUID;

public class APClaim {
	public String Address;
	public UUID Owner;
	public Permissions Permissions;
	public Protections Protections;

	public APClaim(String address,UUID owner, boolean isTown){
		Address = address;
		Owner = owner;
		Protections = new Protections();
		Permissions = new Permissions(isTown);
	}
}
