package model;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RecipeFetcher {
	
	private static RecipeFetcher _instance = null;

	private JSONParser parser;
	private HttpRequest request;
	private HttpResponse<String> response;

	private String APIKey; 

	private RecipeFetcher(String APIKey) {
		this.parser = new JSONParser();
		this.APIKey = ""; // change to key from config file.
	}
	
	private void setKey(String APIKey) {
		this.APIKey = APIKey;
	}

	public static RecipeFetcher getInstance(String APIKey) {
		if (_instance == null)
			_instance = new RecipeFetcher(APIKey);
		_instance.setKey(APIKey);
		return _instance;
	}
	
	private void checkResponseCode(int statusCode) throws ConnectException {
		if (statusCode == 401)
			throw new ConnectException("Invalid API key");
	}

	public Recipe getRandomRecipe() throws Exception {
		/*
		 * Request random recipe from the API and returns it as a new recipe object.
		 * the method compares the recipe with the DB and (if necessary) adds it if it is not in the DB
		 */
		this.request = HttpRequest.newBuilder()
				.uri(URI.create("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=1"))
				.header("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
				.header("x-rapidapi-key", this.APIKey) 
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		this.response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		checkResponseCode(response.statusCode());
				
		JSONObject recipieData = (JSONObject)((JSONArray)(((JSONObject)this.parser.parse(this.response.body())).get("recipes"))).get(0);
		
		if(recipieData == null)
			throw new ConnectException("Invalid response from API");
		
		return createRecipie(recipieData);
	}
	
	public Recipe createRecipeFromWebPage(String url) throws Exception { // NEEDS TO BE CHECEKD
		/*
		 * Translates web page into recipe - WORKS ONLY FOR ENGLISH RECIPE PAGES 
		 */
		this.request = HttpRequest.newBuilder()
				.uri(URI.create("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/extract?url=" + url))
				.header("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
				.header("x-rapidapi-key", this.APIKey)
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		this.response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		checkResponseCode(response.statusCode());
		
		JSONObject recipieData = (JSONObject)((JSONArray)(((JSONObject)this.parser.parse(this.response.body())).get("recipes"))).get(0);
		
		if(recipieData == null)
			throw new ConnectException("Invalid response from API");

			return createRecipie(recipieData);
	}
	
	public ArrayList<Recipe> searchRecipe(String query) throws Exception { // NEEDS TO BE CHECEKD
		/*
		 *  Allows the user to search for recipes in natural language
		 */
		this.request = HttpRequest.newBuilder()
				.uri(URI.create("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/search?query="+query.replaceAll(" ", "%20")))
				.header("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
				.header("x-rapidapi-key", this.APIKey)
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		this.response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		checkResponseCode(response.statusCode());
		
		JSONArray recipiesData = ((JSONArray)(((JSONObject)this.parser.parse(this.response.body())).get("recipes")));
		
		if(recipiesData == null)
			throw new ConnectException("Invalid response from API");
		
		if(recipiesData.isEmpty())
			throw new Exception("No recpies found.");
		
		@SuppressWarnings("unchecked") // spoonacular API returns JSON objects inside of the JSON array. 
		Iterator<JSONObject> it = recipiesData.iterator();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		while(it.hasNext())
			recipes.add(createRecipie(it.next()));
		return recipes;
	}

	private void addRecipeToDB(Recipe recipe) throws SQLException {
		// Compares the recipe with the DB and (if necessary) adds it to the DB.
		DBManager manager = DBManager.getInstance();
		if(manager.requestRecipeByID(recipe.getRecipeID()) == null)
			manager.addRecipe(recipe);
	}
	
	public String convertMeasurementToGrams(Ingredient i) throws IOException, InterruptedException, ParseException {
		/* can be used to convert to other units - I.E: 1000 grams tomatoes = 8 piece(s).
		 * 								  				1000 grams beef = 35.27 o.z
		 * 								  				3 egg yolks = 54 ml
		 * 			
		 * 				CANNOT CONVERT NON-STANDARD MEASUREMENTS!!
		 */
		this.request = HttpRequest.newBuilder()
				.uri(URI.create("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/convert?ingredientName="+i.getName()
								+"&targetUnit=grams&sourceUnit="+i.getMeasurement()
								+"&sourceAmount="+i.getAmount()))
				.header("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
				.header("x-rapidapi-key", this.APIKey)
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		
		this.response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		checkResponseCode(response.statusCode());
		
		JSONObject conversionData = ((JSONObject)this.parser.parse(this.response.body()));
		if(conversionData.get("status") == null)
			return conversionData.get("answer").toString();

		throw new InvalidParameterException("Cannot convert from " + i.getMeasurement());
	}
	
	private Recipe createRecipie(JSONObject recipieData) throws SQLException {
		/*
		 * translates and returns received JSONObject from spoonacular API as recipe object.
		 */
		String id = ((Long)(recipieData.get("id"))).toString();
		String title = (String)recipieData.get("title");
		int readyInMinutes = ((Long)recipieData.get("readyInMinutes")).intValue();
		int servings = ((Long)recipieData.get("servings")).intValue();

		Recipe randomRecipe = new Recipe(id,title,readyInMinutes,servings);

		JSONArray instructionsData = (JSONArray)((JSONObject)((JSONArray)recipieData.get("analyzedInstructions")).get(0)).get("steps");
		Iterator<?> it = instructionsData.iterator();
		while(it.hasNext())	{
			JSONObject currentInstruction = (JSONObject)it.next();
			randomRecipe.addInstruction(((Long)currentInstruction.get("number")).intValue(), (String)currentInstruction.get("step"));
		}

		JSONArray ingrediantsData = (JSONArray)recipieData.get("extendedIngredients");
		it = ingrediantsData.iterator();
		while(it.hasNext()) {
			JSONObject currentIngredient = (JSONObject)it.next();
			String form = ((String)(currentIngredient.get("originalName")));
			form = form.replaceAll(((String)(currentIngredient.get("name"))), "").replaceAll(",","");
			randomRecipe.addIngrediant(new Ingredient(
					((Long)(recipieData.get("id"))).toString(), (String)(currentIngredient.get("name")),
					(String)(currentIngredient.get("unit")), ((Double)(currentIngredient.get("amount"))).floatValue(),form));
		}

		JSONArray cuisines = (JSONArray)recipieData.get("cuisines");
		it = cuisines.iterator();
		while(it.hasNext()) {
			JSONObject currentCuisine = (JSONObject)it.next();
			randomRecipe.addCuisine(((Long)(currentCuisine.get("id"))).toString(), (String)currentCuisine.get("cuisine"));
		}
		addRecipeToDB(randomRecipe); // check if needs specialized catch clauses.
		return randomRecipe;
	}
} 
