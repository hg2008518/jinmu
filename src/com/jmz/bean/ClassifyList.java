package com.jmz.bean;

import java.io.Serializable;
import java.util.List;

public class ClassifyList {
	public List<Classify> classify;

	public List<Classify> getClassify() {
		return classify;
	}

	public void setClassify(List<Classify> classify) {
		this.classify = classify;
	}

	@Override
	public String toString() {
		return "ClassifyList [classify=" + classify + "]";
	}

	
}
