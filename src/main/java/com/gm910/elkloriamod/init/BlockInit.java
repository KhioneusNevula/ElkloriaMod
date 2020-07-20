package com.gm910.elkloriamod.init;

import com.gm910.elkloriamod.Elkloria;
import com.gm910.elkloriamod.blocks.LandvaetBlock;
import com.gm910.elkloriamod.blocks.ModBlock.BlockRegistryObject;
import com.gm910.elkloriamod.blocks.NorikithintesPortalBlock;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BlockInit {
	private BlockInit() {}

	public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Elkloria.MODID);
	
	public static final RegistryObject<Block> N_PORTAL = (new BlockRegistryObject("norikithintes_portal", () -> new NorikithintesPortalBlock())).makeItem(() -> new Item.Properties().group(ItemGroup.TRANSPORTATION)).createRegistryObject();
	
	public static final RegistryObject<Block> LANDVAET = (new BlockRegistryObject("landvaet", () -> new LandvaetBlock())).makeItem(() -> new Item.Properties().group(ItemGroup.MISC)).createRegistryObject();

}
