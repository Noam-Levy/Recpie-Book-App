package model;

public class Ingredient {
	String IngredientID, name, measurement, form;
	float amount;
	
	public Ingredient(String id, String name, String measurement, float amount, String form)
	{
		this.IngredientID = id;
		this.name = name;
		setMeasurement(measurement);
		this.amount = amount;
		this.form = form;
	}

	public String getIngrediantID() {
		return IngredientID;
	}

	public void setIngrediantID(String ingredientID) {
		IngredientID = ingredientID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMeasurement() {
		return measurement;
	}

	public void setMeasurement(String measurement) {
		if (measurement.toLowerCase().equals("c"))
			this.measurement = "cups";
		else if (measurement.isBlank())
			this.measurement = "units";
		else
			this.measurement = measurement;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	
}