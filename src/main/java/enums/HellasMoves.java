package enums;

import battles.attacks.specialAttacks.basic.DivineCastigation;
import com.pixelmonmod.pixelmon.api.pokemon.Element;

public enum HellasMoves {
    Corrode(1, "Corrode", Element.POISON),
    DivineCastigation(2, "Divine Castigation", Element.FIGHTING);

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
