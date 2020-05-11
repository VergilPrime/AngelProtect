package com.vergilprime.angelprotect;

import com.vergilprime.angelprotect.datamodels.APEntityRelation;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.Debug;
import com.vergilprime.angelprotect.utils.UtilSerialize;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.UUID;

public class Tester implements Listener {

    public Tester(AngelProtect main) {
        Bukkit.getPluginManager().registerEvents(this, main);

//        runTests();

    }

    public void runTests() {
        Debug.log(" ############### Running Tests ###############");

        APPlayer xadudex = new APPlayer(UUID.fromString("fc0cfaa2-c73e-4f8e-8557-d7655641b85c"));
        APPlayer vergilprime = new APPlayer(UUID.fromString("82bd54e2-46b5-415b-80c8-e960c9e64599"));
        APPlayer spiritussanctus = new APPlayer(UUID.fromString("a9d0c072-bf19-40a1-a193-68d09d50026"));

        APTown townA = new APTown(UUID.randomUUID(), "Town1", xadudex);
        APTown townB = new APTown(UUID.randomUUID(), "Town2", spiritussanctus);
        townB.addMember(vergilprime);

        townB.addAlly(new APEntityRelation(xadudex));
        townB.addAlly(new APEntityRelation(townA));

        xadudex.save();
        vergilprime.save();
        spiritussanctus.save();
        townA.save();
        townB.save();

        Debug.log("xADudex profile:\n" + UtilSerialize.toYaml(xadudex));
        Debug.log(C.line);
        Debug.log("VergilPrime profile:\n" + UtilSerialize.toYaml(vergilprime));
        Debug.log(C.line);
        Debug.log("SpiritusSanctus profile:\n" + UtilSerialize.toYaml(spiritussanctus));
        Debug.log(C.line);
        Debug.log("Town A:\n" + UtilSerialize.toYaml(townA));
        Debug.log(C.line);
        Debug.log("Town B:\n" + UtilSerialize.toYaml(townB));
        Debug.log(C.line);

        Debug.log(" ############### Finished Tests ###############");
    }

    @EventHandler
    public void onTestCommand(PlayerCommandPreprocessEvent event) {
        String cmd = event.getMessage().split(" ")[0];
        String[] args = event.getMessage().split(" ");
        if (Arrays.asList("/apt", "/aptest").contains(cmd.toLowerCase())) {
            event.getPlayer().sendMessage("Test cmd.");

            event.setCancelled(true);
        }
    }
}
