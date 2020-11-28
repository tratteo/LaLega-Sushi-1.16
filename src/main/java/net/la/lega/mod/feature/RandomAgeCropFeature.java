package net.la.lega.mod.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.CropBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class RandomAgeCropFeature extends Feature<ProbabilityConfig>
{
    private CropBlock cropBlock;
    
    
    public RandomAgeCropFeature(Codec<ProbabilityConfig> configDeserializer, CropBlock cropBlock)
    {
        super(configDeserializer);
        this.cropBlock = cropBlock;
    }
    
    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos pos, ProbabilityConfig config)
    {
        if(cropBlock.canPlaceAt(null, world, pos))
        {
            int age = random.nextInt(cropBlock.getMaxAge());
            world.setBlockState(pos, cropBlock.getDefaultState().with(CropBlock.AGE, age), 4);
            return true;
        }
        return false;
    }
}
