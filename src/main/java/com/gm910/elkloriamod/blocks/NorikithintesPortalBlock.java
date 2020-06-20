package com.gm910.elkloriamod.blocks;

import java.util.List;

import com.gm910.elkloriamod.init.BlockInit;
import com.gm910.elkloriamod.init.TileInit;
import com.gm910.elkloriamod.world.DimensionData;
import com.gm910.elkloriamod.world.Warper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

public class NorikithintesPortalBlock extends ModBlock {

	public NorikithintesPortalBlock() {
		super(Block.Properties.create(Material.PORTAL, MaterialColor.BLACK).doesNotBlockMovement().lightValue(15).hardnessAndResistance(-1.0F, 3600000.0F).noDrops());
		this.addTileEntity(() -> TileInit.N_PORTAL.get(), null);
	}
	
	@Override
	public Vec3d getFogColor(BlockState state, IWorldReader world, BlockPos pos, Entity entity, Vec3d originalColor,
			float partialTicks) {
		
		return Vec3d.ZERO;
	}
	
	public BlockRenderType getRenderType(BlockState state) {
	      return BlockRenderType.MODEL;
	}
	
	public static class NorikithintesPortal extends TileEntity implements ITickableTileEntity {

		public NorikithintesPortal() {
			super(TileInit.N_PORTAL.get());
		}

		@Override
		public void tick() {
			if (!this.world.isRemote) {
				
				MinecraftServer server = this.world.getServer();
	            ServerWorld otherworld;
	            
	            DimensionData.get(server);
				if (this.world.dimension.getType() == DimensionData.USIFIA) {
	            	otherworld = server.getWorld(DimensionType.OVERWORLD);
	            } else if (this.world.dimension.getType() == DimensionType.OVERWORLD) {
	            	DimensionData.get(server);
					otherworld = server.getWorld(DimensionData.USIFIA);
	            } else {
	            	world.setBlockState(this.pos, Blocks.AIR.getDefaultState());
	            	return;
	            }
	            
				 List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos()).grow(3));
		         if (!list.isEmpty()) {
		        	 ChunkPos cpos = new ChunkPos(pos);
		        	 otherworld.forceChunk(cpos.x, cpos.z, true);
		         }
				
		         list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.getPos()));
		         
		         
		         if (!list.isEmpty()) {
		        	
		            
		            int y = otherworld.getHeight();
		            BlockPos otherPortal = null;
		            
		            if (otherworld.getBlockState(pos).getBlock().equals(BlockInit.N_PORTAL.get())) {
		            	otherPortal = pos;
		            }
		            
		            if (otherPortal == null) {
		            	while (y >= 0) {
			            	BlockPos newPos = new BlockPos(pos.getX(), y, pos.getZ());
			            	if (otherworld.getBlockState(newPos).getBlock().equals(BlockInit.N_PORTAL.get())) {
			            		otherPortal = newPos;
			            		break;
			            	} else {
			            		y--;
			            	}
			            }
		            }
		            
		            if (otherPortal == null) {
		            	y = otherworld.getHeight();
		            	while (y >= 0) {
			            	BlockPos newPos = new BlockPos(pos.getX(), y, pos.getZ());
			            	if (otherworld.getBlockState(newPos).getMaterial().isReplaceable() 
			            			&& otherworld.getBlockState(newPos.down()).getMaterial().isOpaque()) {
			            		otherPortal = newPos;
			            		break;
			            	} else {
			            		y--;
			            	}
			            }
		            	
		            	if (otherPortal != null) {
		            		otherworld.setBlockState(otherPortal, BlockInit.N_PORTAL.get().getDefaultState());
		            	}
		            }
		            
		            if (otherPortal == null) {
		            	
		            	return;
		            }
		            
		            for (Entity en : list) {
		            	
		            	Warper.teleportEntity(en, otherworld.dimension.getType(), new Vec3d(otherPortal.north()));
		            }
		         }
		      }
		}
		
	}

}
