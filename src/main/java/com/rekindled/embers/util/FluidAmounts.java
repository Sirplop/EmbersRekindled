package com.rekindled.embers.util;

import com.rekindled.embers.Embers;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class FluidAmounts {

	public static final int NUGGET_AMOUNT = 10;
	public static final int INGOT_AMOUNT = NUGGET_AMOUNT * 9;
	public static final int BLOCK_AMOUNT = INGOT_AMOUNT * 9;
	public static final int RAW_AMOUNT = NUGGET_AMOUNT * 12;
	public static final int ORE_AMOUNT = RAW_AMOUNT * 2;
	public static final int RAW_BLOCK_AMOUNT = RAW_AMOUNT * 9;
	public static final int PLATE_AMOUNT = INGOT_AMOUNT;
	public static final int GEAR_AMOUNT = INGOT_AMOUNT * 2;

	public static MutableComponent getIngotTooltip(int amount) {
		MutableComponent tooltip = null;
		MutableComponent ingots = null;
		MutableComponent nuggets = null;
		MutableComponent mb = null;

		if (amount >= INGOT_AMOUNT) {
			int count = amount / INGOT_AMOUNT;
			if (count == 1) {
				ingots = Component.translatable(Embers.MODID + ".tooltip.fluiddial.ingot");
			} else {
				ingots = Component.translatable(Embers.MODID + ".tooltip.fluiddial.ingots", count);
			}
		}
		if (amount % INGOT_AMOUNT >= NUGGET_AMOUNT) {
			int count = (amount % INGOT_AMOUNT) / NUGGET_AMOUNT;
			if (count == 1) {
				nuggets = Component.translatable(Embers.MODID + ".tooltip.fluiddial.nugget");
			} else {
				nuggets = Component.translatable(Embers.MODID + ".tooltip.fluiddial.nuggets", count);
			}
		}
		if (amount % NUGGET_AMOUNT > 0) {
			int count = amount % NUGGET_AMOUNT;
			mb = Component.translatable(Embers.MODID + ".tooltip.fluiddial.millibucket", count);
		}

		if (ingots == null && nuggets == null) {
			return Component.empty();
		}
		if (ingots != null) {
			tooltip = ingots;
		}
		if (nuggets != null) {
			if (tooltip == null) {
				tooltip = nuggets;
			} else {
				tooltip = Component.translatable(Embers.MODID + ".tooltip.fluiddial.separator", tooltip, nuggets);
			}
		}
		if (mb != null) {
			tooltip = Component.translatable(Embers.MODID + ".tooltip.fluiddial.separator", tooltip, mb);
		}

		return tooltip;
	}
}
