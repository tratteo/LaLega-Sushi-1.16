package net.la.lega.mod.initializer;

import net.la.lega.mod.dispense_behaviour.AvocadoDispenserBehavior;
import net.la.lega.mod.dispense_behaviour.FryerOilDispenserBehavior;
import net.la.lega.mod.dispense_behaviour.SteamCookerDispenserBehavior;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.Items;

public final class LBehaviors
{
    public static void initialize()
    {
        DispenserBlock.registerBehavior(Items.SHEARS, new AvocadoDispenserBehavior());
        DispenserBlock.registerBehavior(LItems.SUNFLOWER_OIL_BOTTLE, new FryerOilDispenserBehavior());
        DispenserBlock.registerBehavior(LItems.RICE_OIL_BOTTLE, new FryerOilDispenserBehavior());
        DispenserBlock.registerBehavior(Items.WATER_BUCKET, new SteamCookerDispenserBehavior());
    }
}