package com.xsasakihaise.hellasforms.enums;

import com.pixelmonmod.pixelmon.api.pokemon.Element;

public enum HellasMoves {
    Corrode(1, "Corrode", Element.POISON),
    PlasmaVeil(2, "Plasma Veil", Element.ELECTRIC),
    HitchKick(3, "Hitch Kick", Element.FIGHTING);

    private final int id;
    private final String moveName;
    private final Element moveType;

    private HellasMoves(int id, String moveName, Element moveType) {
        this.id = id;
        this.moveName = moveName;
        this.moveType = moveType;
    }

    public int getId() {
        return this.id;
    }

    public String getMoveName() {
        return this.moveName;
    }

    public Element getMoveType() {
        return this.moveType;
    }
}
