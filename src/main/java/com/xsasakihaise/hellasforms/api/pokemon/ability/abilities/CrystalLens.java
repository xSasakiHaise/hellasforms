package com.xsasakihaise.hellasforms.api.pokemon.ability.abilities;

import com.pixelmonmod.pixelmon.api.pokemon.ability.AbstractAbility;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.attacks.Value;
import com.pixelmonmod.pixelmon.battles.controller.ai.MoveChoice;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import java.util.List;

public class CrystalLens extends AbstractAbility {
    public boolean ignoreEvasion(PixelmonWrapper pw, PixelmonWrapper target) {
        return true;
    }
    public void weightEffect(PixelmonWrapper pw, MoveChoice userChoice, List<MoveChoice> userChoices, List<MoveChoice> bestUserChoices, List<MoveChoice> opponentChoices, List<MoveChoice> bestOpponentChoices) {
        if (userChoice.isOffensiveMove()) {
            userChoice.raiseWeight((float)this.stages / 10.0F);
        } else if (pw.getBattleStats().increaseCritStage(1, false)) {
            userChoice.raiseWeight(25.0F);
        }

    }
}