package com.rekindled.embers.blockentity;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.joml.Vector3f;

import com.rekindled.embers.Embers;
import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.tile.IExtraCapabilityInformation;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.particle.VaporParticleOptions;
import com.rekindled.embers.recipe.IGaseousFuelRecipe;
import com.rekindled.embers.upgrade.CatalyticPlugUpgrade;
import com.rekindled.embers.util.sound.ISoundController;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class CatalyticPlugBlockEntity extends BlockEntity implements ISoundController, IExtraCapabilityInformation {

	public static final int SOUND_OFF = 1;
	public static final int SOUND_ON = 2;
	public static final int[] SOUND_IDS = new int[]{SOUND_OFF,SOUND_ON};

	int ticksExisted = 0;
	public float renderOffset;
	int previousFluid;
	public int activeTicks = 0;
	public int burnTime = 0;
	public CatalyticPlugUpgrade upgrade;
	public FluidTank tank = new FluidTank(FluidType.BUCKET_VOLUME * 4) {
		@Override
		public void onContentsChanged() {
			CatalyticPlugBlockEntity.this.setChanged();
		}
	};
	private static Random random = new Random();
	public IGaseousFuelRecipe cachedRecipe = null;

	HashSet<Integer> soundsPlaying = new HashSet<>();
	public LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

	public CatalyticPlugBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(RegistryManager.CATALYTIC_PLUG_ENTITY.get(), pPos, pBlockState);
		upgrade = new CatalyticPlugUpgrade(this);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		tank.readFromNBT(nbt.getCompound("tank"));
		activeTicks = nbt.getInt("active");
		burnTime = nbt.getInt("burnTime");
	}

	@Override
	public void saveAdditional(CompoundTag nbt) {
		super.saveAdditional(nbt);
		nbt.put("tank", tank.writeToNBT(new CompoundTag()));
		nbt.putInt("active", activeTicks);
		nbt.putInt("burnTime", burnTime);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		nbt.put("tank", tank.writeToNBT(new CompoundTag()));
		nbt.putInt("active", activeTicks);
		nbt.putInt("burnTime", burnTime);
		return nbt;
	}

	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	public void setActive(int ticks) {
		activeTicks = Math.max(ticks, activeTicks);
		setChanged();
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, CatalyticPlugBlockEntity blockEntity) {
		blockEntity.activeTicks--;
	}

	public static void clientTick(Level level, BlockPos pos, BlockState state, CatalyticPlugBlockEntity blockEntity) {
		blockEntity.ticksExisted++;

		//I know I'm supposed to use onLoad for stuff on the first tick but the tank isn't synced to the client yet when that happens
		if (blockEntity.ticksExisted == 1)
			blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
		if (blockEntity.tank.getFluidAmount() != blockEntity.previousFluid) {
			blockEntity.renderOffset = blockEntity.renderOffset + blockEntity.tank.getFluidAmount() - blockEntity.previousFluid;
			blockEntity.previousFluid = blockEntity.tank.getFluidAmount();
		}
		blockEntity.handleSound();
		blockEntity.activeTicks--;

		if (blockEntity.activeTicks > 0 && state.hasProperty(BlockStateProperties.FACING)) {
			Direction facing = state.getValue(BlockStateProperties.FACING);
			float yoffset = 0.38f;
			float wideoffset = 0.45f;
			Vec3 baseOffset = new Vec3(0.5 - facing.getNormal().getX() * yoffset, 0.5 - facing.getNormal().getY() * yoffset, 0.5 - facing.getNormal().getZ() * yoffset);
			Direction[] planars;
			switch (facing.getAxis()) {
			case X:
				planars = new Direction[] {Direction.DOWN,Direction.UP,Direction.NORTH,Direction.SOUTH}; break;
			case Y:
				planars = new Direction[] {Direction.EAST,Direction.WEST,Direction.NORTH,Direction.SOUTH}; break;
			case Z:
				planars = new Direction[] {Direction.DOWN,Direction.UP,Direction.EAST,Direction.WEST}; break;
			default:
				planars = null; break;
			}
			@SuppressWarnings("resource")
			Vector3f color = IClientFluidTypeExtensions.of(blockEntity.tank.getFluid().getFluid().getFluidType()).modifyFogColor(Minecraft.getInstance().gameRenderer.getMainCamera(), 0, (ClientLevel) level, 6, 0, new Vector3f(1, 1, 1));
			for (Direction planar : planars) {
				BlockState sideState = level.getBlockState(pos.relative(planar));
				if (!sideState.getFaceOcclusionShape(level, pos.relative(planar), planar.getOpposite()).isEmpty())
					continue;
				float x = pos.getX() + (float) baseOffset.x + planar.getNormal().getX() * wideoffset;
				float y = pos.getY() + (float) baseOffset.y + planar.getNormal().getY() * wideoffset;
				float z = pos.getZ() + (float) baseOffset.z + planar.getNormal().getZ() * wideoffset;
				float motionx = planar.getNormal().getX() * 0.053f - facing.getNormal().getX() * 0.015f - 0.01f + random.nextFloat() * 0.02f;
				float motiony = planar.getNormal().getY() * 0.053f - facing.getNormal().getY() * 0.015f - 0.01f + random.nextFloat() * 0.02f;
				float motionz = planar.getNormal().getZ() * 0.053f - facing.getNormal().getZ() * 0.015f - 0.01f + random.nextFloat() * 0.02f;

				level.addParticle(new VaporParticleOptions(color, new Vec3(motionx, motiony, motionz), 1.25f), x, y, z, 0, 0, 0);
			}
		}
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (!this.remove && level.getBlockState(worldPosition).hasProperty(BlockStateProperties.FACING)) {
			Direction facing = level.getBlockState(worldPosition).getValue(BlockStateProperties.FACING);
			if (cap == ForgeCapabilities.FLUID_HANDLER && (side == null || side == facing)) {
				return ForgeCapabilities.FLUID_HANDLER.orEmpty(cap, holder);
			}
			if (cap == EmbersCapabilities.UPGRADE_PROVIDER_CAPABILITY && (side == null || side.getOpposite() == facing)) {
				return upgrade.getCapability(cap, side);
			}
		}
		return super.getCapability(cap, side);
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		holder.invalidate();
		upgrade.invalidate();
	}

	@Override
	public void setChanged() {
		super.setChanged();
		if (!level.isClientSide())
			((ServerLevel) level).getChunkSource().blockChanged(worldPosition);
	}

	@Override
	public void playSound(int id) {
		float soundX = (float) worldPosition.getX() + 0.5f;
		float soundY = (float) worldPosition.getY() + 0.5f;
		float soundZ = (float) worldPosition.getZ() + 0.5f;
		switch (id) {
		case SOUND_ON:
			EmbersSounds.playMachineSound(this, SOUND_ON, EmbersSounds.CATALYTIC_PLUG_LOOP.get(), SoundSource.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
			level.playLocalSound(soundX, soundY, soundZ, EmbersSounds.CATALYTIC_PLUG_START.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
			break;
		case SOUND_OFF:
			EmbersSounds.playMachineSound(this, SOUND_OFF, EmbersSounds.CATALYTIC_PLUG_LOOP_READY.get(), SoundSource.BLOCKS, true, 1.0f, 1.0f, soundX, soundY, soundZ);
			break;
		}
		soundsPlaying.add(id);
	}

	@Override
	public void stopSound(int id) {
		if (id == SOUND_ON) {
			level.playLocalSound(worldPosition, EmbersSounds.CATALYTIC_PLUG_STOP.get(), SoundSource.BLOCKS, 1.0f, 1.0f, false);
		}
		soundsPlaying.remove(id);
	}

	@Override
	public boolean isSoundPlaying(int id) {
		return soundsPlaying.contains(id);
	}

	@Override
	public int[] getSoundIDs() {
		return SOUND_IDS;
	}

	@Override
	public boolean shouldPlaySound(int id) {
		boolean isWorking = activeTicks > 0;

		switch (id) {
		case SOUND_OFF: return !isWorking && tank.getFluidAmount() > 0;
		case SOUND_ON: return isWorking;
		default: return false;
		}
	}

	@Override
	public float getCurrentVolume(int id, float volume) {
		boolean isWorking = activeTicks > 0;

		switch (id) {
		case SOUND_OFF: return !isWorking ? 1.0f : 0.0f;
		case SOUND_ON: return isWorking ? 1.0f : 0.0f;
		default: return 0f;
		}
	}

	@Override
	public boolean hasCapabilityDescription(Capability<?> capability) {
		return capability == ForgeCapabilities.FLUID_HANDLER;
	}

	@Override
	public void addCapabilityDescription(List<Component> strings, Capability<?> capability, Direction facing) {
		if (capability == ForgeCapabilities.FLUID_HANDLER)
			strings.add(IExtraCapabilityInformation.formatCapability(EnumIOType.INPUT, Embers.MODID + ".tooltip.goggles.fluid", Component.translatable(Embers.MODID + ".tooltip.goggles.fluid.steam")));
	}
}
