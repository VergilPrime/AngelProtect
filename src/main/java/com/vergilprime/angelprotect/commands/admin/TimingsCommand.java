package com.vergilprime.angelprotect.commands.admin;

import com.vergilprime.angelprotect.commands.CommandHandler;
import com.vergilprime.angelprotect.datamodels.APConfig;
import com.vergilprime.angelprotect.utils.C;
import com.vergilprime.angelprotect.utils.UtilString;
import com.vergilprime.angelprotect.utils.UtilTiming;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimingsCommand extends CommandHandler {

    private List<String> actions = Arrays.asList("start", "stop", "status", "reset", "dump");

    public TimingsCommand() {
        super("timings", "Start/stop AP timings");
    }

    @Override
    public void onCommand(CommandSender sender, String cmd, String[] args) {
        if (args.length == 0 || !actions.contains(args[0].toLowerCase())) {
            sender.sendMessage(C.usage("/aap timings start", "Start timings."));
            sender.sendMessage(C.usage("/aap timings stop", "Stop timings and write to file."));
            sender.sendMessage(C.usage("/aap timings reset", "Reset timings."));
            sender.sendMessage(C.usage("/aap timings dump", "Dump timings but continue running."));
            sender.sendMessage(C.usage("/aap timings status", "Tells if timings is currently running."));
            return;
        }
        String action = args[0].toLowerCase();
        boolean running = APConfig.get().doTimings;
        if (action.equals("start")) {
            if (running) {
                sender.sendMessage(C.error("Timings is already running."));
                sender.sendMessage(C.error("Consider using reset or stop instead."));
            } else {
                APConfig.get().doTimings = true;
                UtilTiming.resetAll();
                sender.sendMessage(C.main("Timing reset and started."));
            }
        } else if (action.equals("stop")) {
            if (!running) {
                sender.sendMessage(C.error("Timings is not currently running."));
                sender.sendMessage(C.error("Consider running start first."));
            } else {
                UtilTiming.dumpFiles();
                APConfig.get().doTimings = false;
                sender.sendMessage(C.main("Stopped timings and dumped to files."));
            }
        } else if (action.equals("status")) {
            sender.sendMessage(C.main("Timings is currently " + (running ? "" : "not ") + "running."));
        } else if (action.equals("dump")) {
            if (!running) {
                sender.sendMessage(C.error("Timings is currently not running, nothing to dump."));
            } else {
                UtilTiming.dumpFiles();
                sender.sendMessage(C.main("Dumped timings to file, continue running."));
            }
        } else if (action.equals("reset")) {
            if (!running) {
                sender.sendMessage(C.error("Timings is currently not running, nothing to reset."));
            } else {
                UtilTiming.resetAll();
                sender.sendMessage(C.main("Reset timings, continue running."));
            }
        } else {
            sender.sendMessage(C.error("Unknown timings action '" + action + "'."));
        }

    }

    @Override
    public List<String> onTab(CommandSender sender, String cmd, String[] args) {
        if (args.length == 1) {
            return UtilString.filterPrefixIgnoreCase(args[0], actions);
        }
        return Collections.EMPTY_LIST;
    }
}
