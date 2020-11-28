package net.la.lega.mod.initializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.la.lega.mod.feature.RandomAgeCropFeature;
import net.la.lega.mod.loader.LLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.decorator.CountNoiseBiasedDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.FeatureConfig;

public final class LFeatures
{
    private static RandomAgeCropFeature RICE_FEATURE;
    private static RandomAgeCropFeature AVOCADO_FEATURE;
    private static RandomAgeCropFeature WASABI_FEATURE;
    
    public static void initialize()
    {
        
        RICE_FEATURE = Registry.register(Registry.FEATURE, new Identifier(LLoader.MOD_ID, "rice_feature"), new RandomAgeCropFeature(ProbabilityConfig.CODEC, LBlocks.RICE_BLOCK));
        ConfiguredFeature<?, ?> riceFeatureConf = RICE_FEATURE.configure(new ProbabilityConfig(0.15F)).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP_WORLD_SURFACE).spreadHorizontally().decorate(Decorator.COUNT_NOISE_BIASED.configure(new CountNoiseBiasedDecoratorConfig(160, 80.0D, 0.3D)));
        RegistryKey<ConfiguredFeature<?, ?>> configuredRiceFeature = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(LLoader.MOD_ID, "rice_feature"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, configuredRiceFeature.getValue(), riceFeatureConf);
        
        AVOCADO_FEATURE = Registry.register(Registry.FEATURE, new Identifier(LLoader.MOD_ID, "avocado_feature"), new RandomAgeCropFeature(ProbabilityConfig.CODEC, LBlocks.AVOCADO_BLOCK));
        ConfiguredFeature<?, ?> avocadoFeatureConf = AVOCADO_FEATURE.configure(new ProbabilityConfig(0.08F)).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP_WORLD_SURFACE).spreadHorizontally().decorate(Decorator.COUNT_NOISE_BIASED.configure(new CountNoiseBiasedDecoratorConfig(20, 80.0D, 0.3D)));
        RegistryKey<ConfiguredFeature<?, ?>> configuredAvocadoFeature = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(LLoader.MOD_ID, "avocado_feature"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, configuredAvocadoFeature.getValue(), avocadoFeatureConf);
        
        WASABI_FEATURE = Registry.register(Registry.FEATURE, new Identifier(LLoader.MOD_ID, "wasabi_feature"), new RandomAgeCropFeature(ProbabilityConfig.CODEC, LBlocks.WASABI_BLOCK));
        ConfiguredFeature<?, ?> wasabiFeatureConf = WASABI_FEATURE.configure(new ProbabilityConfig(0.035F)).decorate(ConfiguredFeatures.Decorators.HEIGHTMAP_WORLD_SURFACE).spreadHorizontally().decorate(Decorator.COUNT_NOISE_BIASED.configure(new CountNoiseBiasedDecoratorConfig(160, 80.0D, 0.3D)));
        RegistryKey<ConfiguredFeature<?, ?>> configuredWasabiFeature = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier(LLoader.MOD_ID, "wasabi_feature"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_FEATURE, configuredWasabiFeature.getValue(), wasabiFeatureConf);
        
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE), GenerationStep.Feature.VEGETAL_DECORATION, configuredRiceFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE_EDGE), GenerationStep.Feature.VEGETAL_DECORATION, configuredRiceFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE_HILLS), GenerationStep.Feature.VEGETAL_DECORATION, configuredRiceFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BAMBOO_JUNGLE), GenerationStep.Feature.VEGETAL_DECORATION, configuredRiceFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BAMBOO_JUNGLE_HILLS), GenerationStep.Feature.VEGETAL_DECORATION, configuredRiceFeature);
        
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE), GenerationStep.Feature.VEGETAL_DECORATION, configuredAvocadoFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE_EDGE), GenerationStep.Feature.VEGETAL_DECORATION, configuredAvocadoFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE_HILLS), GenerationStep.Feature.VEGETAL_DECORATION, configuredAvocadoFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BAMBOO_JUNGLE), GenerationStep.Feature.VEGETAL_DECORATION, configuredAvocadoFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BAMBOO_JUNGLE_HILLS), GenerationStep.Feature.VEGETAL_DECORATION, configuredAvocadoFeature);
        
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE), GenerationStep.Feature.VEGETAL_DECORATION, configuredWasabiFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE_EDGE), GenerationStep.Feature.VEGETAL_DECORATION, configuredWasabiFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE_HILLS), GenerationStep.Feature.VEGETAL_DECORATION, configuredWasabiFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BAMBOO_JUNGLE), GenerationStep.Feature.VEGETAL_DECORATION, configuredWasabiFeature);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.BAMBOO_JUNGLE_HILLS), GenerationStep.Feature.VEGETAL_DECORATION, configuredWasabiFeature);
    }
    
    private static <FC extends FeatureConfig> ConfiguredFeature<FC, ?> register(String id, ConfiguredFeature<FC, ?> configuredFeature)
    {
        return (ConfiguredFeature) Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, configuredFeature);
    }
}