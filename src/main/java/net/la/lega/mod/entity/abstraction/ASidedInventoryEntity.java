package net.la.lega.mod.entity.abstraction;

import blue.endless.jankson.annotation.Nullable;
import net.la.lega.mod.api.ImplementedSidedInventory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;

public abstract class ASidedInventoryEntity extends BlockEntity implements ImplementedSidedInventory
{
    protected DefaultedList<ItemStack> items;
    
    public ASidedInventoryEntity(BlockEntityType<?> entity, int itemStackNumber)
    {
        super(entity);
        items = DefaultedList.ofSize(itemStackNumber, ItemStack.EMPTY);
    }
    
    @Override public DefaultedList<ItemStack> getItems()
    {
        return items;
    }
    
    @Override
    public boolean canPlayerUse(PlayerEntity player)
    {
        if(this.world.getBlockEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.squaredDistanceTo((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }
    
    @Override
    public abstract int[] getAvailableSlots(Direction side);
    
    @Override
    public abstract boolean isValid(int slot, ItemStack stack);
    
    @Override
    public abstract boolean canInsert(int slot, ItemStack stack, Direction dir);
    
    @Override
    public abstract boolean canExtract(int slot, ItemStack stack, Direction dir);
    
    protected boolean canAcceptRecipeOutput(@Nullable Recipe<?> recipe)
    {
        return false;
    }
    
    protected void craftRecipe(@Nullable Recipe<?> recipe)
    {}
    
    @Override
    public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        Inventories.fromTag(tag, items);
    }
    
    
    @Override
    public CompoundTag toTag(CompoundTag tag)
    {
        Inventories.toTag(tag, items);
        return super.toTag(tag);
    }
}
