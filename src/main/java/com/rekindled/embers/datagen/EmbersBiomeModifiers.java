package com.rekindled.embers.datagen;

import com.rekindled.embers.Embers;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

public class EmbersBiomeModifiers {

	public static final ResourceKey<BiomeModifier> ORE_LEAD_KEY = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Embers.MODID, "add_lead_ore"));

	public static final ResourceKey<BiomeModifier> ORE_SILVER_KEY = ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(Embers.MODID, "add_silver_ore"));

	public static void generate(BootstapContext<BiomeModifier> bootstrap) {
		HolderGetter<PlacedFeature> placed = bootstrap.lookup(Registries.PLACED_FEATURE);
		HolderGetter<Biome> biome = bootstrap.lookup(ForgeRegistries.Keys.BIOMES);
		HolderSet.Named<Biome> overworldBiomes = biome.getOrThrow(BiomeTags.IS_OVERWORLD);

		bootstrap.register(ORE_LEAD_KEY, new AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placed.getOrThrow(EmbersPlacedFeatures.ORE_LEAD_KEY)), Decoration.UNDERGROUND_ORES));
		bootstrap.register(ORE_SILVER_KEY, new AddFeaturesBiomeModifier(overworldBiomes, HolderSet.direct(placed.getOrThrow(EmbersPlacedFeatures.ORE_SILVER_KEY)), Decoration.UNDERGROUND_ORES));
	}
}
