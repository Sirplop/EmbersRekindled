package com.rekindled.embers.blockentity;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.power.DefaultEmberCapability;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public class CopperCellBlockEntity extends BlockEntity {

	public IEmberCapability capability = new DefaultEmberCapability() {
		@Override
		public void onContentsChanged() {
			super.onContentsChanged();
			CopperCellBlockEntity.this.setChanged();
		}
	};

	public CopperCellBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(RegistryManager.COPPER_CELL_ENTITY.get(), pPos, pBlockState);
		capability.setEmberCapacity(24000);
		capability.setEmber(0);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		capability.deserializeNBT(nbt);
		if (capability.getEmberCapacity() == 0)
			capability.setEmberCapacity(24000);
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		capability.writeToNBT(nbt);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.remove && cap == EmbersCapabilities.EMBER_CAPABILITY) {
			return capability.getCapability(cap, side);
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		capability.invalidate();
	}
}
