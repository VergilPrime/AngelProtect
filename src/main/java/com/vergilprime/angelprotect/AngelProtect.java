package com.vergilprime.angelprotect;

import com.vergilprime.angelprotect.models.APClaim;
import com.vergilprime.angelprotect.models.APPlayer;
import com.vergilprime.angelprotect.models.APTown;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class AngelProtect extends JavaPlugin {
	private HashMap<UUID, APPlayer> loadedPlayers;
	private HashMap<String, APClaim> loadedClaims;
	private HashMap<String, APTown> towns;
	private String hostname, database, username, password;
	private Integer port;
	private File claimsDir = new File("claims");
	private File configFile = new File("config.yml");
	private File playersFile = new File("players.yml");

	@Override
	public void onEnable() {


		if(!claimsDir.exists()){
			claimsDir.mkdirs();
		}

		if(!configFile.exists()){
			try {
				configFile.createNewFile();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}

		if(!playersFile.exists()){
			try {
				playersFile.createNewFile();
			}catch(IOException ex){
				ex.printStackTrace();
			}
		}


	}

	@Override
	public void onDisable() {
	}

	private void loadPlayer(UUID uuid){
		loadedPlayers.put(uuid, APPlayer.loadPlayer(uuid));
	}

	private void loadClaim(String address){
		loadedClaims.put(address, APClaim.loadClaim(address));
	}

	// For later: https://www.spigotmc.org/wiki/connecting-to-databases-mysql/#setting-up-a-connection
}
