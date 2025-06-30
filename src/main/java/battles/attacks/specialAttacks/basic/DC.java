package battles.attacks.specialAttacks.basic;

import com.pixelmonmod.api.registry.RegistryValue;
import com.pixelmonmod.pixelmon.api.registries.PixelmonSpecies;
import com.pixelmonmod.pixelmon.battles.attacks.specialAttacks.basic.SpecialAttackBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;

public class DC extends SpecialAttackBase {
    public void applyAfterEffect(PixelmonWrapper pw) {
        if (pw.getSpecies().is(new RegistryValue[]{PixelmonSpecies.MELOETTA})) {
            if (pw.getForm().isForm(new String[]{"hellas"})) {
                pw.setForm("yang");
            } else if (pw.getForm().isForm(new String[]{"yang"})) {
                pw.setForm("hellas");
            }

            pw.bc.sendToAll("pixelmon.abilities.changeform", new Object[]{pw.getNickname()});
        }

    }
}
