package com.vergilprime.angelprotect;

import com.vergilprime.angelprotect.models.APClaim;
import com.vergilprime.angelprotect.models.APPlayer;
import com.vergilprime.angelprotect.models.APTown;

import java.util.HashMap;
import java.util.UUID;

public class AngelProtect {
	private HashMap<UUID, APPlayer> loadedPlayers;
	private HashMap<String, APClaim> loadedClaims;
	private HashMap<String, APTown> towns;

	public static void main(String[] args){
		// TODO: Load all towns
		// TODO: Bind all events
		// TODO: Begin unload tasks

	}

	public void loadPlayer(UUID uuid){
		loadedPlayers.put(uuid, APPlayer.loadPlayer(uuid));
	}
	public void loadClaim(String address){
		loadedClaims.put(address, APClaim.loadClaim(address));
	}
}
