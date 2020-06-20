package com.gm910.elkloriamod.init;

import com.gm910.elkloriamod.Elkloria;
import com.gm910.elkloriamod.world.ElkloriaDimensionFactory;
import com.gm910.elkloriamod.world.usifia.UsifiaDimension;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class DimensionInit {
	private DimensionInit() {}

	public static final DeferredRegister<ModDimension> WORLD_MAKERS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, Elkloria.MODID);
	
	public static final RegistryObject<ModDimension> USIFIA = WORLD_MAKERS.register("usifia", () -> ElkloriaDimensionFactory.withFactory(UsifiaDimension::new));
	
	public static ModDimension from(String name) {
		for (RegistryObject<ModDimension> reg : WORLD_MAKERS.getEntries()) {
			if (reg.getId().equals(new ResourceLocation(Elkloria.MODID, name))) { 
				return reg.get();
			}
		}
		return null;
	}
	
	
	public static RegistryObject<ModDimension> objFrom(String name) {
		for (RegistryObject<ModDimension> reg : WORLD_MAKERS.getEntries()) {
			if (reg.getId().equals(new ResourceLocation(Elkloria.MODID, name))) { 
				return reg;
			}
		}
		return null;
	}
	
	public static RegistryObject<ModDimension> objFrom(ModDimension of) {
		for (RegistryObject<ModDimension> reg : WORLD_MAKERS.getEntries()) {
			if (reg.get().equals(of)) { 
				return reg;
			}
		}
		return null;
	}
	
	public static String nameFrom(ModDimension of) {
		for (RegistryObject<ModDimension> reg : WORLD_MAKERS.getEntries()) {
			if (reg.get().equals(of)) { 
				return reg.getId().getPath();
			}
		}
		return null;
	}
	
	public static ModDimension from(RegistryObject<ModDimension> from) {
		for (RegistryObject<ModDimension> reg : WORLD_MAKERS.getEntries()) {
			if (reg.equals(from)) { 
				return reg.get();
			}
		}
		return null;
	}
	
	public static String nameFrom(RegistryObject<ModDimension> from) {
		for (RegistryObject<ModDimension> reg : WORLD_MAKERS.getEntries()) {
			if (reg.equals(from)) { 
				return reg.getId().getPath();
			}
		}
		return null;
	}
}
