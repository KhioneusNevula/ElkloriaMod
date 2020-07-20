package com.gm910.elkloriamod.init;

import com.gm910.elkloriamod.Elkloria;
import com.gm910.elkloriamod.blocks.LandvaetBlock.Landvaet;
import com.gm910.elkloriamod.blocks.NorikithintesPortalBlock.NorikithintesPortal;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class TileInit {
	private TileInit() {}
	
	public static final DeferredRegister<TileEntityType<?>> TILE_TYPES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, Elkloria.MODID);

	public static final RegistryObject<TileEntityType<NorikithintesPortal>> N_PORTAL = TILE_TYPES.register("norikithintes_portal", () -> TileEntityType.Builder.create(NorikithintesPortal::new, BlockInit.N_PORTAL.get()).build(null));
	
	public static final RegistryObject<TileEntityType<Landvaet>> LANDVAET = TILE_TYPES.register("landvaet", () -> TileEntityType.Builder.create(Landvaet::new, BlockInit.LANDVAET.get()).build(null));
}
