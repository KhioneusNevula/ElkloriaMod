package com.gm910.elkloriamod.blocks;

import java.util.List;

import com.gm910.elkloriamod.init.TileInit;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LandvaetBlock extends ModBlock {

	public LandvaetBlock() {
		super(Block.Properties.create(Material.WOOD, MaterialColor.GOLD).lightValue(15));
		this.addTileEntity(() -> TileInit.LANDVAET.get(), null);
	}
	
	
	public static class Landvaet extends TileEntity implements ITickableTileEntity {

		private int lifetime;
		
		private List<BlockPos> positions;
		
		private int minimum = 70;
		
		private LivingEntity aggressionTarget = null;
		
		public Landvaet() {
			super(TileInit.LANDVAET.get());
			lifetime = 0;
		}
		
		@SubscribeEvent
		public void blockBreak(BlockEvent.BreakEvent event) {
			if (positions.contains(event.getPos()) && event.getWorld().getDimension().getType().equals(this.world.getDimension().getType())) {
				aggressionTarget = event.getPlayer();
			}
		}

		@Override
		public void tick() {
			if (!this.world.isRemote) {
				
				if (lifetime < 30 || lifetime % 50 == 0) {
					
					if (world.isBlockLoaded(pos)) {
						MinecraftForge.EVENT_BUS.register(this);
					} else {
						MinecraftForge.EVENT_BUS.unregister(this);
						return;
					}
					
					Biome thisBiome = world.getBiome(pos);
					
					ChunkPos thischunk = new ChunkPos(pos);
					
					positions.clear();
					
					for (int cxf = -5; cxf <= 5; cxf++) {
						for (int czf = -5; czf <= 5; czf++) {
							ChunkPos newChunkPos = new ChunkPos(thischunk.x + cxf, thischunk.z + czf);
							Chunk newChunk = this.world.getChunk(newChunkPos.x, newChunkPos.z);
							for (int x = 0; x <= 16; x++) {
								for (int z = 0; z <= 16; z++) {
									BlockPos posm = new BlockPos(newChunkPos.getXStart() + x, 0, newChunkPos.getZStart() + z);
									if (world.getBiome(posm).getRegistryName().equals(thisBiome.getRegistryName())) {
										for (int y = 255; y >= minimum; y--) {
											BlockPos pos1 = posm.up(y);
											if (!positions.contains(pos1)) {
												positions.add(pos1);
											}
										}
									}
								}
							}
						}
					}
					
				}
				
				lifetime++;
		    }
		}
		
		@Override
		public CompoundNBT serializeNBT() {
			CompoundNBT nbt = super.serializeNBT();
			
			nbt.putInt("Lifetime", lifetime);
			
			return nbt;
		}
		
		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			
			super.deserializeNBT(nbt);
			
			lifetime = nbt.getInt("Lifetime");
		}
		
		public int getLifetime() {
			return lifetime;
		}
		
		public List<BlockPos> getPositions() {
			return positions;
		}
		
		public int getMinimum() {
			return minimum;
		}
		
	}

}
