package net.la.lega.mod.dispense_behaviour;

import net.la.lega.mod.block.AvocadoBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ShearsDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Random;

public class AvocadoDispenserBehavior extends ShearsDispenserBehavior
{
    @Override
    protected ItemStack dispenseSilently(BlockPointer blockPointer, ItemStack stack)
    {
        World world = blockPointer.getWorld();
        BlockPos blockPos = blockPointer.getBlockPos().offset((Direction) blockPointer.getBlockState().get(DispenserBlock.FACING));
        BlockState blockState = world.getBlockState(blockPos);
        
        if(blockState.getBlock() instanceof AvocadoBlock)
        {
            AvocadoBlock avocadoBlock = (AvocadoBlock) blockState.getBlock();
            int avocadoAge = (Integer) blockState.get(AvocadoBlock.AGE);
            if(avocadoAge > 3)
            {
                if(stack.damage(1, (Random) world.random, (ServerPlayerEntity) null))
                {
                    stack.setCount(0);
                }
                
                avocadoBlock.dropAvocadoes(blockState, world, blockPos);
                avocadoBlock.setAgeState(world, blockPos, 3);
            }
            return stack;
        }
        else
        {
            return super.dispenseSilently(blockPointer, stack);
        }
    }
}