package com.misys.stockmarket.mbeans;

public class MyFollowingFormBean {
	private long userId;
	
	private String name;
	
	private String totalValue;
	
	private String followerCount;
	
	private String ranking;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public String getFollowerCount() {
		return followerCount;
	}

	public void setFollowerCount(String followerCount) {
		this.followerCount = followerCount;
	}

	public String getRanking() {
		return ranking;
	}

	public void setRanking(String ranking) {
		this.ranking = ranking;
	}
}
