package com.bedrocklegends.woodenutilities.tile;

import com.bedrocklegends.woodenutilities.config.WoodenConfig;
import com.bedrocklegends.woodenutilities.item.UpgradeItem;
import com.bedrocklegends.woodenutilities.utility.NBTHelper;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

public class UpgradableTileEntity extends TileEntity {

    public static final String TAG_NUM_UPGRADES = "NumOfUpgrades";
    private int numOfUpgrades;
    public static final String TAG_UPGRADES = "Upgrades";
    private List<ItemStack> upgrades = Lists.newArrayList();

    public UpgradableTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT compound) {
        CompoundNBT nbt = super.write(compound);
        nbt.putInt(TAG_NUM_UPGRADES, this.numOfUpgrades);
        if (!this.upgrades.isEmpty()) {
            nbt.put(TAG_UPGRADES, NBTHelper.writeItemStackList(this.upgrades));
        }
        return nbt;
    }

    @Override
    public void read(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.read(state, nbt);
        this.numOfUpgrades = nbt.getInt(TAG_NUM_UPGRADES);
        if (nbt.contains(TAG_UPGRADES)) {
            this.upgrades = NBTHelper.readItemStackList(nbt.getCompound(TAG_UPGRADES));
        }
    }

    public int getNumOfUpgrades() {
        return this.numOfUpgrades;
    }

    public void setNumOfUpgrades(int amount) {
        this.numOfUpgrades = amount;
    }

    public List<ItemStack> getUpgrades() {
        return this.upgrades;
    }

    public boolean addUpgrade(ItemStack stack) {
        if (this.getNumOfUpgrades() == this.getMaxUpgrades()) return false;
        if (stack == null) return false;
        this.getUpgrades().add(stack);
        this.setNumOfUpgrades(this.getNumOfUpgrades() + 1);
        return true;
    }

    public List<ItemStack> getUpgradesFor(ItemStack toCheckFor) {
        return this.getUpgrades().stream().filter(s -> s.getItem() == toCheckFor.getItem()).collect(Collectors.toList());
    }

    public <T extends UpgradeItem> List<ItemStack> getUpgradesFor(RegistryObject<T> toCheckFor) {
        return this.getUpgradesFor(new ItemStack(toCheckFor.get()));
    }

    public boolean hasUpgrade(ItemStack stack) {
        return this.getUpgrades().stream().anyMatch(s -> s.getItem() == stack.getItem());
    }

    public <T extends UpgradeItem> boolean hasUpgrade(RegistryObject<T> toCheck) {
        return this.hasUpgrade(new ItemStack(toCheck.get()));
    }

    public int getMaxUpgrades() {
        return WoodenConfig.COMMON_CONFIG.maxUpgrades.get();
    }
}