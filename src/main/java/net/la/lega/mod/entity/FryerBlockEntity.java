package net.la.lega.mod.entity;

import io.github.cottonmc.cotton.gui.PropertyDelegateHolder;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.la.lega.mod.api.LimitedQueue;
import net.la.lega.mod.api.ProcessableRecipeObject;
import net.la.lega.mod.block.FryerBlock;
import net.la.lega.mod.entity.abstraction.ASidedInventoryEntity;
import net.la.lega.mod.gui.handler.FryerBlockGUIHandler;
import net.la.lega.mod.initializer.LEntities;
import net.la.lega.mod.model_enum.OilType;
import net.la.lega.mod.recipe.FryingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class FryerBlockEntity extends ASidedInventoryEntity implements Tickable, PropertyDelegateHolder, NamedScreenHandlerFactory, ExtendedScreenHandlerFactory
{
    public static final int OUTPUT_SLOT = 0;
    public static final int PROCESSING_SLOT = 1;
    public static final int INPUT_SLOT = 2;
    private static final int MAX_OIL_USAGE = 128;
    
    public static final int CURRENT_OIL_USAGE = 0;
    public static final int MAX_USAGE = 1;
    
    private int currentOilUsage;
    private int maxOilUsage;
    private int inverseOilUsage;
    private LimitedQueue<ProcessableRecipeObject<FryingRecipe>> processingBatch;
    
    //#region Property Delegate
    private final PropertyDelegate propertyDelegate = new PropertyDelegate()
    {
        public int get(int key)
        {
            switch(key)
            {
                case CURRENT_OIL_USAGE:
                    if(getOilType() == OilType.NONE) return 0;
                    return inverseOilUsage;
                case MAX_USAGE:
                    return maxOilUsage;
                default:
                    return 0;
            }
        }
        
        public void set(int key, int value)
        {
            switch(key)
            {
                case CURRENT_OIL_USAGE:
                    inverseOilUsage = value;
                    break;
                case MAX_USAGE:
                    maxOilUsage = value;
                    break;
            }
        }
        
        public int size()
        {
            return 2;
        }
    };
    
    public final Supplier<String> OilTypeSupplier = () ->
    {
        OilType type = getOilType();
        switch(type)
        {
            case NONE:
                return "None";
            case SUNFLOWER_OIL:
                return "Sunflower Oil";
            case RICE_OIL:
                return "Rice Oil";
            default: return "";
        }
    };
    public final Supplier<String> OilUsageSupplier = () ->
    {
        OilType type = getOilType();
        if(type == OilType.NONE) return "";
        int val = (int) (((float) inverseOilUsage / maxOilUsage) * 100F);
        val = val < 0 ? 0 : val;
        return  val + "%";
    };
    //#endregion
    
    public FryerBlockEntity()
    {
        super(LEntities.FRYER_BLOCK_ENTITY, 3);
        processingBatch = new LimitedQueue<>(3);
        maxOilUsage = MAX_OIL_USAGE;
    }
    
    @Override public int[] getAvailableSlots(Direction side)
    {
        if(side.equals(Direction.DOWN))
        {
            return new int[]{OUTPUT_SLOT};
        }
        else
        {
            return new int[]{INPUT_SLOT};
        }
    }
    
    @Override public boolean isValid(int slot, ItemStack stack)
    {
        return slot == INPUT_SLOT;
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
        FryingRecipe bcRecipe = (FryingRecipe) recipe;
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
                    return currentOutputStack.getCount() + bcRecipe.getOutputAmount() + processingBatch.size() <= this.getMaxCountPerStack();
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
        FryingRecipe fRecipe = (FryingRecipe) recipe;
        if(fRecipe != null && this.canAcceptRecipeOutput(fRecipe))
        {
            ItemStack output = fRecipe.craft(this);
            ItemStack outputSlot = (ItemStack) this.items.get(OUTPUT_SLOT);
            if(outputSlot.isEmpty())
            {
                this.items.set(OUTPUT_SLOT, output);
            }
            else if(outputSlot.getItem() == output.getItem())
            {
                outputSlot.increment(fRecipe.getOutputAmount());
            }
            
            items.get(PROCESSING_SLOT).decrement(1);
        }
    }
    
    
    @Override public void tick()
    {
        if(!world.isClient)
        {
            inverseOilUsage = MAX_OIL_USAGE - currentOilUsage;
            FryingRecipe match = world.getRecipeManager().getFirstMatch(FryingRecipe.Type.INSTANCE, new SimpleInventory(items.get(INPUT_SLOT)), world).orElse(null);
            if(match != null)
            {
                if(isOilValid())
                {
                    Item inputItem = items.get(INPUT_SLOT).getItem();
                    if(canProcessingSlotAcceptInput(inputItem) && canAcceptRecipeOutput(match))
                    {
                        processingBatch.enqueue(new ProcessableRecipeObject<>(inputItem, match));
                        items.get(INPUT_SLOT).decrement(1);
                        addProcessingElement(inputItem);
                    }
                }
            }
            
            if(!processingBatch.isEmpty())
            {
                int size = processingBatch.size();
                for(int i = 0; i < size; i++)
                {
                    processingBatch.at(i).processStep();
                }
                while(!processingBatch.isEmpty() && processingBatch.head().isCompleted())
                {
                    craftRecipe(processingBatch.poll().getRecipe());
                    currentOilUsage++;
                }
                if(!isOn())
                {
                    setOn(true);
                }
            }
            else if(isOn())
            {
                setOn(false);
            }
        }
    }
    
    private void addProcessingElement(Item item)
    {
        ItemStack processingItemStack = items.get(PROCESSING_SLOT);
        if(processingItemStack.isEmpty())
        {
            items.set(PROCESSING_SLOT, new ItemStack(item));
        }
        else if(item.equals(processingItemStack.getItem()))
        {
            processingItemStack.increment(1);
        }
    }
    
    private boolean canProcessingSlotAcceptInput(Item item)
    {
        if(item.equals(null))
        {
            return true;
        }
        ProcessableRecipeObject peek = processingBatch.head();
        if(peek == null)
        {
            return true;
        }
        if(processingBatch.canAdd() && peek.getInputType().equals(item))
        {
            return true;
        }
        return false;
    }
    
    public int getComparatorOutput()
    {
        return getOilType() == OilType.NONE ? 6 : (int) (((float) currentOilUsage / MAX_OIL_USAGE) * 5);
    }
    
    public void fillOil(OilType oilType)
    {
        if(!world.isClient)
        {
            setOilType(oilType);
            currentOilUsage = 0;
            world.updateComparators(pos, world.getBlockState(pos).getBlock());
        }
    }
    
    public void drainOil()
    {
        if(!world.isClient)
        {
            setOilType(OilType.NONE);
            currentOilUsage = MAX_OIL_USAGE;
            world.updateComparators(pos, world.getBlockState(pos).getBlock());
        }
    }
    
    public boolean isOilNew()
    {
        return currentOilUsage == 0 && getOilType() != OilType.NONE;
    }
    
    public boolean isOilValid()
    {
        return currentOilUsage < MAX_OIL_USAGE && getOilType() != OilType.NONE;
    }
    
    private void setOilType(OilType oilType)
    {
        if(!world.isClient)
        {
            this.world.setBlockState(this.pos, (BlockState) this.world.getBlockState(this.pos).with(FryerBlock.OIL_TYPE, oilType), 0B1011);
            world.updateComparators(pos, world.getBlockState(pos).getBlock());
        }
    }
    
    public OilType getOilType()
    {
        return this.world.getBlockState(pos).get(FryerBlock.OIL_TYPE);
    }
    
    private void setOn(boolean state)
    {
        if(!world.isClient)
        {
            this.world.setBlockState(this.pos, (BlockState) this.world.getBlockState(this.pos).with(FryerBlock.ON, state), 0B1011);
        }
    }
    
    public boolean isOn()
    {
        return world.getBlockState(pos).get(FryerBlock.ON);
    }
    
    @Override public void fromTag(BlockState state, CompoundTag tag)
    {
        super.fromTag(state, tag);
        currentOilUsage = tag.getInt("oilUsage");
        inverseOilUsage = MAX_OIL_USAGE - currentOilUsage;
    }
    
    @Override public CompoundTag toTag(CompoundTag tag)
    {
        ItemStack processingStack = items.get(PROCESSING_SLOT);
        ItemStack inputStack = items.get(INPUT_SLOT);
        if(processingStack.getCount() + inputStack.getCount() < getMaxCountPerStack())
        {
            inputStack.increment(processingStack.getCount());
        }
        processingStack.setCount(0);
        processingBatch.clear();
        tag.putInt("oilUsage", currentOilUsage);
        
        return super.toTag(tag);
    }
    
    @Override public PropertyDelegate getPropertyDelegate()
    {
        return propertyDelegate;
    }
    
    @Override public Text getDisplayName()
    {
        return new TranslatableText(getCachedState().getBlock().getTranslationKey());
    }
    
    @Override public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player)
    {
        return new FryerBlockGUIHandler(syncId, inv, ScreenHandlerContext.create(world, pos));
    }
    
    @Override public void writeScreenOpeningData(ServerPlayerEntity serverPlayerEntity, PacketByteBuf packetByteBuf)
    {
        packetByteBuf.writeBlockPos(pos);
    }
}
