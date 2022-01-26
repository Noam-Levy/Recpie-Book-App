package model;

public class Ingredient {
	String IngredientID, name, measurement, form;
	float amount;
	
	public Ingredient(String id, float amount, String measurement, String name, String form)
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
		else if(measurement.toLowerCase().equals("tbsp"))
				this.measurement = "tablespoons";
		else
			this.measurement = measurement;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}
	
	public void setFrom(String form) {
		this.form = form;
	}
	
	public String getFrom() {
		return form;
	}

	@Override
	public String toString() {
		return "Ingredient : (name " + name + ", measurement " + measurement + ", form " + form + ", amount " + amount
				+ ")\n";
	}
	
	
	
	
}
