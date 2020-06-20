package com.gm910.elkloriamod.api.networking.messages.types;

import com.gm910.elkloriamod.api.events.ServerKeyEvent;
import com.gm910.elkloriamod.api.networking.messages.ModTask;

import net.minecraftforge.common.MinecraftForge;

public class TaskKeyPress extends ModTask {
	
	public final int key;

	public TaskKeyPress() {
		key = 0;
	}
	
	public TaskKeyPress(int key) {
		this.key = key;
	}

	@Override
	public void run() {
		MinecraftForge.EVENT_BUS.post(new ServerKeyEvent(key));
	}

}
