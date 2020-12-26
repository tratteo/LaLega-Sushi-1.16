package net.la.lega.mod.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.la.lega.mod.initializer.LItems;
import net.la.lega.mod.loader.LLoader;
import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Iterator;
import java.util.Random;

public class RiceBlock extends CropBlock
{
    public static final Identifier ID = new Identifier(LLoader.MOD_ID, "rice_block");
    
    public RiceBlock()
    {
        super(FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP).nonOpaque().build());
        this.setDefaultState((BlockState) ((BlockState) this.stateManager.getDefaultState()).with(this.getAgeProperty(), 0));
    }
    
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
          Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
          Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
          Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
          Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
          Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
          Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
          Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
          Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};
    
    @Environment(EnvType.CLIENT)
    protected ItemConvertible getSeedsItem()
    {
        return LItems.RICE_SEEDS;
    }
    
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ePos)
    {
        return AGE_TO_SHAPE[(Integer) state.get(this.getAgeProperty())];
    }
    
    @Override protected int getGrowthAmount(World world)
    {
        return 1;
    }
    public void setAgeState(World world, BlockPos pos, int age)
    {
        if(age < 0) return;
        if(!world.isClient)
        {
            world.setBlockState(pos, this.withAge(age > getMaxAge() ? getMaxAge() : age), 2);
        }
    }
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
    {
        if (world.getBaseLightLevel(pos, 0) >= 9)
        {
            int i = this.getAge(state);
            if (i < this.getMaxAge())
            {
                if (random.nextInt((int)(14.0F)) == 0)
                {
                    setAgeState(world, pos, i + getGrowthAmount(world));
                }
            }
        }
    }
    
    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
    {
        BlockState blockState = world.getBlockState(pos.down());
        if (blockState.isOf(Blocks.GRASS_BLOCK) || blockState.isOf(Blocks.DIRT) || blockState.isOf(Blocks.FARMLAND) || blockState.isOf(Blocks.PODZOL))
        {
            BlockPos blockPos = pos.down();
            Iterator var6 = Direction.Type.HORIZONTAL.iterator();
        
            while(var6.hasNext())
            {
                Direction direction = (Direction)var6.next();
                BlockState blockState2 = world.getBlockState(blockPos.offset(direction));
                FluidState fluidState = world.getFluidState(blockPos.offset(direction));
                if (fluidState.isIn(FluidTags.WATER) || blockState2.isOf(Blocks.FROSTED_ICE))
                {
                    return true;
                }
            }
        }
        return false;
    }
}