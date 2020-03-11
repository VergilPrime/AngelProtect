package com.vergilprime.angelprotect.models;

import java.util.List;
import java.util.UUID;

public class APTown {
	public String TownID;
	public String TownDisplay;

	// Can promote assistants and has full permission in all town owned claims.
	public UUID Mayor;
	public List<UUID> Members;

	// Assistants have the ability to claim land for the town and manage claims by default.
	public List<UUID> Assistants;

	// Allies can contain both players and other towns
	public List<String> Allies;



	public int getRunesAvailable(){
		int runesAvailable = 0;
		for (UUID member : Members) {
			int impact = 0;
			// Using UUID, get the player's total runes, subtract 32 and assign this to "impact"
			impact -= 32;
			runesAvailable += impact;
		}
		// for every claim referenced in this.Claims, calculate the amount of runes the claim is using and subtract this from RunesAvailable.
		return runesAvailable;
	}

	public void save(){
		// TODO: save the town's data to persistence
	}

	public void addMember(UUID nonmember){
		if(!Members.contains(nonmember)){
			Members.add(nonmember);
			save();
		}
	}

	public void removeMember(UUID member){
		if(Members.contains(member)){
			Assistants.remove(member);
			Members.remove(member);
			save();
		}
	}

	public void promoteAssistant(UUID member){
		if(Members.contains(member)){
			Assistants.add(member);
			save();
		}
	}

	public void demoteAssistant(UUID member){
		if(Assistants.contains(member)){
			Assistants.remove(member);
			save();
		}
	}

	public void makeMayor(UUID member){
		if(Members.contains(member)){
			Assistants.add(Mayor);
			Mayor = member;
			save();
		}
	}
}
