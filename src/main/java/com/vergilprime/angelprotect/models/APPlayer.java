package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.AngelProtect;
import org.minidns.record.A;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class APPlayer {
	public java.util.UUID uuid;

	// List of a players claims, the "address" of which looks like world.x.z I.E. world_the_end.53._23
	// In MS namespaces for addresses can't contain dash (-) so we substitute an underscore.
	// Coordinates refer to chunk coordinates.
	public List<String> claims;

	// Friends are used in determining claim permissions.
	// A player can have "friends" added to a permission list on a claim to allow all players on their friends list.
	// A player should also be able to friend a town and the permission should extend to all members of the town.
	public List<UUID> friends;

	// Runes are a special kind of currency which determines how much land a player can claim.
	// Runes only increase over time based on votifier captured votes.
	// They can be "in use" in a claim but not actually "spent" so the total number a player has doesn't decrease.
	public int runes = 16;
	public String town = null;

	// This claim represents the default settings when a player claims new land.
	public APPersonalClaim defaultClaim;

	// TODO: this isn't right
	public LocalDateTime LastAccessed;// = new LocalDateTime.now();

	//
	//  Constructors
	//

	public APPlayer(UUID uuid){
		this.uuid = uuid;
		defaultClaim = new APPersonalClaim(null, this.uuid);
	}

	//
	//  Methods
	//

	public static APPlayer loadPlayer(UUID uuid){
		APPlayer player;
		if(false /* If the player data exists in persistence*/){
			// TODO: Load the player data into memory
			player = new APPlayer(uuid);
		}else{
			player = new APPlayer(uuid);
		}
		return player;
	}

	public int getRunesAvailable(){
		int runesAvailable = runes;
		// for every claim referenced in this.Claims, calculate the amount of runes the claim is using and subtract this from RunesAvailable.
		return runesAvailable;
	}

	public void save(){
		// TODO: update this.LastAccessed
		// TODO: save the player's data to persistence
	}

	public void addRunes(int amount){
		// Runes should never decrease normally unless cheating occured so runes can be removed by using a negative number for amount.
		runes += amount;
		save();
	}

	public void addClaim(String claimAddress){
		claims.add(claimAddress);
		save();
	}

	public void removeClaim(String claimAddress){
		claims.remove(claimAddress);
		save();
	}

	public void addFriend(UUID friend){
		friends.add(friend);
		save();
	}

	public void removeFriend(UUID friend){
		friends.remove(friend);
		save();
	}

	public void joinTown(String town){
		this.town = town;
		save();
	}

	//
	// Serialization
	//

	public HashMap<String, Object> serialize(){
		HashMap<String, Object> output = new HashMap<String, Object>();
		output.put("UUID", uuid);
		output.put("Friends", friends);
		output.put("Runes", runes);
		output.put("Town", town);
		output.put("DefaultClaim", defaultClaim);
		return output;
	}

}
