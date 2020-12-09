package net.la.lega.mod.entity;

import blue.endless.jankson.annotation.Nullable;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.la.lega.mod.block.BlastChillerBlock;
import net.la.lega.mod.entity.abstraction.AProcessingEntity;
import net.la.lega.mod.gui.handler.BlastChillerBlockGUIHandler;
import net.la.lega.mod.initializer.LEntities;
import net.la.lega.mod.recipe.BlastChillingRecipe;
import net.la.lega.mod.recipe.abstraction.AInjectiveProcessingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;

public class BlastChillerBlockEntity extends AProcessingEntity implements NamedScreenHandlerFactory, ExtendedScreenHandlerFactory
{
    private static final int[] TOP_SLOTS = new int[]{0};
    private static final int[] BOTTOM_SLOTS = new int[]{1};
    private static final int[] SIDE_SLOTS = new int[]{0};
    
    public BlastChillerBlockEntity()
    {
        super(LEntities.BLAST_CHILLER_BLOCK_ENTITY, 2);
    }
    
    @Override
    public int[] getAvailableSlots(Direction side)
    {
        if(side == Direction.DOWN)
        {
            return BOTTOM_SLOTS;
        }
        else
        {
            return side == Direction.UP ? TOP_SLOTS : SIDE_SLOTS;
        }
    }
    
    @Override
    public boolean isValid(int slot, ItemStack stack)
    {
        return slot != 1;
    }
    
    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir)
    {
        return dir != Direction.DOWN && this.isValid(slot, stack);
    }
    
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir)
    {
        if(slot == 1)
        {
            return (dir == Direction.DOWN);
        }
        return false;
    }
    
    @Override
    public void tick()
    {
        if(!this.world.isClient)
        {
            BlastChillingRecipe match = world.getRecipeManager().getFirstMatch(BlastChillingRecipe.Type.INSTANCE, this, world).orElse(null);
            checkCurrentRecipe(match);
            processCurrentRecipe();
            if(isProcessing() && !isOn())
            {
                setOn(true);
            }
            else if(!isProcessing() && isOn())
            {
                setOn(false);
            }
        }
    }
    
    private void setOn(boolean state)
    {
        this.world.setBlockState(this.pos, (BlockState) this.world.getBlockState(this.pos).with(BlastChillerBlock.ON, state), 3);
    }
    
    private boolean isOn()
    {
        return this.world.getBlockState(this.pos).get(BlastChillerBlock.ON);
    }
    
    @Override
    protected boolean canAcceptRecipeOutput(Recipe<?> recipe)
    {
        AInjectiveProcessingRecipe bcRecipe = (AInjectiveProcessingRecipe) recipe;
        if(!((ItemStack) this.items.get(0)).isEmpty() && recipe != null)
        {
            ItemStack itemStack = bcRecipe.getOutput();
            if(itemStack.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack itemStack2 = (ItemStack) this.items.get(1);
                if(itemStack2.isEmpty())
                {
                    return true;
                }
                else if(!itemStack2.isItemEqualIgnoreDamage(itemStack))
                {
                    return false;
                }
                else if(itemStack2.getCount() + bcRecipe.getOutputAmount() <= this.getMaxCountPerStack()
                      && itemStack2.getCount() + bcRecipe.getOutputAmount() <= itemStack2.getMaxCount())
                {
                    return true;
                }
                else
                {
                    return itemStack2.getCount() < itemStack.getMaxCount();
                }
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override
    protected void craftRecipe(@Nullable Recipe<?> recipe)
    {
        AInjectiveProcessingRecipe bcRecipe = (AInjectiveProcessingRecipe) recipe;
        if(recipe != null && this.canAcceptRecipeOutput(recipe))
        {
            ItemStack inputSlot = (ItemStack) this.items.get(0);
            ItemStack output = bcRecipe.craft(this);
            ItemStack outputSlot = (ItemStack) this.items.get(1);
            if(outputSlot.isEmpty())
            {
                this.items.set(1, output.copy());
            }
            else if(outputSlot.getItem() == output.getItem())
            {
                outputSlot.increment(bcRecipe.getOutputAmount());
            }
            inputSlot.decrement(1);
        }
    }
    
    @Override public Text getDisplayName()
    {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
    
    @Override
    public @org.jetbrains.annotations.Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
    {
        return new BlastChillerBlockGUIHandler(syncId, inv, ScreenHandlerContext.create(world, pos));
    }
    
    @Override public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf)
    {
        packetByteBuf.writeBlockPos(pos);
    }
}