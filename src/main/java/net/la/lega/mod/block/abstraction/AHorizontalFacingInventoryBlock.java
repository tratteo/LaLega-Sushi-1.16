package net.la.lega.mod.block.abstraction;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public abstract class AHorizontalFacingInventoryBlock extends AInventoryBlock
{
    public static final DirectionProperty FACING;
    
    static
    {
        FACING = HorizontalFacingBlock.FACING;
    }
    
    public AHorizontalFacingInventoryBlock(Settings settings)
    {
        super(settings);
        this.setDefaultState((BlockState) ((BlockState) ((BlockState) this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)));
    }
    
    @Override public abstract BlockEntity createBlockEntity(BlockView view);
    
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager)
    {
        stateManager.add(FACING);
    }
    
    public BlockState getPlacementState(ItemPlacementContext ctx)
    {
        return (BlockState) this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }
    
    public BlockState rotate(BlockState state, BlockRotation rotation)
    {
        return (BlockState) state.with(FACING, rotation.rotate((Direction) state.get(FACING)));
    }
    
    public BlockState mirror(BlockState state, BlockMirror mirror)
    {
        return state.rotate(mirror.getRotation((Direction) state.get(FACING)));
    }
}
