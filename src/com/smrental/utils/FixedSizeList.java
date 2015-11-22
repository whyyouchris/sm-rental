package com.smrental.utils;

import java.util.ArrayList;

public class FixedSizeList<T> extends ArrayList<T> {
	/**
	 * A fixed size arrayList implementation, initialized with capacity
	 * will not accept any element more than the given limit
	 */
	private static final long serialVersionUID = 6171561822731976460L;
	private int limit;
	
	public FixedSizeList(int limit) {
		this.limit = limit;
	}
	
	@Override public boolean add(T item){
		if (this.size() > this.limit) {
			return false;
		}
		return super.add(item);
	}
}
