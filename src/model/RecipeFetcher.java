package model;

import java.io.FileReader;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

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

	private RecipeFetcher() throws NoSuchAlgorithmException, IOException {
		this.parser = new JSONParser();
		getKey(); // get API key from config file.
	}

	private void getKey() throws IOException, NoSuchAlgorithmException {
		Properties p = new Properties();
		String path = System.getProperty("user.dir");
		FileReader reader = new FileReader(path + "/src/config.properties");
		p.load(reader);
		reader.close();
		this.APIKey = p.getProperty("APIKey");
	}

	public static RecipeFetcher getInstance() throws NoSuchAlgorithmException, IOException {
		if (_instance == null)
			_instance = new RecipeFetcher();
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
				.uri(URI.create("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=2"))
				.header("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
				.header("x-rapidapi-key", this.APIKey) 
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		this.response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		checkResponseCode(response.statusCode());

		JSONObject recipieData = (JSONObject)((JSONArray)(((JSONObject)this.parser.parse(this.response.body())).get("recipes"))).get(0);

		if(recipieData == null)
			throw new ConnectException("Invalid response from API");

		return createRecipe(recipieData);
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

		return createRecipe(recipieData);
	}

	public ArrayList<Recipe> searchRecipe(String query) throws Exception {
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

		JSONArray recipiesData = ((JSONArray)(((JSONObject)this.parser.parse(this.response.body())).get("results")));

		if(recipiesData == null)
			throw new ConnectException("Invalid response from API");

		if(recipiesData.isEmpty())
			throw new Exception("No recpies found.");

		@SuppressWarnings("unchecked") // spoonacular API returns JSON objects inside of the JSON array. 
		Iterator<JSONObject> it = recipiesData.iterator();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		while(it.hasNext())
			recipes.add(createRecipe(it.next()));
		return recipes;
	}
	
	public ArrayList<Recipe> searchRecipeByCuisine(String cuisine) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/searchComplex?limitLicense=true&offset=0&number=2&cuisine=" + cuisine))
				.header("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
				.header("x-rapidapi-key", this.APIKey)
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		this.response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		checkResponseCode(response.statusCode());

		JSONArray recipiesData = ((JSONArray)(((JSONObject)this.parser.parse(this.response.body())).get("results")));

		if(recipiesData == null)
			throw new ConnectException("Invalid response from API");

		if(recipiesData.isEmpty())
			throw new Exception("No recpies found.");

		@SuppressWarnings("unchecked") // spoonacular API returns JSON objects inside of the JSON array. 
		Iterator<JSONObject> it = recipiesData.iterator();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		while(it.hasNext()) {
			int id = ((Long)it.next().get("id")).intValue();
			Recipe r = searchRecipeByID(id);
			if(r != null)
				recipes.add(r);
		}
		return recipes;
	}
	
	public ArrayList<Recipe> searchRecipesByIngredients(Ingredient[] ingredientList) throws Exception {
		// prepare ingredients list for web request
		if(ingredientList.length == 0 || ingredientList == null)
			return null;
		StringBuffer ingredients = new StringBuffer();
		for (Ingredient i : ingredientList) {
			ingredients.append(i.getName().replaceAll(" ", "%20")+"%2C");
		}
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?ingredients=" + ingredients + "&number=2&ignorePantry=true&ranking=1"))
				.header("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
				.header("x-rapidapi-key", this.APIKey)
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		this.response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		checkResponseCode(response.statusCode());
		
		JSONArray recipesData = (JSONArray)this.parser.parse(this.response.body());
		
		if(recipesData == null)
			throw new ConnectException("Invalid response from API");

		if(recipesData.isEmpty())
			throw new Exception("No recpies found.");

		@SuppressWarnings("unchecked") // spoonacular API returns JSON objects inside of the JSON array. 
		Iterator<JSONObject> it = recipesData.iterator();
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		while(it.hasNext()) {
			int id = ((Long)it.next().get("id")).intValue();
			try {
				Recipe r = searchRecipeByID(id);
				if(r != null)
					recipes.add(r);
			} catch(IndexOutOfBoundsException e) {};
		}
		return recipes;
	}

	public Recipe searchRecipeByID(int id) throws Exception {
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/"+ id + "/information"))
				.header("x-rapidapi-host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
				.header("x-rapidapi-key", this.APIKey)
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		this.response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		checkResponseCode(response.statusCode());
		
		JSONObject recipeData = ((JSONObject)this.parser.parse(this.response.body()));
		
		if(recipeData == null)
			throw new ConnectException("Invalid response from API");

		if(recipeData.isEmpty())
			throw new Exception("No recpies found.");

		return createRecipe(recipeData);
	}

	private boolean addRecipeToDB(Recipe recipe) throws SQLException, NoSuchAlgorithmException {
		// Compares the recipe with the DB and (if necessary) adds it to the DB.
		DBManager manager = DBManager.getInstance();
		if(manager.requestRecipeByID(recipe.getRecipeID()) == null)
			try {
				return manager.addRecipe(recipe);
			} catch (SQLException e) {
				return false;
			}
		return true;
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

	private Recipe createRecipe(JSONObject recipieData) throws Exception{
		/*
		 * translates and returns received JSONObject from spoonacular API as recipe object.
		 */
		String id = ((Long)(recipieData.get("id"))).toString();
		String title = (String)recipieData.get("title");
		int readyInMinutes = ((Long)recipieData.get("readyInMinutes")).intValue();
		int servings = ((Long)recipieData.get("servings")).intValue();

		Recipe randomRecipe = new Recipe(id,title,readyInMinutes,servings);
		JSONArray instructionsData;
		try {
			instructionsData = (JSONArray)((JSONObject)((JSONArray)recipieData.get("analyzedInstructions")).get(0)).get("steps");
		} catch (Exception e) {
			return null;
		}
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
					((Long)(currentIngredient.get("id"))).toString(), ((Double)(currentIngredient.get("amount"))).floatValue(),
					(String)(currentIngredient.get("unit")), (String)(currentIngredient.get("name")),form));
		}

		JSONArray cuisines = (JSONArray)recipieData.get("cuisines");
		it = cuisines.iterator();
		while(it.hasNext()) {
			String currentCuisine = (String)it.next();
			String cuisineID = DBManager.getInstance().getCuisineID(currentCuisine);
			randomRecipe.addCuisine(cuisineID, currentCuisine);
		}
		boolean success = addRecipeToDB(randomRecipe);
		if (!success)
			return null;
		return randomRecipe;
	}
} 
