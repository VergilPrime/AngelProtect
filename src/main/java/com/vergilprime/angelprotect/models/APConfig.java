package com.vergilprime.angelprotect.models;

import sun.security.util.Password;

public class APConfig {
	Integer defaultRunes = 16;
	Integer joinTownCost = 32;
	APClaim defaultPersonalClaim = new APPersonalClaim(null,null);
	APClaim defaultTownClaim = new APTownClaim(null, null);
	Short claimCost = 1;
	Short protectionPVPCost = 1;
	Short protectionMobCost = 1;
	Short protectionContainerCost = 2;
	// database stuff
	String hostname = "sql.host.com";
	Short port = 3306;
	String database = angelprotect;
	String username = angelprotect;
	String password = angelprotect;
}
