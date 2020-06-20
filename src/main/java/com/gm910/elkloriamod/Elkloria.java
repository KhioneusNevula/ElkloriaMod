package com.gm910.elkloriamod;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gm910.elkloriamod.api.networking.Sides;
import com.gm910.elkloriamod.api.networking.messages.ModChannels;
import com.gm910.elkloriamod.api.networking.messages.Networking.StringMessage;
import com.gm910.elkloriamod.capabilities.CapabilityProvider;
import com.gm910.elkloriamod.init.BiomeInit;
import com.gm910.elkloriamod.init.BlockInit;
import com.gm910.elkloriamod.init.DimensionInit;
import com.gm910.elkloriamod.init.ItemInit;
import com.gm910.elkloriamod.init.TileInit;
import com.gm910.elkloriamod.keys.ModKeys;
import com.gm910.elkloriamod.world.DimensionData;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Elkloria.MODID)
public class Elkloria
{
    
	public static final String MODID = "elkloriamod";
	
	public static final String NAME = "Elkloria Mod";
    public static final String VERSION = "1.0";
	
    private static final Logger LOGGER = LogManager.getLogger();
    
    public static Elkloria instance;

    public Elkloria() {
    	
        instance = this;
        
        
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        ModKeys.firstinit();
        
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        TileInit.TILE_TYPES.register(modBus);
        
        ItemInit.ITEMS.register(modBus);
        
        BlockInit.BLOCKS.register(modBus);
        
        BiomeInit.BIOMES.register(modBus);
        
        DimensionInit.WORLD_MAKERS.register(modBus);
        
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM PREINIT");
        CapabilityProvider.preInit();
        
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        ModKeys.clientinit();
        ModChannels.INSTANCE.registerMessage(ModChannels.id++, StringMessage.class, StringMessage::encode, StringMessage::fromBuffer, StringMessage::handle);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("elkloriamod", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }
    
    

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        
    }
    
    @SubscribeEvent
    public void onServerStarted(FMLServerStartedEvent event) {
    	
    	DimensionData dat = DimensionData.get(event.getServer());
    	dat.storeInitialDimensions();
    	dat.registerStoredDimensions();
    }
    
    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {

    	DimensionData dat = DimensionData.get(event.getServer());
    	dat.unregisterStoredDimensions();
    }

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
    	
    	
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            // register a new block here
        }
        
    }
    
    @EventBusSubscriber(bus=EventBusSubscriber.Bus.FORGE)
    public static class ForgeEvents {
    	
    	@SubscribeEvent
    	public static void onDimensionRegister(RegisterDimensionsEvent event) {
    		DimensionData.registerInitialDimensionsStatic();
    	}
        
    }
}
