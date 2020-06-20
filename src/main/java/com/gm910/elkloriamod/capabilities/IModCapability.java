package com.gm910.elkloriamod.capabilities;

import net.minecraft.entity.Entity;

public interface IModCapability<T> {

	public T $getOwner();
	
	public void $setOwner(T e);
}
