package com.vergilprime.angelprotect.datamodels.claimparts;

import com.vergilprime.angelprotect.datamodels.APConfig;

import java.io.Serializable;

public class Protections implements Serializable {

    private static final long serialVersionUID = 1957796085560132961L;

    private boolean fire = true;
    private boolean tnt = true;
    private boolean mob = false;
    private boolean pvp = false;
    private boolean container = false;

    public Protections() {
    }

    public Protections(boolean fire, boolean tnt, boolean mob, boolean pvp, boolean container) {
        this.fire = fire;
        this.tnt = tnt;
        this.mob = mob;
        this.pvp = pvp;
        this.container = container;
    }

    public int getCost() {
        APConfig config = APConfig.get();
        int cost = config.claimCost;
        if (mob) {
            cost += config.protectionMobCost;
        }
        if (pvp) {
            cost += config.protectionPVPCost;
        }
        if (container) {
            cost += config.protectionContainerCost;
        }
        return cost;
    }

    public boolean isFire() {
        return fire;
    }

    public boolean isTnt() {
        return tnt;
    }

    public boolean isMob() {
        return mob;
    }

    public boolean isPvp() {
        return pvp;
    }

    public boolean isContainer() {
        return container;
    }

    @Override
    public Protections clone() {
        Protections prot = new Protections();
        prot.fire = fire;
        prot.tnt = tnt;
        prot.mob = mob;
        prot.pvp = pvp;
        prot.container = container;
        return prot;
    }
}
