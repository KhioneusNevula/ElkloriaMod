package com.gm910.elkloriamod.api.util;

import java.util.stream.IntStream;

import com.google.common.base.MoreObjects;
import com.mojang.datafixers.types.DynamicOps;

import net.minecraft.client.Minecraft;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.util.Constants.NBT;

public class ServerPos extends BlockPos {

	private int d;
	
	public ServerPos(Entity source) {
		super(source);
		d =  source.dimension.getId();
	}

	public ServerPos(Vec3d vec, int d) {
		super(vec);
		this.d = d;
	}

	public ServerPos(IPosition p_i50799_1_, int d) {
		super(p_i50799_1_);
		this.d = d;
		
	}

	public ServerPos(Vec3i source, int d) {
		super(source);
		this.d = d;
		// TODO Auto-generated constructor stub
	}

	public ServerPos(int x, int y, int z, int d) {
		super(x, y, z);
		this.d = d;
	}

	public ServerPos(double x, double y, double z, int d) {
		super(x, y, z);
		this.d = d;
	}
	
	public int getD() {
		return d;
	}
	
	public int getDimension() {
		return d;
	}
	
	public ServerPos setDimension(int d) {
		return new ServerPos(this, d);
	}
	
	public BlockPos getPos() {
		return new BlockPos(this);
	}
	
	public BlockPos castToPos() {
		return (BlockPos) this;
	}
	
	@Override
	public boolean equals(Object p_equals_1_) {
		if (!(p_equals_1_ instanceof ServerPos)) {
			return super.equals(p_equals_1_);
		} else {
			return super.equals(p_equals_1_) && ((ServerPos)p_equals_1_).d == d;
		}
	}
	
	public ServerPos up(int n) {
		return new ServerPos(super.up(n), d);
	}
	
	public ServerPos up() {
		return up(1);
	}
	
	public ServerPos down(int n) {
		return new ServerPos(super.down(n), d);
	}
	
	public ServerPos down() {
		return down(1);
	}
	
	public ServerPos north(int n) {
		return new ServerPos(super.north(n), d);
	}
	
	public ServerPos north() {
		// TODO Auto-generated method stub
		return new ServerPos(super.north(), d);
	}
	
	public ServerPos south(int n) {
		return new ServerPos(super.south(n), d);
	}
	
	public ServerPos south() {
		// TODO Auto-generated method stub
		return new ServerPos(super.south(), d);
	}
	
	public ServerPos east(int n) {
		return new ServerPos(super.east(n), d);
	}
	
	public ServerPos east() {
		// TODO Auto-generated method stub
		return new ServerPos(super.east(), d);
	}
	
	public ServerPos west(int n) {
		return new ServerPos(super.west(n), d);
	}
	
	public ServerPos west() {
		// TODO Auto-generated method stub
		return new ServerPos(super.west(), d);
	}
	
	public ServerPos offset(Direction facing, int n) {
		return new ServerPos(super.offset(facing, n), d);
	}
	
	public ServerPos offset(Direction facing) {
		return new ServerPos(super.offset(facing), d);
	}
	
	public ServerPos add(double x, double y, double z) {
		return new ServerPos(super.add(x, y, z), d);
	}
	
	public ServerPos add(int x, int y, int z) {
		return new ServerPos(super.add(x, y, z), d);
	}
	
	public ServerPos add(Vec3i vec) {
		return new ServerPos(super.add(vec), d);
	}
	
	@Override
	public String func_229422_x_() {
		return super.func_229422_x_() + ", " + d;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("x", this.getX()).add("y", this.getY()).add("z", this.getZ()).add("d", this.getD()).toString();
	}
	
	public ServerPos toImmutable() {
		return this;
	}
	
	public ServerPos subtract(Vec3i vec) {
		// TODO Auto-generated method stub
		return new ServerPos(super.subtract(vec), d);
	}
	
	public ServerPos rotate(Rotation rotationIn) {
		// TODO Auto-generated method stub
		return new ServerPos(super.rotate(rotationIn), d);
	}
	
	@Override
	public <T> T serialize(DynamicOps<T> p_218175_1_) {
		// TODO Auto-generated method stub
		return p_218175_1_.createIntList(IntStream.of(this.getX(), this.getY(), this.getZ(), this.getD()));
	}
	
	/**
	 * Gets blockpos from nbt OR serverpos depending on whether the nbt is configured for a serverpos or blockpos
	 * @param nbt
	 * @return
	 */
	public static BlockPos bpFromNBT(CompoundNBT nbt) {
		
		if (nbt.contains("D", NBT.TAG_INT)) {
			return new ServerPos(nbt.getInt("X"), nbt.getInt("Y"), nbt.getInt("Z"), nbt.getInt("D"));
		} else {
			return new BlockPos(nbt.getInt("X"), nbt.getInt("Y"), nbt.getInt("Z"));
		}
	}
	
	/**
	 * If returntype is not a serverpos, returns null;
	 */
	public static ServerPos fromNBT(CompoundNBT nbt) {
		BlockPos pos = bpFromNBT(nbt);
		return pos instanceof ServerPos ? (ServerPos)pos : null;
	}
	
	/**
	 * Works for blockpos or serverpos
	 * @param pos
	 * @return
	 */
	public static CompoundNBT toNBT(BlockPos pos) {
		CompoundNBT nbt = new CompoundNBT();
		nbt.putInt("X", pos.getX());
		nbt.putInt("Y", pos.getY());
		nbt.putInt("Z", pos.getZ());
		if (pos instanceof ServerPos) nbt.putInt("D", ((ServerPos)pos).getD());
		return nbt;
	}
	
	public CompoundNBT toNBT() {
		return ServerPos.toNBT(this);
	}
	
	public DimensionType getDimensionType() {
		return DimensionType.getById(d);
	}
	
	public World getWorld(MinecraftServer server) {
		return server.getWorld(this.getDimensionType());
	}
	
	public boolean isClientInWorld(Minecraft mc) {
		return mc.world.dimension.getType().getId() == this.d;
	}
	
}
