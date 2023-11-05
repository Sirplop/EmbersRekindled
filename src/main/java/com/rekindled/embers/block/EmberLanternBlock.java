package com.rekindled.embers.block;

import javax.annotation.Nullable;

import com.rekindled.embers.particle.GlowParticleOptions;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EmberLanternBlock extends Block implements SimpleWaterloggedBlock {

	public static final GlowParticleOptions EMBER = new GlowParticleOptions(GlowParticleOptions.EMBER_COLOR, 2.0F, 120);
	public static final VoxelShape LANTERN_AABB = Shapes.or(Block.box(6,0,6,10,13,10), Block.box(4,2,4,12,11,12));

	public EmberLanternBlock(Properties pProperties) {
		super(pProperties);
		this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.WATERLOGGED, false));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return LANTERN_AABB;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		for (int i = 0; i < 3; i ++) {
			level.addParticle(EMBER, pos.getX()+0.5f, pos.getY()+0.375f, pos.getZ()+0.5f, (random.nextFloat()-0.5f)*0.003f, (random.nextFloat())*0.003f, (random.nextFloat()-0.5f)*0.003f);
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext) {
		return this.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(pContext.getLevel().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER));
	}

	@Override
	public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
		if (pState.getValue(BlockStateProperties.WATERLOGGED)) {
			pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
		}
		return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
		pBuilder.add(BlockStateProperties.WATERLOGGED);
	}

	@Override
	public FluidState getFluidState(BlockState pState) {
		return pState.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(pState);
	}
}
