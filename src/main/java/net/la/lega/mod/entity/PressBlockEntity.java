package net.la.lega.mod.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.la.lega.mod.entity.abstraction.AProcessingEntity;
import net.la.lega.mod.gui.controller.PressBlockGUIHandler;
import net.la.lega.mod.initializer.LEntities;
import net.la.lega.mod.initializer.LSounds;
import net.la.lega.mod.recipe.PressingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

public class PressBlockEntity extends AProcessingEntity implements NamedScreenHandlerFactory, ExtendedScreenHandlerFactory
{
    public static final int OUTPUT_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int INPUT2_SLOT = 2;
    
    private float progress;
    private float lastProgress;
    private boolean soundPlaying = false;
    private int delay = 15;
    private int currentDelay;
    
    public PressBlockEntity()
    {
        super(LEntities.PRESS_BLOCK_ENTITY, 3);
    }
    
    @Override public int[] getAvailableSlots(Direction side)
    {
        if(side.equals(Direction.DOWN))
        {
            return new int[]{OUTPUT_SLOT};
        }
        else
        {
            return new int[]{INPUT_SLOT, INPUT2_SLOT};
        }
    }
    
    private boolean isItemAlreadyPresent(Item item, int interestedSlot)
    {
        for(int i = 0; i < 2; i++)
        {
            if(i != interestedSlot && item == items.get(i).getItem())
            {
                return true;
            }
        }
        return false;
    }
    
    @Override public boolean isValid(int slot, ItemStack stack)
    {
        return slot != OUTPUT_SLOT && !isItemAlreadyPresent(stack.getItem(), slot);
    }
    
    @Override public boolean canInsert(int slot, ItemStack stack, Direction dir)
    {
        return isValid(slot, stack);
    }
    
    @Override public boolean canExtract(int slot, ItemStack stack, Direction dir)
    {
        return slot == OUTPUT_SLOT && dir == Direction.DOWN;
    }
    
    @Override protected boolean canAcceptRecipeOutput(Recipe<?> recipe)
    {
        PressingRecipe bcRecipe = (PressingRecipe) recipe;
        if(bcRecipe != null)
        {
            ItemStack outputStack = bcRecipe.getOutput();
            if(outputStack.isEmpty())
            {
                return false;
            }
            else
            {
                ItemStack currentOutputStack = (ItemStack) this.items.get(OUTPUT_SLOT);
                if(currentOutputStack.isEmpty())
                {
                    return true;
                }
                else if(!currentOutputStack.isItemEqualIgnoreDamage(outputStack))
                {
                    return false;
                }
                else
                {
                    return currentOutputStack.getCount() + bcRecipe.getOutputAmount() <= this.getMaxCountPerStack();
                }
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override protected void craftRecipe(Recipe<?> recipe)
    {
        PressingRecipe bcRecipe = (PressingRecipe) recipe;
        if(bcRecipe != null && this.canAcceptRecipeOutput(bcRecipe))
        {
            ItemStack output = bcRecipe.craft(this);
            ItemStack outputSlot = (ItemStack) this.items.get(OUTPUT_SLOT);
            
            if(outputSlot.isEmpty())
            {
                this.items.set(OUTPUT_SLOT, output);
            }
            else if(outputSlot.getItem() == output.getItem())
            {
                outputSlot.increment(bcRecipe.getOutputAmount());
            }
            
            if(!items.get(INPUT_SLOT).isEmpty())
            {
                items.get(INPUT_SLOT).decrement(1);
            }
            if(!items.get(INPUT2_SLOT).isEmpty())
            {
                items.get(INPUT2_SLOT).decrement(1);
            }
            currentDelay = 0;
        }
    }
    
    @Override public void tick()
    {
        if(!world.isClient)
        {
            PressingRecipe match = world.getRecipeManager().getFirstMatch(PressingRecipe.Type.INSTANCE, new SimpleInventory(items.get(INPUT_SLOT), items.get(INPUT2_SLOT)), world).orElse(null);
            checkCurrentRecipe(match);
            if(match != null)
            {
                if(currentDelay < delay)
                {
                    currentDelay++;
                    return;
                }
            }
            else
            {
                currentDelay = 0;
            }
            lastProgress = progress;
            processCurrentRecipe();
            float unitPT = getCurrentUnitProcessingTime();
            if(unitPT == 0)
            {
                progress = 0;
                lastProgress = progress;
            }
            else
            {
                progress = getCurrentProcessingTime() / unitPT;
            }
            sync();
        }
        else if(world.isClient)
        {
            if(getCurrentUnitProcessingTime() != 0)
            {
                if(!soundPlaying)
                {
                    world.playSound(pos.getX() + 1D, pos.getY() + 1, pos.getZ() + 1, LSounds.PRESS_ACTIVATE_SOUNDEVENT, SoundCategory.BLOCKS, 0.25F, 0.9F, false);
                    soundPlaying = true;
                }
            }
            else
            {
                soundPlaying = false;
            }
        }
    }
    
    private float getDeltaProgress(float tickDelta)
    {
        if(tickDelta > 1.0F)
        {
            tickDelta = 1.0F;
        }
        return MathHelper.lerp(tickDelta, this.lastProgress, this.progress);
    }
    
    @Environment(EnvType.CLIENT)
    public float getExtensionAmount(float tickDelta)
    {
        float progress = getDeltaProgress(tickDelta);
        return progress < 0.5F ? progress * 0.5F : (1 - progress) * 0.5F;
    }
    
    @Override public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        progress = tag.getFloat("progress");
        soundPlaying = tag.getBoolean("soundPlaying");
        lastProgress = progress;
    }
    
    @Override public CompoundTag toTag(CompoundTag tag)
    {
        tag.putFloat("progress", progress);
        tag.putFloat("lastProgress", lastProgress);
        tag.putBoolean("soundPlaying", soundPlaying);
        return super.toTag(tag);
    }
    
    @Override public void fromClientTag(CompoundTag tag)
    {
        super.fromClientTag(tag);
        progress = tag.getFloat("progress");
        lastProgress = progress;
    }
    
    @Override public CompoundTag toClientTag(CompoundTag tag)
    {
        tag.putFloat("progress", progress);
        return super.toClientTag(tag);
    }
    
    @Override public Text getDisplayName()
    {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
    
    @Override public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
    {
        return new PressBlockGUIHandler(syncId, inv, ScreenHandlerContext.create(world, pos));
    }
    
    @Override public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf)
    {
        packetByteBuf.writeBlockPos(pos);
    }
}
