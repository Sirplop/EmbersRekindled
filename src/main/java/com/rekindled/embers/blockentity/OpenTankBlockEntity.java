package com.rekindled.embers.blockentity;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;

public abstract class OpenTankBlockEntity extends FluidHandlerBlockEntity {

	FluidStack lastEscaped = null;
	long lastEscapedTickServer;
	long lastEscapedTickClient;

	public OpenTankBlockEntity(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		if(nbt.contains("lastEscaped")) {
			lastEscaped = FluidStack.loadFluidStackFromNBT(nbt.getCompound("lastEscaped"));
			lastEscapedTickServer = nbt.getLong("lastEscapedTick");
		}
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		if (lastEscaped != null) {
			nbt.put("lastEscaped", lastEscaped.writeToNBT(new CompoundTag()));
			nbt.putLong("lastEscapedTick", lastEscapedTickServer);
		}
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		tank.writeToNBT(nbt);
		if (lastEscaped != null) {
			nbt.put("lastEscaped", lastEscaped.writeToNBT(new CompoundTag()));
			nbt.putLong("lastEscapedTick", lastEscapedTickServer);
		}
		return nbt;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public void setEscapedFluid(FluidStack stack) {
		lastEscaped = stack;
		lastEscapedTickServer = level.getLevelData().getGameTime();

		this.setChanged();
	}

	protected boolean shouldEmitParticles() {
		if(lastEscaped == null)
			return false;
		if(lastEscapedTickClient < lastEscapedTickServer) {
			lastEscapedTickClient = lastEscapedTickServer;
			return true;
		}
		long dTime = level.getLevelData().getGameTime() - lastEscapedTickClient;
		if(dTime < lastEscaped.getAmount() + 5)
			return true;
		return false;
	}

	protected abstract void updateEscapeParticles();
}
