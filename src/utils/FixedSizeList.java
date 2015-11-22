package utils;

import java.util.ArrayList;

public class FixedSizeList<T> extends ArrayList<T> {
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
