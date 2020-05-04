package com.vergilprime.angelprotect.datamodels.claimparts;

import com.vergilprime.angelprotect.datamodels.APConfig;

import java.io.Serializable;

public class Protections implements Serializable {

    private static final long serialVersionUID = 1957796085560132961L;

    public boolean fire = true;
    public boolean tnt = true;
    public boolean mob = false;
    public boolean pvp = false;
    public boolean container = false;

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
