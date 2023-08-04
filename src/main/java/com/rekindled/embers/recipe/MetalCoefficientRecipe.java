package com.rekindled.embers.recipe;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.rekindled.embers.RegistryManager;

import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class MetalCoefficientRecipe implements Recipe<BlockStateContext> {

	public static final Serializer SERIALIZER = new Serializer(); 

	public final ResourceLocation id;

	public final TagKey<Block> blockTag;
	public final double coefficient;

	public MetalCoefficientRecipe(ResourceLocation id, TagKey<Block> blockTag, double coefficient) {
		this.id = id;
		this.blockTag = blockTag;
		this.coefficient = coefficient;
	}

	@Override
	public boolean matches(BlockStateContext context, Level pLevel) {
		return context.state.is(blockTag);
	}

	public double getCoefficient(BlockStateContext context) {
		return coefficient;
	}

	@Override
	public ItemStack getToastSymbol() {
		return new ItemStack(RegistryManager.PRESSURE_REFINERY_ITEM.get());
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return SERIALIZER;
	}

	@Override
	public RecipeType<?> getType() {
		return RegistryManager.METAL_COEFFICIENT.get();
	}

	public List<ItemStack> getDisplayInput() {
		List<ItemStack> list = Lists.newArrayList();
		for (Holder<Block> holder : BuiltInRegistries.BLOCK.getTagOrEmpty(blockTag)) {
			list.add(new ItemStack(holder.get()));
		}
		return list;
	}

	public double getDisplayCoefficient() {
		return coefficient;
	}

	@Override
	@Deprecated
	public ItemStack getResultItem(RegistryAccess registry) {
		return ItemStack.EMPTY;
	}

	@Override
	@Deprecated
	public ItemStack assemble(BlockStateContext context, RegistryAccess registry) {
		return ItemStack.EMPTY;
	}

	@Override
	@Deprecated
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	public static class Serializer implements RecipeSerializer<MetalCoefficientRecipe> {

		@Override
		public MetalCoefficientRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
			ResourceLocation blockTag = new ResourceLocation(json.get("block_tag").getAsString());
			double coefficient = GsonHelper.getAsDouble(json, "coefficient");

			return new MetalCoefficientRecipe(recipeId, BlockTags.create(blockTag), coefficient);
		}

		@Override
		public @Nullable MetalCoefficientRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
			ResourceLocation blockTag = buffer.readResourceLocation();
			double coefficient = buffer.readDouble();

			return new MetalCoefficientRecipe(recipeId, BlockTags.create(blockTag), coefficient);
		}

		@Override
		public void toNetwork(FriendlyByteBuf buffer, MetalCoefficientRecipe recipe) {
			buffer.writeResourceLocation(recipe.blockTag.location());
			buffer.writeDouble(recipe.coefficient);
		}
	}
}
