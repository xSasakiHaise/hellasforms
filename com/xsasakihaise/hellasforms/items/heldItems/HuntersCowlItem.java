package com.xsasakihaise.hellasforms.items.heldItems;

import com.pixelmonmod.pixelmon.battles.controller.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.battles.status.Sunny;
import com.pixelmonmod.pixelmon.battles.status.Weather;
import com.pixelmonmod.pixelmon.enums.heldItems.EnumHeldItems;
import com.pixelmonmod.pixelmon.items.HeldItem;
import net.minecraft.item.Item;

public class HuntersCowlItem extends HeldItem {
    public HuntersCowlItem() {
        super(EnumHeldItems.other, new Item.Properties());
    }

    public void applySwitchInEffect(PixelmonWrapper newPokemon) {
        if (newPokemon.getSpecies().getName().equals("Treecko") && newPokemon.getForm().getName().equals("hellas") && newPokemon.bc.globalStatusController.getWeather() instanceof Sunny) {
            newPokemon.bc.sendToAll("hellasforms.helditems.hunters_cowl_start", new Object[]{newPokemon.getNickname()});
        }

    }

    public void onWeatherSwitch(BattleController bc, PixelmonWrapper user, Weather weather) {
        if (user.getSpecies().getName().equals("Treecko") && user.getForm().getName().equals("hellas") && user.bc.globalStatusController.getWeather() instanceof Sunny) {
            user.bc.sendToAll("hellasforms.helditems.hunters_cowl_start", new Object[]{user.getNickname()});
        }

        if (user.getSpecies().getName().equals("Treecko") && user.getForm().getName().equals("hellas") && !(user.bc.globalStatusController.getWeather() instanceof Sunny)) {
            user.bc.sendToAll("hellasforms.helditems.hunters_cowl_end", new Object[]{user.getNickname()});
        }

    }

    public void applyRepeatedEffect(PixelmonWrapper pw) {
        if (!pw.hasFullHealth() && !pw.hasStatus(new StatusType[]{StatusType.HealBlock}) && pw.getSpecies().getName().equals("Treecko") && pw.getForm().getName().equals("hellas") && pw.bc.globalStatusController.getWeather() instanceof Sunny) {
            int par1 = (int)((float)pw.getMaxHealth() * 0.1F);
            if (pw.healEntityBy(par1) > 0 && pw.bc != null) {
                pw.bc.sendToAll("hellasforms.helditems.hunters_cowl_heal", new Object[]{pw.getNickname()});
            }
        }

    }
}
