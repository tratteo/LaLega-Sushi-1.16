package net.la.lega.mod.dispense_behaviour;

import net.la.lega.mod.block.SteamCookerBlock;
import net.la.lega.mod.entity.SteamCookerBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class SteamCookerDispenserBehavior extends ItemDispenserBehavior
{
    @Override
    protected ItemStack dispenseSilently(BlockPointer blockPointer, ItemStack stack)
    {
        World world = blockPointer.getWorld();
        BlockPos blockPos = blockPointer.getBlockPos().offset((Direction) blockPointer.getBlockState().get(DispenserBlock.FACING));
        BlockState blockState = world.getBlockState(blockPos);
        
        if(blockState.getBlock() instanceof SteamCookerBlock && !((SteamCookerBlockEntity) world.getBlockEntity(blockPos)).isWaterNew())
        {
            SteamCookerBlockEntity steamCookerEntity = (SteamCookerBlockEntity) world.getBlockEntity(blockPos);
            if(stack.getItem().equals(Items.WATER_BUCKET))
            {
                steamCookerEntity.fillWater();
            }
            
            ItemStack bucket = new ItemStack(Items.BUCKET);
            stack.decrement(1);
            this.dispense(blockPointer, bucket.copy());
            return stack;
        }
        else
        {
            BucketItem bucketItem = (BucketItem) stack.getItem();
            blockPos = blockPointer.getBlockPos().offset((Direction) blockPointer.getBlockState().get(DispenserBlock.FACING));
            world = blockPointer.getWorld();
            if(bucketItem.placeFluid((PlayerEntity) null, world, blockPos, (BlockHitResult) null))
            {
                bucketItem.onEmptied(world, stack, blockPos);
                return new ItemStack(Items.BUCKET);
            }
            else
            {
                return this.dispense(blockPointer, stack);
            }
        }
    }
}
