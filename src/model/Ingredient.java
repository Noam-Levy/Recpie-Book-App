package model;

public class Ingredient {

	String ingredientID, name, measurement, form;
	float amount;
	
	public Ingredient(String id, float amount, String measurement, String name, String form)
	{
		this.ingredientID = id;
		this.name = name;
		setMeasurement(measurement);
		this.amount = amount;
		setFrom(form);
	}

	public String getIngrediantID() {
		return ingredientID;
	}

	public void setIngrediantID(String ingredientID) {
		this.ingredientID = ingredientID;
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
		if(measurement == null)
			return;
		if (measurement.toLowerCase().equals("c"))
			this.measurement = "cups";
		if (measurement.toLowerCase().equals("g"))
			this.measurement = "grams";
		else if (measurement.isBlank())
			this.measurement = "unit";
		else if(measurement.toLowerCase().equals("tbsp"))
				this.measurement = "tablespoons";
		else if(measurement.toLowerCase().equals("tsp"))
			this.measurement = "teaspoons";
		else if(measurement.toLowerCase().equals("units"))
			this.measurement = "unit";
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
		if(form == null)
			this.form = form;
		else
			this.form = form.replaceAll("\\s{2,}", " ").trim(); // removes multiple white spaces if any
	}
	
	public String getFrom() {
		return form;
	}

	@Override
	public String toString() {
		return "Ingredient : (name " + name + ", measurement " + measurement + ", form " + form + ", amount " + amount
				+ ")\n";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Ingredient)
			if(this.ingredientID.equals(((Ingredient)obj).ingredientID))
				return true;
		return false;
	}
	
	
}
