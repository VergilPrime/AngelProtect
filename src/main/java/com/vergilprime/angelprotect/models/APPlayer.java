package com.vergilprime.angelprotect.models;

import com.vergilprime.angelprotect.models.APClaim;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class APPlayer {
	public java.util.UUID UUID;

	// List of a players claims, the "address" of which looks like world.x.z I.E. world_the_end.53._23
	// In MS namespaces for addresses can't contain dash (-) so we substitute an underscore.
	// Coordinates refer to chunk coordinates.
	public List<String> Claims;

	// Friends are used in determining claim permissions.
	// A player can have "friends" added to a permission list on a claim to allow all players on their friends list.
	// A player should also be able to friend a town and the permission should extend to all members of the town.
	public List<UUID> Friends;

	// Runes are a special kind of currency which determines how much land a player can claim.
	// Runes only increase over time based on votifier captured votes.
	// They can be "in use" in a claim but not actually "spent" so the total number a player has doesn't decrease.
	public int Runes = 16;
	public String Town = null;

	// This claim represents the default settings when a player claims new land.
	public APClaim DefaultClaim;

	// TODO: this isn't right
	public LocalDateTime LastAccessed;// = new LocalDateTime.now();



	public APPlayer(UUID uuid){
		UUID = uuid;
		DefaultClaim = new APClaim(null,UUID,false);
	}

	public static void LoadPlayer(UUID uuid){
		// TODO: load the player from persistence into memory if they exist, if not create a new APPlayer
		if(false /* If the player data exists in persistence*/){
			// Load the player data into memory
		}else{
			// Create new player
		}
	}

	public int getRunesAvailable(){
		int runesAvailable = Runes;
		// for every claim referenced in this.Claims, calculate the amount of runes the claim is using and subtract this from RunesAvailable.
		return runesAvailable;
	}

	public void save(){
		// TODO: update this.LastAccessed
		// TODO: save the player's data to persistence
	}

	public void addRunes(int amount){
		// Runes should never decrease normally unless cheating occured so runes can be removed by using a negative number for amount.
		Runes+= amount;
		save();
	}

	public void addClaim(String claimAddress){
		Claims.add(claimAddress);
		save();
	}

	public void removeClaim(String claimAddress){
		Claims.remove(claimAddress);
		save();
	}

	public void addFriend(UUID friend){
		Friends.add(friend);
		save();
	}

	public void removeFriend(UUID friend){
		Friends.remove(friend);
		save();
	}

	public void joinTown(String town){
		Town = town;
		save();
	}

}
