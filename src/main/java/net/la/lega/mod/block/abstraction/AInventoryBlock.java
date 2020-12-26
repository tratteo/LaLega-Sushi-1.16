package net.la.lega.mod.block.abstraction;

import net.la.lega.mod.entity.abstraction.AInventoryEntity;
import net.la.lega.mod.entity.abstraction.ASidedInventoryEntity;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public abstract class AInventoryBlock extends BlockWithEntity
{
    public AInventoryBlock(Settings settings)
    {
        super(settings);
    }
    
    public abstract BlockEntity createBlockEntity(BlockView view);
    
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
    {
        if(!state.isOf(newState.getBlock()))
        {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if(blockEntity instanceof ASidedInventoryEntity || blockEntity instanceof AInventoryEntity)
            {
                ItemScatterer.spawn(world, pos, (Inventory) (blockEntity));
                world.updateComparators(pos, this);
            }
            
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }
    
    public boolean hasComparatorOutput(BlockState state)
    {
        return true;
    }
    
    public int getComparatorOutput(BlockState state, World world, BlockPos pos)
    {
        return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
    }
    
    public BlockRenderType getRenderType(BlockState state)
    {
        return BlockRenderType.MODEL;
    }
}