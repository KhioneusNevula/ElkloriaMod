package com.gm910.elkloriamod.events;


import com.gm910.elkloriamod.api.networking.TaskEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus=Bus.MOD)
public class ModEventHandler {

	@SubscribeEvent
	public static void onTask(TaskEvent event) {
		if (event.shouldRunByDefault()) {
			event.run();
		}
	}
	
}
