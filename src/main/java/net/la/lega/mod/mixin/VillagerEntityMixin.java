package net.la.lega.mod.mixin;

import com.google.common.collect.ImmutableSet;
import net.la.lega.mod.initializer.LItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.VillagerData;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity
{
    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world)
    {
        super(entityType, world);
    }
    
    @Shadow private static Set<Item> GATHERABLE_ITEMS;
    
    @Shadow public abstract VillagerData getVillagerData();
    
    @Inject(at = @At("HEAD"), method = "hasSeedToPlant", cancellable = true)
    private void hasSeedToPlant(CallbackInfoReturnable<Boolean> info)
    {
        SimpleInventory basicInventory = this.getInventory();
        info.setReturnValue(basicInventory.containsAny(ImmutableSet.of(Items.WHEAT_SEEDS, Items.POTATO, Items.CARROT, Items.BEETROOT_SEEDS, LItems.RICE_SEEDS, LItems.WASABI_ROOT)));
        info.cancel();
    }
    
    @Inject(at = @At("HEAD"), method = "canGather", cancellable = true)
    private void canGather(ItemStack stack, CallbackInfoReturnable<Boolean> info)
    {
        Item item = stack.getItem();
        info.setReturnValue(GATHERABLE_ITEMS.contains(item) || this.getVillagerData().getProfession().getGatherableItems().contains(item) || item.equals(LItems.RICE_SEEDS) || item.equals(LItems.WASABI_ROOT));
        info.cancel();
    }
}