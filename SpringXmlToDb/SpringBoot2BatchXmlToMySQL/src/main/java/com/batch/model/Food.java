package com.batch.model;



public class Food {
	
	private int id;
	private String name;
	private String price;
	private String description;
	private int calories;

	public Food(){}

	public Food(int id, String name, String price, String description, int calories) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
		this.calories = calories;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	
	
	
}
