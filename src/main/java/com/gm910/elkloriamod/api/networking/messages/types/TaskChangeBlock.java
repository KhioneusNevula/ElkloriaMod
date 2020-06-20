package com.gm910.elkloriamod.api.networking.messages.types;

import javax.annotation.Nullable;

import com.gm910.elkloriamod.api.networking.messages.ModTask;
import com.gm910.elkloriamod.api.util.ServerPos;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TaskChangeBlock extends ModTask {

	private BlockState state;
	private ServerPos pos;
	private CompoundNBT tile;
	private int flag;

	public TaskChangeBlock() {}
	
	/**
	 * 
	 * @param newstate the state to change to
	 * @param position the position to change
	 * @param tiledata the tile entity data, if it is to be changed
	 * @param flag the change flag
	 */
	public TaskChangeBlock(BlockState newstate, ServerPos position, @Nullable TileEntity tile, int flag) {
		this.state = newstate;
		this.pos = position;
		this.tile = tile != null ? tile.serializeNBT() : null;
		this.flag = flag;
	}
	
	public TaskChangeBlock(BlockState newstate, ServerPos position, @Nullable TileEntity tile) {
		this(newstate, position, tile, 3);
	}
	
	public TaskChangeBlock(BlockState newstate, ServerPos position, int flag) {
		this(newstate, position, null, flag);
	}
	
	public TaskChangeBlock(BlockState newstate, ServerPos position) {
		this(newstate, position, null, 3);
	}
	
	@Override
	public void run() {
		World world = getWorldRef();
		TileEntity te = tile != null ? TileEntity.create(tile) : null;
		if (world != null) {
			if (world.isRemote) {
				if (world.dimension.getType().getId() != this.pos.getD()) {
					world.setBlockState(pos, state, flag);
					if (te != null) {
						world.setTileEntity(pos, te);
					}
				}
			} else {
				world = world.getServer().getWorld(world.dimension.getType());
				world.setBlockState(pos, state, flag);
				if (te != null) {
					world.setTileEntity(pos, te);
				}
			}
		}
	}
	
	@Override
	public String write() {
		CompoundNBT data = new CompoundNBT();
		
		data.put("State", NBTUtil.writeBlockState(state));
		data.put("Pos", pos.toNBT());
		if (tile != null) data.put("TileData", tile);
		data.putInt("Flag", flag);
		
		return data.toString();
	}
	
	@Override
	protected void read(String str) {
		CompoundNBT nbt = null;
		try {
			nbt = JsonToNBT.getTagFromJson(str);
		} catch (CommandSyntaxException e) {
			System.err.println("ERROR when reading an nbt compound for a changeblockmessage");
			e.printStackTrace();
			return;
		}
		this.state = NBTUtil.readBlockState(nbt.getCompound("State"));
		this.pos = ServerPos.fromNBT(nbt.getCompound("Pos"));
		this.tile = nbt.contains("TileData") ? nbt.getCompound("TileData") : null;
		this.flag = nbt.getInt("Flag");
		
		super.read(str);
	}

	@Override
	public boolean isLClient() {
		
		return true;
	}

	@Override
	public boolean isLServer() {
		return true;
	}

}
