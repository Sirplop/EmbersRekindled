package com.rekindled.embers.datagen;

import java.util.function.Function;

import com.rekindled.embers.Embers;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.RegistryManager.FluidStuff;
import com.rekindled.embers.RegistryManager.StoneDecoBlocks;
import com.rekindled.embers.block.EmberBoreBlock;
import com.rekindled.embers.block.MechEdgeBlockBase;
import com.rekindled.embers.block.MechEdgeBlockBase.MechEdge;
import com.rekindled.embers.block.PipeBlockBase;
import com.rekindled.embers.block.PipeBlockBase.PipeConnection;
import com.rekindled.embers.block.StamperBlock;

import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EmbersBlockStates extends BlockStateProvider {

	public EmbersBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
		super(gen, Embers.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		//this is just to give them proper particles
		for (FluidStuff fluid : RegistryManager.fluidList) {
			fluid(fluid.FLUID_BLOCK, fluid.name);
		}

		blockWithItemTexture(RegistryManager.LEAD_ORE, "ore_lead");
		blockWithItemTexture(RegistryManager.DEEPSLATE_LEAD_ORE, "deepslate_ore_lead");
		blockWithItemTexture(RegistryManager.RAW_LEAD_BLOCK, "material_lead");
		blockWithItemTexture(RegistryManager.LEAD_BLOCK, "block_lead");

		blockWithItemTexture(RegistryManager.SILVER_ORE, "ore_silver");
		blockWithItemTexture(RegistryManager.DEEPSLATE_SILVER_ORE, "deepslate_ore_silver");
		blockWithItemTexture(RegistryManager.RAW_SILVER_BLOCK, "material_silver");
		blockWithItemTexture(RegistryManager.SILVER_BLOCK, "block_silver");

		blockWithItemTexture(RegistryManager.DAWNSTONE_BLOCK, "block_dawnstone");

		blockWithItem(RegistryManager.CAMINITE_BRICKS);
		decoBlocks(RegistryManager.CAMINITE_BRICKS_DECO);
		blockWithItem(RegistryManager.ARCHAIC_BRICKS);
		decoBlocks(RegistryManager.ARCHAIC_BRICKS_DECO);
		blockWithItem(RegistryManager.ARCHAIC_EDGE, "archaic_edge");
		blockWithItem(RegistryManager.ARCHAIC_TILE);
		decoBlocks(RegistryManager.ARCHAIC_TILE_DECO);
		blockWithItem(RegistryManager.ARCHAIC_LIGHT, "archaic_light");
		blockWithItem(RegistryManager.EMBER_LANTERN, "ember_lantern");

		blockWithItem(RegistryManager.COPPER_CELL, "copper_cell");
		blockWithItem(RegistryManager.CREATIVE_EMBER);
		dial(RegistryManager.EMBER_DIAL, "ember_dial");
		dial(RegistryManager.ITEM_DIAL, "item_dial");
		dial(RegistryManager.FLUID_DIAL, "fluid_dial");
		dial(RegistryManager.CLOCKWORK_ATTENUATOR, "clockwork_attenuator");

		ModelFile fluidPipeCenterModel = models().withExistingParent("fluid_pipe_center", new ResourceLocation(Embers.MODID, "pipe_center"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"));
		ModelFile fluidPipeEndModel = models().withExistingParent("fluid_pipe_end", new ResourceLocation(Embers.MODID, "pipe_end"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"));
		ModelFile fluidPipeConnectionModel = models().withExistingParent("fluid_pipe_connection", new ResourceLocation(Embers.MODID, "pipe_connection"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"));
		ModelFile fluidPipeEndModel2 = models().withExistingParent("fluid_pipe_end_2", new ResourceLocation(Embers.MODID, "pipe_end_2"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"));
		ModelFile fluidPipeConnectionModel2 = models().withExistingParent("fluid_pipe_connection_2", new ResourceLocation(Embers.MODID, "pipe_connection_2"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"));

		ExistingModelFile emitterModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "ember_emitter"));
		simpleBlockItem(RegistryManager.EMBER_EMITTER.get(), emitterModel);

		getMultipartBuilder(RegistryManager.EMBER_EMITTER.get())
		.part().modelFile(emitterModel).addModel()
		.condition(BlockStateProperties.FACING, Direction.UP).end()
		.part().modelFile(emitterModel).rotationX(180).addModel()
		.condition(BlockStateProperties.FACING, Direction.DOWN).end()
		.part().modelFile(emitterModel).rotationX(90).addModel()
		.condition(BlockStateProperties.FACING, Direction.NORTH).end()
		.part().modelFile(emitterModel).rotationX(90).rotationY(180).addModel()
		.condition(BlockStateProperties.FACING, Direction.SOUTH).end()
		.part().modelFile(emitterModel).rotationX(90).rotationY(90).addModel()
		.condition(BlockStateProperties.FACING, Direction.EAST).end()
		.part().modelFile(emitterModel).rotationX(90).rotationY(270).addModel()
		.condition(BlockStateProperties.FACING, Direction.WEST).end()
		.part().modelFile(fluidPipeEndModel).addModel()
		.condition(BlockStateProperties.DOWN, true).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(180).addModel()
		.condition(BlockStateProperties.UP, true).end()
		.part().modelFile(fluidPipeEndModel).rotationX(90).addModel()
		.condition(BlockStateProperties.SOUTH, true).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(90).rotationY(180).addModel()
		.condition(BlockStateProperties.NORTH, true).end()
		.part().modelFile(fluidPipeEndModel).rotationX(90).rotationY(90).addModel()
		.condition(BlockStateProperties.WEST, true).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(90).rotationY(270).addModel()
		.condition(BlockStateProperties.EAST, true).end();

		ExistingModelFile receiverModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "ember_receiver"));
		directionalBlock(RegistryManager.EMBER_RECEIVER.get(), receiverModel);
		simpleBlockItem(RegistryManager.EMBER_RECEIVER.get(), receiverModel);

		ExistingModelFile leverModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "caminite_lever"));
		leverBlock(RegistryManager.CAMINITE_LEVER.get(), leverModel, models().getExistingFile(new ResourceLocation(Embers.MODID, "caminite_lever_on")));
		simpleBlockItem(RegistryManager.CAMINITE_LEVER.get(), leverModel);

		ModelFile itemPipeCenterModel = models().withExistingParent("item_pipe_center", new ResourceLocation(Embers.MODID, "pipe_center"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"));
		ModelFile itemPipeEndModel = models().withExistingParent("item_pipe_end", new ResourceLocation(Embers.MODID, "pipe_end"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"));
		ModelFile itemPipeConnectionModel = models().withExistingParent("item_pipe_connection", new ResourceLocation(Embers.MODID, "pipe_connection"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"));
		ModelFile itemPipeEndModel2 = models().withExistingParent("item_pipe_end_2", new ResourceLocation(Embers.MODID, "pipe_end_2"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"));
		ModelFile itemPipeConnectionModel2 = models().withExistingParent("item_pipe_connection_2", new ResourceLocation(Embers.MODID, "pipe_connection_2"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"));

		simpleBlockItem(RegistryManager.ITEM_PIPE.get(), models().withExistingParent("item_pipe_inventory", new ResourceLocation(Embers.MODID, "pipe_inventory"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/item_pipe_tex")));

		getMultipartBuilder(RegistryManager.ITEM_PIPE.get())
		.part().modelFile(itemPipeCenterModel).addModel().end()
		//pipe ends
		.part().modelFile(itemPipeEndModel).addModel()
		.condition(PipeBlockBase.DOWN, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel2).rotationX(180).addModel()
		.condition(PipeBlockBase.UP, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel).rotationX(90).addModel()
		.condition(PipeBlockBase.SOUTH, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel2).rotationX(90).rotationY(180).addModel()
		.condition(PipeBlockBase.NORTH, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel).rotationX(90).rotationY(90).addModel()
		.condition(PipeBlockBase.WEST, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel2).rotationX(90).rotationY(270).addModel()
		.condition(PipeBlockBase.EAST, PipeConnection.END).end()
		//pipe connections
		.part().modelFile(itemPipeConnectionModel).addModel()
		.condition(PipeBlockBase.DOWN, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel2).rotationX(180).addModel()
		.condition(PipeBlockBase.UP, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel).rotationX(90).addModel()
		.condition(PipeBlockBase.SOUTH, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel2).rotationX(90).rotationY(180).addModel()
		.condition(PipeBlockBase.NORTH, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel).rotationX(90).rotationY(90).addModel()
		.condition(PipeBlockBase.WEST, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel2).rotationX(90).rotationY(270).addModel()
		.condition(PipeBlockBase.EAST, PipeConnection.PIPE).end();

		ModelFile itemExtractorCenterModel = models().withExistingParent("item_extractor_center", new ResourceLocation(Embers.MODID, "extractor_center"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/item_pipe_tex"));

		simpleBlockItem(RegistryManager.ITEM_EXTRACTOR.get(), itemExtractorCenterModel);
		getMultipartBuilder(RegistryManager.ITEM_EXTRACTOR.get())
		.part().modelFile(itemExtractorCenterModel).addModel().end()
		//pipe ends
		.part().modelFile(itemPipeEndModel).addModel()
		.condition(PipeBlockBase.DOWN, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel2).rotationX(180).addModel()
		.condition(PipeBlockBase.UP, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel).rotationX(90).addModel()
		.condition(PipeBlockBase.SOUTH, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel2).rotationX(90).rotationY(180).addModel()
		.condition(PipeBlockBase.NORTH, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel).rotationX(90).rotationY(90).addModel()
		.condition(PipeBlockBase.WEST, PipeConnection.END).end()
		.part().modelFile(itemPipeEndModel2).rotationX(90).rotationY(270).addModel()
		.condition(PipeBlockBase.EAST, PipeConnection.END).end()
		//pipe connections
		.part().modelFile(itemPipeConnectionModel).addModel()
		.condition(PipeBlockBase.DOWN, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel2).rotationX(180).addModel()
		.condition(PipeBlockBase.UP, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel).rotationX(90).addModel()
		.condition(PipeBlockBase.SOUTH, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel2).rotationX(90).rotationY(180).addModel()
		.condition(PipeBlockBase.NORTH, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel).rotationX(90).rotationY(90).addModel()
		.condition(PipeBlockBase.WEST, PipeConnection.PIPE).end()
		.part().modelFile(itemPipeConnectionModel2).rotationX(90).rotationY(270).addModel()
		.condition(PipeBlockBase.EAST, PipeConnection.PIPE).end();

		ExistingModelFile emberBoreModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "ember_bore_center"));
		ExistingModelFile emberBoreBladesModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "ember_bore_blades"));
		getVariantBuilder(RegistryManager.EMBER_BORE.get()).forAllStates(state -> {
			Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);

			return ConfiguredModel.builder()
					.modelFile(state.getValue(EmberBoreBlock.BLADES) ? emberBoreBladesModel : emberBoreModel)
					.rotationY(axis == Axis.Z ? 0 : 90)
					.uvLock(false)
					.build();
		});
		simpleBlockItem(RegistryManager.EMBER_BORE.get(), models().cubeAll("ember_bore", new ResourceLocation(Embers.MODID, "block/crate_bore")));

		ExistingModelFile mechEdgeModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "mech_edge_straight"));
		ExistingModelFile boreEdgeModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "ember_bore_edge"));
		ExistingModelFile mechCornerModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "mech_edge_corner"));
		getVariantBuilder(RegistryManager.EMBER_BORE_EDGE.get()).forAllStates(state -> {
			MechEdge edge = state.getValue(MechEdgeBlockBase.EDGE);
			Axis axis = state.getValue(BlockStateProperties.HORIZONTAL_AXIS);

			return ConfiguredModel.builder()
					.modelFile(edge.corner ? mechCornerModel : (edge == MechEdge.NORTH || edge == MechEdge.SOUTH) && axis == Axis.Z || (edge == MechEdge.EAST || edge == MechEdge.WEST) && axis == Axis.X ? boreEdgeModel : mechEdgeModel)
					.rotationY(edge.rotation)
					.uvLock(true)
					.build();
		});

		ExistingModelFile mechCoreModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "mech_core"));
		betterDirectionalBlock(RegistryManager.MECHANICAL_CORE.get(), $ -> mechCoreModel, 0);
		simpleBlockItem(RegistryManager.MECHANICAL_CORE.get(), mechCoreModel);

		ExistingModelFile activatorTopModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "activator_top"));
		simpleBlockItem(RegistryManager.EMBER_ACTIVATOR.get(), activatorTopModel);
		getVariantBuilder(RegistryManager.EMBER_ACTIVATOR.get()).forAllStates(state -> {
			return ConfiguredModel.builder()
					.modelFile(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? models().getExistingFile(new ResourceLocation(Embers.MODID, "activator_bottom")) : activatorTopModel)
					.uvLock(false)
					.build();
		});

		ExistingModelFile melterBottomModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "melter_bottom"));
		simpleBlockItem(RegistryManager.MELTER.get(), melterBottomModel);
		getVariantBuilder(RegistryManager.MELTER.get()).forAllStates(state -> {
			return ConfiguredModel.builder()
					.modelFile(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? melterBottomModel : models().getExistingFile(new ResourceLocation(Embers.MODID, "melter_top")))
					.uvLock(false)
					.build();
		});

		simpleBlockItem(RegistryManager.FLUID_PIPE.get(), models().withExistingParent("fluid_pipe_inventory", new ResourceLocation(Embers.MODID, "pipe_inventory"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex")));

		getMultipartBuilder(RegistryManager.FLUID_PIPE.get())
		.part().modelFile(fluidPipeCenterModel).addModel().end()
		//pipe ends
		.part().modelFile(fluidPipeEndModel).addModel()
		.condition(PipeBlockBase.DOWN, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(180).addModel()
		.condition(PipeBlockBase.UP, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel).rotationX(90).addModel()
		.condition(PipeBlockBase.SOUTH, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(90).rotationY(180).addModel()
		.condition(PipeBlockBase.NORTH, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel).rotationX(90).rotationY(90).addModel()
		.condition(PipeBlockBase.WEST, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(90).rotationY(270).addModel()
		.condition(PipeBlockBase.EAST, PipeConnection.END).end()
		//pipe connections
		.part().modelFile(fluidPipeConnectionModel).addModel()
		.condition(PipeBlockBase.DOWN, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel2).rotationX(180).addModel()
		.condition(PipeBlockBase.UP, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel).rotationX(90).addModel()
		.condition(PipeBlockBase.SOUTH, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel2).rotationX(90).rotationY(180).addModel()
		.condition(PipeBlockBase.NORTH, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel).rotationX(90).rotationY(90).addModel()
		.condition(PipeBlockBase.WEST, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel2).rotationX(90).rotationY(270).addModel()
		.condition(PipeBlockBase.EAST, PipeConnection.PIPE).end();

		ModelFile fluidExtractorCenterModel = models().withExistingParent("fluid_extractor_center", new ResourceLocation(Embers.MODID, "extractor_center"))
				.texture("pipe", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/fluid_pipe_tex"));

		simpleBlockItem(RegistryManager.FLUID_EXTRACTOR.get(), fluidExtractorCenterModel);
		getMultipartBuilder(RegistryManager.FLUID_EXTRACTOR.get())
		.part().modelFile(fluidExtractorCenterModel).addModel().end()
		//pipe ends
		.part().modelFile(fluidPipeEndModel).addModel()
		.condition(PipeBlockBase.DOWN, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(180).addModel()
		.condition(PipeBlockBase.UP, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel).rotationX(90).addModel()
		.condition(PipeBlockBase.SOUTH, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(90).rotationY(180).addModel()
		.condition(PipeBlockBase.NORTH, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel).rotationX(90).rotationY(90).addModel()
		.condition(PipeBlockBase.WEST, PipeConnection.END).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(90).rotationY(270).addModel()
		.condition(PipeBlockBase.EAST, PipeConnection.END).end()
		//pipe connections
		.part().modelFile(fluidPipeConnectionModel).addModel()
		.condition(PipeBlockBase.DOWN, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel2).rotationX(180).addModel()
		.condition(PipeBlockBase.UP, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel).rotationX(90).addModel()
		.condition(PipeBlockBase.SOUTH, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel2).rotationX(90).rotationY(180).addModel()
		.condition(PipeBlockBase.NORTH, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel).rotationX(90).rotationY(90).addModel()
		.condition(PipeBlockBase.WEST, PipeConnection.PIPE).end()
		.part().modelFile(fluidPipeConnectionModel2).rotationX(90).rotationY(270).addModel()
		.condition(PipeBlockBase.EAST, PipeConnection.PIPE).end();

		blockWithItem(RegistryManager.FLUID_VESSEL, "fluid_vessel");

		ExistingModelFile stamperModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "stamper"));
		simpleBlockItem(RegistryManager.STAMPER.get(), stamperModel);
		getVariantBuilder(RegistryManager.STAMPER.get()).forAllStates(state -> {
			return ConfiguredModel.builder()
					.modelFile(state.getValue(StamperBlock.ARM) ? models().getExistingFile(new ResourceLocation(Embers.MODID, "stamper_arm")) : stamperModel)
					.uvLock(false)
					.build();
		});

		blockWithItem(RegistryManager.STAMP_BASE, "stamp_base");

		blockWithItem(RegistryManager.BIN, "bin");

		ExistingModelFile mixerBottomModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "mixer_bottom"));
		simpleBlockItem(RegistryManager.MIXER_CENTRIFUGE.get(), mixerBottomModel);
		getVariantBuilder(RegistryManager.MIXER_CENTRIFUGE.get()).forAllStates(state -> {
			return ConfiguredModel.builder()
					.modelFile(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? mixerBottomModel : models().getExistingFile(new ResourceLocation(Embers.MODID, "mixer_top")))
					.uvLock(false)
					.build();
		});

		ModelFile dropperModel = models().withExistingParent(RegistryManager.ITEM_DROPPER.getId().toString(), new ResourceLocation(Embers.MODID, "dropper"))
				.texture("dropper", new ResourceLocation(Embers.MODID, "block/plates_lead"))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/plates_lead"));
		simpleBlock(RegistryManager.ITEM_DROPPER.get(), dropperModel);
		simpleBlockItem(RegistryManager.ITEM_DROPPER.get(), dropperModel);

		ExistingModelFile refineryBottomModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "refinery_bottom"));
		simpleBlockItem(RegistryManager.PRESSURE_REFINERY.get(), refineryBottomModel);
		getVariantBuilder(RegistryManager.PRESSURE_REFINERY.get()).forAllStates(state -> {
			return ConfiguredModel.builder()
					.modelFile(state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? refineryBottomModel : models().getExistingFile(new ResourceLocation(Embers.MODID, "refinery_top")))
					.uvLock(false)
					.build();
		});

		ExistingModelFile ejectorModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "ember_ejector"));
		simpleBlockItem(RegistryManager.EMBER_EJECTOR.get(), ejectorModel);

		getMultipartBuilder(RegistryManager.EMBER_EJECTOR.get())
		.part().modelFile(ejectorModel).addModel()
		.condition(BlockStateProperties.FACING, Direction.UP).end()
		.part().modelFile(ejectorModel).rotationX(180).addModel()
		.condition(BlockStateProperties.FACING, Direction.DOWN).end()
		.part().modelFile(ejectorModel).rotationX(90).addModel()
		.condition(BlockStateProperties.FACING, Direction.NORTH).end()
		.part().modelFile(ejectorModel).rotationX(90).rotationY(180).addModel()
		.condition(BlockStateProperties.FACING, Direction.SOUTH).end()
		.part().modelFile(ejectorModel).rotationX(90).rotationY(90).addModel()
		.condition(BlockStateProperties.FACING, Direction.EAST).end()
		.part().modelFile(ejectorModel).rotationX(90).rotationY(270).addModel()
		.condition(BlockStateProperties.FACING, Direction.WEST).end()
		.part().modelFile(fluidPipeEndModel).addModel()
		.condition(BlockStateProperties.DOWN, true).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(180).addModel()
		.condition(BlockStateProperties.UP, true).end()
		.part().modelFile(fluidPipeEndModel).rotationX(90).addModel()
		.condition(BlockStateProperties.SOUTH, true).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(90).rotationY(180).addModel()
		.condition(BlockStateProperties.NORTH, true).end()
		.part().modelFile(fluidPipeEndModel).rotationX(90).rotationY(90).addModel()
		.condition(BlockStateProperties.WEST, true).end()
		.part().modelFile(fluidPipeEndModel2).rotationX(90).rotationY(270).addModel()
		.condition(BlockStateProperties.EAST, true).end();

		ExistingModelFile funnelModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "ember_funnel"));
		directionalBlock(RegistryManager.EMBER_FUNNEL.get(), funnelModel);
		simpleBlockItem(RegistryManager.EMBER_FUNNEL.get(), funnelModel);

		ExistingModelFile relayModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "ember_relay"));
		directionalBlock(RegistryManager.EMBER_RELAY.get(), relayModel);
		simpleBlockItem(RegistryManager.EMBER_RELAY.get(), relayModel);

		ExistingModelFile mirrorModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "mirror_relay"));
		directionalBlock(RegistryManager.MIRROR_RELAY.get(), mirrorModel);
		simpleBlockItem(RegistryManager.MIRROR_RELAY.get(), mirrorModel);

		ExistingModelFile splitterModelX = models().getExistingFile(new ResourceLocation(Embers.MODID, "beam_splitter_x"));
		ExistingModelFile splitterModelZ = models().getExistingFile(new ResourceLocation(Embers.MODID, "beam_splitter_z"));
		simpleBlockItem(RegistryManager.BEAM_SPLITTER.get(), splitterModelZ);
		getVariantBuilder(RegistryManager.BEAM_SPLITTER.get()).forAllStates(state -> {
			Direction face = state.getValue(BlockStateProperties.FACING);
			Axis axis = state.getValue(BlockStateProperties.AXIS);
			return ConfiguredModel.builder()
					.modelFile((axis == Axis.X && face.getAxis() == Axis.Y) || (axis != Axis.Y && face.getAxis() != Axis.Y) ? splitterModelX : splitterModelZ)
					.rotationX(face == Direction.DOWN ? 180 : face == Direction.UP ? 0 : 90)
					.rotationY(face == Direction.SOUTH ? 180 : face == Direction.WEST ? 270 : face == Direction.EAST ? 90 : 0)
					.uvLock(false)
					.build();
		});

		ExistingModelFile vacuumModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "item_vacuum"));
		directionalBlock(RegistryManager.ITEM_VACUUM.get(), vacuumModel);
		simpleBlockItem(RegistryManager.ITEM_VACUUM.get(), vacuumModel);

		simpleBlock(RegistryManager.HEARTH_COIL.get(), models().getExistingFile(new ResourceLocation(Embers.MODID, "hearth_coil_center")));
		simpleBlockItem(RegistryManager.HEARTH_COIL.get(), models().cubeAll("hearth_coil", new ResourceLocation(Embers.MODID, "block/crate_coil")));

		ExistingModelFile coilEdgeModelX = models().getExistingFile(new ResourceLocation(Embers.MODID, "hearth_coil_edge_x"));
		ExistingModelFile coilEdgeModelZ = models().getExistingFile(new ResourceLocation(Embers.MODID, "hearth_coil_edge_z"));
		ExistingModelFile coilCornerModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "hearth_coil_corner"));
		getVariantBuilder(RegistryManager.HEARTH_COIL_EDGE.get()).forAllStates(state -> {
			MechEdge edge = state.getValue(MechEdgeBlockBase.EDGE);

			return ConfiguredModel.builder()
					.modelFile(edge.corner ? coilCornerModel : edge == MechEdge.EAST || edge == MechEdge.WEST ? coilEdgeModelZ : coilEdgeModelX)
					.rotationY(edge.rotation)
					.uvLock(true)
					.build();
		});

		simpleBlock(RegistryManager.RESERVOIR.get(), models().getExistingFile(new ResourceLocation(Embers.MODID, "reservoir_center")));
		simpleBlockItem(RegistryManager.RESERVOIR.get(), models().cubeAll("reservoir", new ResourceLocation(Embers.MODID, "block/crate_tank")));

		getVariantBuilder(RegistryManager.RESERVOIR_EDGE.get()).forAllStates(state -> {
			MechEdge edge = state.getValue(MechEdgeBlockBase.EDGE);

			return ConfiguredModel.builder()
					.modelFile(edge.corner ? mechCornerModel : mechEdgeModel)
					.rotationY(edge.rotation)
					.uvLock(true)
					.build();
		});

		simpleBlock(RegistryManager.CAMINITE_RING.get(), models().getExistingFile(new ResourceLocation(Embers.MODID, "caminite_ring_center")));
		flatItem(RegistryManager.CAMINITE_RING, "caminite_ring");

		ExistingModelFile ringEdgeModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "caminite_ring_edge"));
		ExistingModelFile ringCornerModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "caminite_ring_corner"));
		getVariantBuilder(RegistryManager.CAMINITE_RING_EDGE.get()).forAllStates(state -> {
			MechEdge edge = state.getValue(MechEdgeBlockBase.EDGE);

			return ConfiguredModel.builder()
					.modelFile(edge.corner ? ringCornerModel : ringEdgeModel)
					.rotationY(edge.rotation)
					.uvLock(false)
					.build();
		});

		simpleBlock(RegistryManager.CAMINITE_VALVE.get(), models().getExistingFile(new ResourceLocation(Embers.MODID, "caminite_ring_center")));
		flatItem(RegistryManager.CAMINITE_VALVE, "caminite_valve");

		ExistingModelFile valveEdgeModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "caminite_valve_edge"));
		getVariantBuilder(RegistryManager.CAMINITE_VALVE_EDGE.get()).forAllStates(state -> {
			MechEdge edge = state.getValue(MechEdgeBlockBase.EDGE);

			return ConfiguredModel.builder()
					.modelFile(edge.corner ? ringCornerModel : valveEdgeModel)
					.rotationY(edge.rotation)
					.uvLock(false)
					.build();
		});

		simpleBlock(RegistryManager.CRYSTAL_CELL.get(), models().getExistingFile(new ResourceLocation(Embers.MODID, "crystal_cell_center")));
		simpleBlockItem(RegistryManager.CRYSTAL_CELL.get(), models().cubeAll("crystal_cell", new ResourceLocation(Embers.MODID, "block/crate_crystal")));

		ExistingModelFile cellEdgeModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "crystal_cell_edge"));
		ExistingModelFile cellCornerModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "crystal_cell_corner"));
		getVariantBuilder(RegistryManager.CRYSTAL_CELL_EDGE.get()).forAllStates(state -> {
			MechEdge edge = state.getValue(MechEdgeBlockBase.EDGE);

			return ConfiguredModel.builder()
					.modelFile(edge.corner ? cellCornerModel : cellEdgeModel)
					.rotationY(edge.rotation)
					.uvLock(false)
					.build();
		});

		ExistingModelFile separatorModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "geologic_separator"));
		horizontalBlock(RegistryManager.GEOLOGIC_SEPARATOR.get(), separatorModel);
		simpleBlockItem(RegistryManager.GEOLOGIC_SEPARATOR.get(), separatorModel);

		ExistingModelFile chargerModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "copper_charger"));
		horizontalBlock(RegistryManager.COPPER_CHARGER.get(), chargerModel);
		simpleBlockItem(RegistryManager.COPPER_CHARGER.get(), chargerModel);

		ExistingModelFile siphonModel = models().getExistingFile(new ResourceLocation(Embers.MODID, "ember_siphon"));
		simpleBlock(RegistryManager.EMBER_SIPHON.get(), siphonModel);
		simpleBlockItem(RegistryManager.EMBER_SIPHON.get(), siphonModel);
	}

	public void blockWithItem(RegistryObject<? extends Block> registryObject) {
		//block model
		simpleBlock(registryObject.get());
		//itemblock model
		simpleBlockItem(registryObject.get(), cubeAll(registryObject.get()));
	}

	public void columnBlockWithItem(RegistryObject<? extends Block> registryObject, String sideTex, String topTex) {
		ResourceLocation side = new ResourceLocation(Embers.MODID, "block/" + sideTex);
		//ResourceLocation side_overlay = new ResourceLocation(Embers.MODID, "block/" + sideTex + "_overlay");
		ResourceLocation end = new ResourceLocation(Embers.MODID, "block/" + topTex);

		ModelFile model = models().cubeColumn(registryObject.getId().getPath(), side, end)/*.texture("overlay", side_overlay).renderType("cutout").element()
				.from(0, 0, 0)
				.to(16, 16, 16)
				.cube(topTex)
				.face(Direction.NORTH).texture("#overlay").cullface(Direction.NORTH).emissivity(8).end()
				.allFaces(
						(dir, f) -> {
							if (dir.getAxis() != Axis.Y) {
								f.texture("#overlay");
								f.cullface(dir);
								f.emissivity(8);
							}
						}).end()*/;
		//block model
		simpleBlock(registryObject.get(), model);
		//itemblock model
		simpleBlockItem(registryObject.get(), model);
	}

	public void blockWithItem(RegistryObject<? extends Block> registryObject, String model) {
		ExistingModelFile modelFile = models().getExistingFile(new ResourceLocation(Embers.MODID, model));
		//block model
		simpleBlock(registryObject.get(), modelFile);
		//itemblock model
		simpleBlockItem(registryObject.get(), modelFile);
	}

	public void blockWithItemTexture(RegistryObject<? extends Block> registryObject, String texture) {
		ModelFile modelFile = models().cubeAll(ForgeRegistries.BLOCKS.getKey(registryObject.get()).getPath(), new ResourceLocation(Embers.MODID, "block/" + texture));
		//block model
		simpleBlock(registryObject.get(), modelFile);
		//itemblock model
		simpleBlockItem(registryObject.get(), modelFile);
	}

	public void dial(RegistryObject<? extends Block> registryObject, String texture) {
		//block model
		ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(registryObject.get());
		ModelFile model = models().withExistingParent(loc.toString(), new ResourceLocation(Embers.MODID, "dial"))
				.texture("dial", new ResourceLocation(Embers.MODID, "block/" + texture))
				.texture("particle", new ResourceLocation(Embers.MODID, "block/" + texture));
		directionalBlock(registryObject.get(), model);

		//item model
		flatItem(registryObject, texture);
	}

	public void flatItem(RegistryObject<? extends Block> registryObject, String texture) {
		ResourceLocation loc = ForgeRegistries.BLOCKS.getKey(registryObject.get());
		itemModels().getBuilder(loc.toString())
		.parent(new ModelFile.UncheckedModelFile("item/generated"))
		.texture("layer0", new ResourceLocation(loc.getNamespace(), "item/" + texture));
	}

	public void betterDirectionalBlock(Block block, Function<BlockState, ModelFile> modelFunc, int angleOffset) {
		getVariantBuilder(block)
		.forAllStates(state -> {
			Direction dir = state.getValue(BlockStateProperties.FACING);
			return ConfiguredModel.builder()
					.modelFile(modelFunc.apply(state))
					.rotationX(dir == Direction.DOWN ? 180 : dir.getAxis().isHorizontal() ? 270 : 0)
					.rotationY(dir.getAxis().isVertical() ? 0 : (((int) dir.toYRot()) + angleOffset) % 360)
					.build();
		});
	}

	public void leverBlock(Block block, ModelFile lever, ModelFile leverFlipped) {
		getVariantBuilder(block).forAllStates(state -> {
			Direction facing = state.getValue(ButtonBlock.FACING);
			AttachFace face = state.getValue(ButtonBlock.FACE);
			boolean powered = state.getValue(ButtonBlock.POWERED);

			return ConfiguredModel.builder()
					.modelFile(powered ? leverFlipped : lever)
					.rotationX(face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180))
					.rotationY((int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot())
					.uvLock(false)
					.build();
		});
	}

	public void fluid(RegistryObject<? extends Block> fluid, String name) {
		simpleBlock(fluid.get(), models().cubeAll(name, new ResourceLocation(Embers.MODID, ModelProvider.BLOCK_FOLDER + "/fluid/" + name + "_still")));
	}

	public void decoBlocks(StoneDecoBlocks deco) {
		ResourceLocation resourceLocation = this.blockTexture(deco.block.get());

		if (deco.stairs != null) {
			this.stairsBlock(deco.stairs.get(), resourceLocation);
			this.itemModels().stairs(deco.stairs.getId().getPath(), resourceLocation, resourceLocation, resourceLocation);
		}
		if (deco.slab != null) {
			this.slabBlock(deco.slab.get(), deco.block.getId(), resourceLocation);
			this.itemModels().slab(deco.slab.getId().getPath(), resourceLocation, resourceLocation, resourceLocation);
		}
		if (deco.wall != null) {
			this.wallBlock(deco.wall.get(), resourceLocation);
			this.itemModels().wallInventory(deco.wall.getId().getPath(), resourceLocation);
		}
	}
}