package com.jmz.http;

import java.util.List;
/**
 * 相应包
 * @author Administrator
 *
 * @param <T>
 */
public class ApiResponse<T> {
	private T object;
	private List<T> array;


	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public List<T> getArray() {
		return array;
	}

	public void setArray(List<T> array) {
		this.array = array;
	}

	@Override
	public String toString() {
		return "ApiResponse [object=" + object + ", array=" + array + "]";
	}

}
