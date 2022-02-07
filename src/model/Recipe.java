package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;


public class Recipe {

	private String recipeID, recipieName;
	private int cookTime, servings;
	private HashMap<Integer,String> instructions; // step number, instruction
	private int totalSteps;
	private ArrayList<Ingredient> ingredients;
	private HashMap<String, String> cuisine; // id, name
	
	public Recipe() {
		this.ingredients = new ArrayList<Ingredient>();
		this.instructions = new HashMap<Integer,String>();
		this.totalSteps = 0;
		this.cuisine = new HashMap<String, String>();
	}
	
	public Recipe(String id, String name, int cookTime, int servings) {
		this();
		this.recipeID = id;
		this.recipieName = name;
		this.cookTime = cookTime;
		this.servings = servings;
	}

	public String getRecipeID() {
		if(recipeID == null)
			return "null";
		return recipeID;
	}

	public void setRecipeID(String recipeID) {
		this.recipeID = recipeID;
	}

	public String getRecipeName() {
		return recipieName;
	}

	public void setRecipieName(String recipieName) {
		this.recipieName = recipieName;
	}

	public int getCookTime() {
		return cookTime;
	}

	public void setCookTime(int cookTime) {
		this.cookTime = cookTime;
	}

	public int getServings() {
		return servings;
	}

	public void setServings(int servings) {
		this.servings = servings;
	}

	public HashMap<Integer, String> getInstructions() {
		return instructions;
	}
	
	public void addInstruction(String stepInfo) {
		/*
		 * To be used when instruction length is unknown (such as from spoonacular API) 
		 * in order to be able to save the recipe into the database.
		 */
		StringTokenizer tokenizer = new StringTokenizer(stepInfo,".");
		if(tokenizer.countTokens() > 1) {
			while(tokenizer.hasMoreTokens())
				addInstruction(++totalSteps, tokenizer.nextToken().trim()+".");
			}
		else {
			addInstruction(++totalSteps, stepInfo);
		}
	}
	
	public void addInstruction(int stepNumber, String stepInfo) {
			this.instructions.put(stepNumber, stepInfo);
			this.totalSteps = stepNumber;
	}
	
	public void setInstructions(HashMap<Integer, String> recipeInstructions) {
		this.instructions = recipeInstructions;	
	}

	public ArrayList<Ingredient> getIngrediants() {
		return ingredients;
	}

	public void addIngrediant(Ingredient i) {
		for (Ingredient ingredient : ingredients)
			if(i.equals(ingredient))
				return;
		this.ingredients.add(i);
	}
	
	public void setIngredients(ArrayList<Ingredient> recipeIngredients) {
		this.ingredients = recipeIngredients;
		
	}

	public HashMap<String, String> getCuisine() {
		return cuisine;
	}

	public void addCuisine(String id, String name) {
		cuisine.put(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Recipe) {
			return this.recipeID.equalsIgnoreCase(((Recipe) obj).getRecipeID());		
		}
		return false;
	}
}
