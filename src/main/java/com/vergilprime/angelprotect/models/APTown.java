package com.vergilprime.angelprotect.models;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class APTown {
	public String townID;
	public String townDisplay;

	// Can promote assistants and has full permission in all town owned claims.
	public UUID mayor;
	public List<UUID> members;

	// Assistants have the ability to claim land for the town and manage claims by default.
	public List<UUID> assistants;

	// Allies can contain both players and other towns
	public List<String> Allies;

	//
	//  Methods
	//

	public int getRunesAvailable(){
		int runesAvailable = 0;
		for (UUID member : members) {
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
		if(!members.contains(nonmember)){
			members.add(nonmember);
			save();
		}
	}

	public void removeMember(UUID member){
		if(members.contains(member)){
			assistants.remove(member);
			members.remove(member);
			save();
		}
	}

	public void promoteAssistant(UUID member){
		if(members.contains(member)){
			assistants.add(member);
			save();
		}
	}

	public void demoteAssistant(UUID member){
		if(assistants.contains(member)){
			assistants.remove(member);
			save();
		}
	}

	public void makeMayor(UUID member){
		if(members.contains(member)){
			assistants.add(mayor);
			mayor = member;
			save();
		}
	}

	//
	// Serialization
	//

	public HashMap<String, Object> serialize(){
		HashMap<String, Object> serializedTown = new HashMap<>();
		serializedTown.put("townID",this.townID);
		serializedTown.put("townDisplay",this.townDisplay);
		serializedTown.put("mayor",this.mayor);
		serializedTown.put("assistants",this.assistants);
		serializedTown.put("members",this.members);

		return serializedTown;
	}
}
