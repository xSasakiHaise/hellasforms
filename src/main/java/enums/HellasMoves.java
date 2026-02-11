package enums;

import com.pixelmonmod.pixelmon.api.pokemon.type.Type;
import net.minecraft.resources.ResourceKey;

public enum HellasMoves {
    Corrode(1, "Corrode", Type.POISON);

    private final int id;
    private final String moveName;
    private final ResourceKey<Type> moveType;

    HellasMoves(int id, String moveName, ResourceKey<Type> moveType) {
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

    public ResourceKey<Type> getMoveType() {
        return this.moveType;
    }
}
