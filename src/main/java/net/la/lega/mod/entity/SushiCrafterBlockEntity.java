package net.la.lega.mod.entity;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.la.lega.mod.entity.abstraction.AProcessingEntity;
import net.la.lega.mod.gui.controller.SushiCrafterBlockGUIHandler;
import net.la.lega.mod.initializer.LEntities;
import net.la.lega.mod.initializer.LItems;
import net.la.lega.mod.initializer.LTags;
import net.la.lega.mod.initializer.LVillagerProfessions;
import net.la.lega.mod.recipe.SushiCraftingRecipe;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class SushiCrafterBlockEntity extends AProcessingEntity implements NamedScreenHandlerFactory, ExtendedScreenHandlerFactory
{
    public static final int OUTPUT_SLOT = 0;
    public static final int[] REQUIRED_SLOTS = new int[]{1, 2, 3, 4};
    public static final int[] INGREDIENTS_SLOTS = new int[]{5, 6, 7, 8, 9, 10, 11, 12};
    
    private VillagerEntity sushiMan = null;
    
    private boolean isRequiredSlot(int slot)
    {
        return Arrays.stream(REQUIRED_SLOTS).anyMatch(p -> p == slot);
    }
    
    private boolean isRequiredTypeItem(Item item)
    {
        return item.isIn(LTags.SUSHI_REQUIRED);
    }
    
    private boolean isItemAlreadyPresent(Item item, int interestedSlot)
    {
        for(int i = 0; i < REQUIRED_SLOTS.length; i++)
        {
            if(i != interestedSlot && item == items.get(i).getItem())
            {
                return true;
            }
        }
        for(int i = 0; i < INGREDIENTS_SLOTS.length; i++)
        {
            if(i != interestedSlot && item == items.get(i).getItem())
            {
                return true;
            }
        }
        return false;
    }
    
    private boolean isIngredientSlot(int slot)
    {
        return Arrays.stream(INGREDIENTS_SLOTS).anyMatch(p -> p == slot);
    }
    
    private boolean isIngredient(Item item)
    {
        return item.isIn(LTags.SUSHI_INGREDIENT);
    }
    
    private int[] TOP_SLOTS;
    private int[] BOTTOM_SLOTS = new int[]{OUTPUT_SLOT};
    private int[] SIDE_SLOTS;
    
    public SushiCrafterBlockEntity()
    {
        super(LEntities.SUSHI_CRAFTER_BLOCK_ENTITY, 13);
        calculateSlots();
    }
    
    private void calculateSlots()
    {
        TOP_SLOTS = Arrays.copyOf(REQUIRED_SLOTS, REQUIRED_SLOTS.length + INGREDIENTS_SLOTS.length);
        System.arraycopy(INGREDIENTS_SLOTS, 0, TOP_SLOTS, REQUIRED_SLOTS.length, INGREDIENTS_SLOTS.length);
        SIDE_SLOTS = TOP_SLOTS;
    }
    
    @Override
    public int[] getAvailableSlots(Direction side)
    {
        switch(side)
        {
            case UP:
                return TOP_SLOTS;
            case DOWN:
                return BOTTOM_SLOTS;
            default:
                return SIDE_SLOTS;
        }
    }
    
    @Override
    public boolean isValid(int slot, ItemStack stack)
    {
        Item stackItem = stack.getItem();
        if(slot == OUTPUT_SLOT)
        {
            return false;
        }
        else if(isRequiredSlot(slot) && isRequiredTypeItem(stackItem))
        {
            return !isItemAlreadyPresent(stackItem, slot);
        }
        else if(isIngredientSlot(slot) && isIngredient(stackItem))
        {
            return !isItemAlreadyPresent(stackItem, slot);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public boolean canInsert(int slot, ItemStack stack, Direction dir)
    {
        return slot != OUTPUT_SLOT;
    }
    
    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir)
    {
        return slot == OUTPUT_SLOT && dir == Direction.DOWN;
    }
    
    @Override
    protected boolean canAcceptRecipeOutput(Recipe<?> recipe)
    {
        SushiCraftingRecipe bcRecipe = (SushiCraftingRecipe) recipe;
        if(bcRecipe != null && areRequiredSlotNotEmpty())
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
                else if(currentOutputStack.getCount() + bcRecipe.getOutputAmount() <= this.getMaxCountPerStack())
                {
                    return true;
                }
                else
                {
                    return currentOutputStack.getCount() < outputStack.getMaxCount();
                }
            }
        }
        else
        {
            return false;
        }
    }
    
    @Override
    protected void craftRecipe(Recipe<?> recipe)
    {
        SushiCraftingRecipe bcRecipe = (SushiCraftingRecipe) recipe;
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
            
            for(int i = 0; i < INGREDIENTS_SLOTS.length; i++)
            {
                ItemStack current = items.get(INGREDIENTS_SLOTS[i]);
                if(!current.isEmpty())
                {
                    current.decrement(1);
                }
            }
            for(int i = 0; i < REQUIRED_SLOTS.length; i++)
            {
                ItemStack current = items.get(REQUIRED_SLOTS[i]);
                if(!current.isEmpty())
                {
                    current.decrement(1);
                }
            }
        }
    }
    
    @Override
    public void tick()
    {
        if(!this.world.isClient)
        {
            if(isSushiManNear())
            {
                SushiCraftingRecipe match = world.getRecipeManager().getFirstMatch(SushiCraftingRecipe.Type.INSTANCE, calculateCurrentInventory(), world).orElse(null);
                if(match != null && match.getOutput().getItem().equals(LItems.PUFFER_FISH_FILLET))
                {
                    match = canSushiManCutPufferFish() ? match : null;
                }
                checkCurrentRecipe(match);
                processCurrentRecipe();
            }
        }
    }
    
    private SimpleInventory calculateCurrentInventory()
    {
        SimpleInventory inv = new SimpleInventory(items.size() - 1);
        for(int i = 0; i < REQUIRED_SLOTS.length; i++)
        {
            inv.addStack(items.get(REQUIRED_SLOTS[i]));
        }
        for(int i = 0; i < INGREDIENTS_SLOTS.length; i++)
        {
            inv.addStack(items.get(INGREDIENTS_SLOTS[i]));
        }
        return inv;
    }
    
    private boolean areRequiredSlotNotEmpty()
    {
        for(int i = 0; i < REQUIRED_SLOTS.length; i++)
        {
            if(!items.get(REQUIRED_SLOTS[i]).isEmpty())
            {
                return true;
            }
        }
        return false;
    }
    
    private boolean isSushiManNear()
    {
        List<VillagerEntity> villagers = world.getNonSpectatingEntities(VillagerEntity.class, (new Box(getPos())).expand(5D, 5D, 5D));
        for(VillagerEntity villager : villagers)
        {
            if(villager.getVillagerData().getProfession() == LVillagerProfessions.SUSHI_MAN_PROFESSION)
            {
                sushiMan = villager;
                return true;
            }
        }
        sushiMan = null;
        return false;
    }
    
    private boolean canSushiManCutPufferFish()
    {
        return sushiMan.getVillagerData().getLevel() >= 3;
    }
    
    @Override public Text getDisplayName()
    {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
    
    @Override public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
    {
        return new SushiCrafterBlockGUIHandler(syncId, inv, ScreenHandlerContext.create(world, pos));
    }
    
    @Override public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf)
    {
        packetByteBuf.writeBlockPos(pos);
    }
}
