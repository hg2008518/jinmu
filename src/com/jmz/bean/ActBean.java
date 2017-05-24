package com.jmz.bean;

import java.io.Serializable;

public class ActBean implements Serializable{
	private String ActivityName;
	private String ActivityImgURL;
	private String MobileURL;
	private String ActivityURL;
	private String PropertyTypeID;
	private String PropertyID;
	private String NodeID ;
	private String SubjID ;
	private String PropertyName;
	private String Note;
	private String ProductID;

	public String getActivityName() {
		return ActivityName;
	}

	public String getPropertyTypeID() {
		return PropertyTypeID.length()>0?PropertyTypeID:"-1";
	}

	public void setPropertyTypeID(String propertyTypeID) {
		PropertyTypeID = propertyTypeID;
	}

	public String getPropertyID() {
		return PropertyID.length()>0?PropertyID:"-1";
	}

	public void setPropertyID(String propertyID) {
		PropertyID = propertyID;
	}

	public String getNodeID() {
		return NodeID.length()>0?NodeID:"-1";
	}

	public void setNodeID(String nodeID) {
		NodeID = nodeID;
	}

	public void setActivityName(String activityName) {
		ActivityName = activityName;
	}

	public String getActivityImgURL() {
		return ActivityImgURL;
	}

	public void setActivityImgURL(String activityImgURL) {
		ActivityImgURL = activityImgURL;
	}

	public String getActivityURL() {
		return ActivityURL.length()>0?ActivityURL:"-1";
	}

	public void setActivityURL(String activityURL) {
		ActivityURL = activityURL;
	}

	public String getMobileURL() {
		return MobileURL;
	}

	public void setMobileURL(String mobileURL) {
		MobileURL = mobileURL;
	}

	public String getPropertyName() {
		return PropertyName;
	}

	public void setPropertyName(String propertyName) {
		PropertyName = propertyName;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public String getProductID() {
		return ProductID.length()>0?ProductID:"-1";
	}

	public void setProductID(String productID) {
		ProductID = productID;
	}


	public String getSubjID() {
		return  SubjID.length()>0?SubjID:"-1";
	}

	public void setSubjID(String subjID) {
		SubjID = subjID;
	}
	
	@Override
	public String toString() {
		return "ActBean [ActivityName=" + ActivityName + ", ActivityImgURL="
				+ ActivityImgURL + ", MobileURL=" + MobileURL
				+ ", ActivityURL=" + ActivityURL + ", PropertyTypeID="
				+ PropertyTypeID + ", PropertyID=" + PropertyID + ", NodeID="
				+ NodeID + ", SubjID=" + SubjID + ", PropertyName="
				+ PropertyName + ", Note=" + Note + ", ProductID=" + ProductID
				+ "]";
	}
}
