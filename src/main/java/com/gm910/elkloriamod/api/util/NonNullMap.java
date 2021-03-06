package com.gm910.elkloriamod.api.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class NonNullMap<K, V> extends HashMap<K, V> {

	private BiFunction<K, NonNullMap<K, V>, V> supplier;
	
	public static final float DEFAULT_LOAD_FACTOR = ModReflect.getField(HashMap.class, float.class, "DEFAULT_LOAD_FACTOR", null);
	public static final int MAXIMUM_CAPACITY = ModReflect.getField(HashMap.class, int.class, "MAXIMUM_CAPACITY", null);
	
	
	public NonNullMap(BiFunction<K, NonNullMap<K, V>, V> supplier) {
		this.supplier = supplier;
	}
	
	public NonNullMap(Function<K, V> supplier) {
		this.supplier = (a, b) -> {
			return supplier.apply(a);
		};
	}
	
	public static <K, V> NonNullMap<K, V> create(Function<NonNullMap<K, V>, V> supplier) {
		BiFunction<K, NonNullMap<K, V>, V> supplier2 = (a, b) -> {
			return supplier.apply(b);
		};
		return new NonNullMap<K, V>(supplier2);
	}
	
	public NonNullMap(Supplier<V> supplier) {
		this.supplier = (a, b) -> {
			return supplier.get();
		};
	}
	
	public BiFunction<K, NonNullMap<K, V>, V> getSupplier() {
		return supplier;
	}
	
	@Override
	public V get(Object key) {
		
		return super.get(key) == null ? supplier.apply((K)key, this) : super.get(key);
	}
	
	
	/**
	 * Replaces the entire contents of this map with the contents of the other map
	 * Returns self for easy chaining (since the constructor only accepts suppliers)
	 * @param newmap
	 */
	public NonNullMap<K, V> setAs(Map<? extends K, ? extends V> newmap) {
		this.clear();
		this.putAll(newmap);
		return this;
	}
	
	/**
	 * Constructs map the same way as the given
	 * Returns self for easy chaining (since the constructor only accepts suppliers)
	 * @param newmap
	 */
	public NonNullMap<K, V> setAs(int capacity) {
		
		return setAs(capacity, DEFAULT_LOAD_FACTOR);
	}
	
	public NonNullMap<K, V> setAs(int initialCapacity, float loadFactor) {
		
		if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);
        
        System.out.println("Nonnullmapmaking " + ModReflect.setField(HashMap.class, float.class, "loadFactor", this, loadFactor));
        System.out.println("Nonnullmapmaking " + ModReflect.setField(HashMap.class, int.class, "threshold", this, ModReflect.run(HashMap.class, int.class, "tableSizeFor", null, initialCapacity)));
		
		return this;
	}

}
