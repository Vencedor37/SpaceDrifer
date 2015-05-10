package com.mgd.davcw002;

/**
 * Created by Campbell on 8/05/2015.
 */
public class Reward extends Entity {
    public enum TYPES {
        HEALTH, FUEL
    }

    private TYPES type;

    public Reward(TYPES type) {
        this.type = type;
    }

    public TYPES getType() {
        return this.type;
    }
}
