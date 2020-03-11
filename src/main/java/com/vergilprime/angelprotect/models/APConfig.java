package com.vergilprime.angelprotect.models;

public class APConfig {
	Integer DefaultRunes = 16;
	Integer JoinTownCost = 32;
	APClaim DefaultPersonalClaim = new APPersonalClaim(null,null);
	APClaim DefaultTownClaim = new APTownClaim(null, null);
	Short ClaimCost = 1;
	Short ProtectionPVPCost = 1;
	Short ProtectionMobCost = 1;
	Short ProtectionContainerCost = 2;
}
