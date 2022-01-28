package model;

import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Properties;

public class DBManager {

	private static DBManager _instance = null;

	private String url;
	private String appUser, appPassword; // stored encrypted
	private Connection connection;


	private DBManager() throws SQLException {
		// get database data from config file
		FileReader reader;
		Properties p = new Properties();
		String path = System.getProperty("user.dir");
		try {
			reader = new FileReader(path + "/src/config.properties");
			p.load(reader); 
			reader.close();
		} catch (IOException e) {
			throw new SQLException("Cannot connect to database: Missing properties file");
		}
		this.url = p.getProperty("DBAdress");
		this.appUser = p.getProperty("username");
		this.appPassword = p.getProperty("password");
		this.connection = null;
	}

	public static DBManager getInstance() throws SQLException {
		if (_instance == null)
			_instance = new DBManager();
		return _instance;
	}

	private void connectToDB() throws SQLException {
		if(this.connection != null)
			if(!this.connection.isClosed())
				return;
		try {
			PasswordManager manager = PasswordManager.getInstance();
			Class.forName("com.mysql.cj.jdbc.Driver");
			this.connection = DriverManager.getConnection(url,manager.decrypt(appUser),manager.decrypt(appPassword));
		} catch (IOException e) {
			throw new SQLException("Cannot connect to database: Missing properties file");
		} catch (ClassNotFoundException e) {
			throw new SQLException("Cannot connect to database: Missing Driver");
		} catch (NoSuchAlgorithmException e) {
			throw new SQLException("Cannot connect to database: " + e.getMessage());
		}
	}

	private ResultSet executeQuery(String query) throws SQLException {
		connectToDB();
		PreparedStatement statement = connection.prepareStatement(query);
		ResultSet rs = statement.executeQuery(query);
		return rs;
	}

	private int executeUpdate(String query) throws SQLException {
		if(!this.connection.isValid(0))
			connectToDB();
		PreparedStatement statement = connection.prepareStatement(query);
		int affectedRows = statement.executeUpdate(query);
		return affectedRows;
	}

	private void disconnectFromDB(ResultSet rs) throws SQLException {
		if(rs != null)
			rs.close();
		this.connection.close();
	}

	public boolean searchUser(String userName) throws SQLException {
		String query = "SELECT * FROM accounttable WHERE userName = '" + userName + "'";
		ResultSet rs = executeQuery(query);
		boolean success = rs.next();
		disconnectFromDB(rs);	
		return success;
	}

	public User getUser(String userName) throws SQLException {
		String query = "SELECT * FROM accounttable WHERE userName = '" + userName + "'";
		ResultSet rs = executeQuery(query);
		if(!rs.next()) {
			disconnectFromDB(rs);
			return null;
		}
		User user = new User(rs.getInt("userID"), rs.getString("userName"), rs.getString("password"));
		disconnectFromDB(rs);
		return user;
	}

	public boolean addUser(User user) throws SQLException {
		String query = "INSERT INTO accounttable (userID, userName, password)"
				+		" VALUES(" + user.getUserID() + ",'" + user.getUserName() + "','" + user.getPassword() + "');";
		int effectedRows = executeUpdate(query);
		disconnectFromDB(null);	
		return effectedRows > 0;
	}

	public boolean addRecipe(Recipe recipe) throws SQLException {
		// change connection type to transaction
		connectToDB();
		boolean success = false;
		try {
			success =  saveRecipeToDB(recipe);
			if(!success)
				return false;
			success = success 
					&& saveInstructionsToDB(recipe.getRecipeID(), recipe.getInstructions()) 
					&& saveIngredientsToDB(recipe.getRecipeID(), recipe.getIngrediants()) 
					&& saveCuisineToDB(recipe.getRecipeID(), recipe.getCuisine());
		} catch (SQLException e) {
			// roll-back and change connection type to normal.
			removeRecipeFromDB(recipe.getRecipeID());
			disconnectFromDB(null);
			return false;
		}
		disconnectFromDB(null);
		return success;
	}

	private void removeRecipeFromDB(String recipeID) throws SQLException {
		String query = "DELETE FROM recipe_instructions WHERE recipeID = " + recipeID + ";";
		executeUpdate(query);
		query = "DELETE FROM recipe_ingredients WHERE recipeID = " + recipeID + ";";
		executeUpdate(query);
		query = "DELETE FROM recipe WHERE recipeID = "+ recipeID + ";";
		executeUpdate(query);
	}

	public Recipe requestRecipeByID(String recipeID) throws SQLException {
		String query = "Select * FROM recipe WHERE recipeID = " + recipeID + ";";
		ResultSet rs = executeQuery(query);
		if(!rs.next()) {
			disconnectFromDB(rs);
			return null;
		}
		// recipe exists -> create and return recipe object
		Recipe recipe = new Recipe();
		recipe.setRecipeID(((Integer)(rs.getInt("recipeID"))).toString());
		recipe.setRecipieName(rs.getString("recipeName"));
		recipe.setCookTime(rs.getInt("cookTime"));
		recipe.setServings(rs.getInt("servings"));
		// get ingredients list and add to recipe
		addIngredientsListToRecipe(recipe);
		// get instructions list and add to recipe
		addInstructionsListToRecipe(recipe);
		// get cuisine data and add to recipe
		addCuisineToRecipe(recipe);
		return recipe;
	}

	public ArrayList<Recipe> searchRecipeByName(String recipeName) throws SQLException {
		String query = "Select recipeID FROM recipe WHERE recipeName = \"" + recipeName + "\";";
		ResultSet rs = executeQuery(query);
		if(!rs.next()) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<Recipe> foundRecipes = new ArrayList<Recipe>();
		Recipe r =  requestRecipeByID(rs.getString("recipeID"));
		do {
			if(r != null)
				foundRecipes.add(r);
			r = requestRecipeByID(rs.getString("recipeID"));
		} while(rs.next());
		disconnectFromDB(rs);
		return foundRecipes;
	}

	public ArrayList<Recipe> searchRecipeByCuisine(String cuisine) throws SQLException { // TO BE CHECKED
		String query = "Select recipeID FROM recipe_cuisine "
				+ "WHERE cuisineID IN (SELECT cuisineID FROM cuisine WHERE cuisineName = \"" + cuisine + "\");";
		ResultSet rs = executeQuery(query);
		if(!rs.next()) {
			return null;
		}
		ArrayList<Recipe> recipies =  new ArrayList<Recipe>();
		while(rs.next()) {
			recipies.add(requestRecipeByID(rs.getString("recipeID")));
		}
		disconnectFromDB(rs);
		return recipies;
	}

	public String getCuisineID(String currentCuisine) throws SQLException {
		String id;
		String query = "Select cuisineID FROM cuisine WHERE cuisineName = \"" + currentCuisine + "\";";
		ResultSet rs = executeQuery(query);
		if(!rs.next())
			id = addCuisineToDB(currentCuisine);
		else
			id = Integer.toString(rs.getInt("cuisineID"));
		disconnectFromDB(rs);
		return id;
	}

	public ArrayList<Recipe> searchRecipeByIngredients(Ingredient[] userIngredients) throws SQLException {
		StringBuffer allIngredients = new StringBuffer();
		for (int i = 0; i < userIngredients.length; i++) {
			allIngredients.append(userIngredients[i].IngredientID );
			if(i< (userIngredients.length - 1)) {
				allIngredients.append(" OR ingredientID = ");
			}
		}
		String query = "Select recipeID FROM recipe_ingredients WHERE ingredientID = " + allIngredients.toString() + ";";
		ResultSet rs = executeQuery(query);
		if(!rs.next()) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<Recipe> recipies =  new ArrayList<Recipe>();
		while(rs.next()) {
			recipies.add(requestRecipeByID(rs.getString("recipeID")));
		}
		disconnectFromDB(rs);
		return recipies;
	}

	public ArrayList<Recipe> showAllRecipies() throws SQLException { 
		String query = "Select recipeID FROM recipe;";
		ResultSet rs = executeQuery(query);
		if(!rs.next()) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<Recipe> recipies =  new ArrayList<Recipe>();
		do {
			recipies.add(requestRecipeByID(Integer.toString(rs.getInt("recipeID"))));
		} while(rs.next());
		disconnectFromDB(rs);
		return recipies;
	}

	public Ingredient searchIngredient(String name) throws SQLException {
		String query = "Select ingredientID FROM ingredient WHERE ingredientName = \"" + name + "\";";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return null;
		}
		Ingredient i =  new Ingredient(rs.getInt("ingredientID")+"" ,0,null,name,null);
		disconnectFromDB(rs);
		return i;
	}

	public Ingredient addIngredient(String id,String name) throws SQLException {
		String query = "INSERT INTO ingredient(ingredientID, ingredientName) VALUES (" + id +",\""+ name + "\");";
		int effectedRows = executeUpdate(query);	
		if (effectedRows > 0)
			return searchIngredient(name);
		disconnectFromDB(null);
		return null;
	}

	public ArrayList<Recipe> getUserFavorites(int userID) throws SQLException { 
		String query = "Select recipeID FROM user_favorites WHERE userID = " + userID + ";";
		ResultSet rs = executeQuery(query);
		if(!rs.next()) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<Recipe> favoriteRecipiesOfUser =  new ArrayList<Recipe>();
		do {
			favoriteRecipiesOfUser.add(requestRecipeByID(rs.getString("recipeID")));
		} while(rs.next()); 
		disconnectFromDB(rs);
		return favoriteRecipiesOfUser;
	}

	public boolean addRecipeToUserFavorites(String userID , String recipeID ) throws SQLException {  
		String query = "INSERT INTO user_favorites (userID,recipeID) VALUES ("+userID +"," +recipeID+");" ;
		int effectedRows = executeUpdate(query);
		disconnectFromDB(null);	
		return effectedRows > 0;
	}

	public boolean removeRecipeFromUserFavorites(String userID , String recipeID ) throws SQLException {  
		String query = "DELETE From user_favorites Where userID = \""+userID +"\" AND recipeID = \"" +recipeID+"\" ;" ;
		int effectedRows = executeUpdate(query);
		disconnectFromDB(null);	
		return effectedRows > 0;
	}

	public boolean checkIfRecipeIsFavorite(String userID , String recipeID ) throws SQLException {  
		String query = "Select * FROM user_favorites Where userID = \""+userID +"\" AND recipeID = \"" +recipeID+"\";" ;
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return false;
		}
		disconnectFromDB(rs);
		return true;
	}

	public ArrayList<String> getMeasurements() throws SQLException {
		String query = "Select measurementName FROM standard_measurement;";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<String> measurements =  new ArrayList<String>();
		while(rs.next()) 
			measurements.add(rs.getString("measurementName"));
		disconnectFromDB(rs);
		return measurements;
	}

	public ArrayList<Ingredient> getIngredients() throws SQLException {
		String query = "Select ingredientID, ingredientName FROM ingredient;";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<Ingredient> ingredients =  new ArrayList<Ingredient>();
		while(rs.next()) 
			ingredients.add(new Ingredient(Integer.toString(rs.getInt("ingredientID")), 0, "", rs.getString("ingredientName"), ""));
		disconnectFromDB(rs);
		return ingredients;
	}

	private void addCuisineToRecipe(Recipe recipe) throws SQLException { // TO BE CHECKED
		String query = "Select cuisineID, cuisineName FROM cuisine NATURAL JOIN recipe_cuisine WHERE recipeID = " + recipe.getRecipeID() + ";";
		ResultSet rs = executeQuery(query);
		if(!rs.next())
			return; // recipe might not have cuisine
		recipe.addCuisine(((Integer)(rs.getInt("cuisineID"))).toString(), rs.getString("cuisineName"));
		while(rs.next())
			recipe.addCuisine(((Integer)(rs.getInt("cuisineID"))).toString(), rs.getString("cuisineName"));
	}

	private void addInstructionsListToRecipe(Recipe recipe) throws SQLException {
		String query = "Select stepNumber, stepInfo FROM recipe_instructions WHERE recipeID = " + recipe.getRecipeID() + ";";
		ResultSet rs = executeQuery(query);
		if(!rs.next()) {
			disconnectFromDB(rs);
			throw new SQLException("Cannot retrive instructions list to requested recipe");
		}
		recipe.addInstruction(rs.getInt("stepNumber"), rs.getString("stepInfo"));
		while(rs.next())
			recipe.addInstruction(rs.getInt("stepNumber"), rs.getString("stepInfo"));
	}

	private void addIngredientsListToRecipe(Recipe recipe) throws SQLException {
		String query = "SELECT ingredientID, ingredientName, measurementName, amount, form FROM ingredient "
				+ "NATURAL JOIN recipe_ingredients NATURAL JOIN standard_measurement WHERE recipeID = " + recipe.getRecipeID() + ";";
		ResultSet rs = executeQuery(query);
		if(!rs.next()) {
			disconnectFromDB(rs);
			throw new SQLException("Cannot retrive ingredients list to requested recipe");
		}
		recipe.addIngrediant(new Ingredient(((Integer)(rs.getInt("ingredientID"))).toString(), rs.getFloat("amount"), 
				rs.getString("measurementName"), rs.getString("ingredientName"), rs.getString("form")));
		while(rs.next())
			recipe.addIngrediant(new Ingredient(((Integer)(rs.getInt("ingredientID"))).toString(), rs.getFloat("amount"), 
					rs.getString("measurementName"), rs.getString("ingredientName"), rs.getString("form")));
	}

	private boolean saveRecipeToDB(Recipe recipe) throws SQLException {
		String query = "INSERT INTO recipe (recipeID, recipeName, cookTime, servings)" 
				+	" VALUES (" + recipe.getRecipeID() +",\"" + recipe.getRecipieName() 
				+ 	"\"," + recipe.getCookTime() + "," + recipe.getServings() + ");";

		int affectedRows;
		if (!recipe.getRecipeID().equals("null"))
			affectedRows = executeUpdate(query);
		else {
			PreparedStatement statement = this.connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			affectedRows = statement.executeUpdate();
			ResultSet rs = statement.getGeneratedKeys();
			if(affectedRows > 0 && rs.next()) {
				String id = Integer.toString(rs.getInt(1));
				recipe.setRecipeID(id);
			}
		}
		return affectedRows > 0;
	}

	private boolean saveInstructionsToDB(String recipeID, HashMap<Integer, String> instructions) throws SQLException {
		StringBuffer query = new StringBuffer();
		query.append("INSERT INTO recipe_instructions (recipeID, stepNumber, stepInfo) VALUES ");
		for (Entry<Integer, String> entry : instructions.entrySet()) {
			int stepNumber = entry.getKey();
			String instruction = entry.getValue();
			instruction.replaceAll("\"", "");
			query.append("(" + recipeID +"," + stepNumber + ",\"" + instruction + "\"), ");
		}
		// replace last ", " to ";"
		query.setCharAt(query.length() - 2, ';');
		int effectedRows = executeUpdate(query.toString());
		return effectedRows > 0;
	}

	private boolean saveIngredientsToDB(String recipeID, ArrayList<Ingredient> ingredients) throws SQLException { 
		StringBuffer query = new StringBuffer();
		query.append("INSERT INTO recipe_ingredients (recipeID, ingredientID, measurementID, amount, form) VALUES ");
		for (Ingredient i : ingredients) {
			String ingredientID = i.getIngrediantID();
			if(searchIngredient(i.getName()) == null)
				addIngredient(ingredientID, i.getName());
			int measurementID = requestMeasurementID(i.measurement);
			if(measurementID < 0)
				return false;
			float amount = i.getAmount();
			String form = i.getFrom();
			query.append("(" + recipeID +"," + ingredientID + "," + measurementID + "," + amount + ",'" + form + "'), ");
		}
		// replace last ", " to ";"
		query.setCharAt(query.length() - 2, ';');
		int effectedRows = executeUpdate(query.toString());
		return effectedRows > 0;
	}

	private boolean saveCuisineToDB(String recipeID, HashMap<String, String> cuisine) throws SQLException { // TO BE CHECKED
		if(cuisine.isEmpty())
			return true;
		StringBuffer query = new StringBuffer();
		query.append("INSERT INTO recipe_cuisine (recipeID, cuisineID) VALUES ");
		for (Entry<String, String> entry : cuisine.entrySet()) {
			String cuisineID = entry.getKey();
			query.append("(" + recipeID +"," + cuisineID + "), ");
		}
		// replace last ", " to ";"
		query.setCharAt(query.length() - 2, ';');
		int effectedRows = executeUpdate(query.toString());
		return effectedRows > 0;
	}

	private int requestMeasurementID(String measurement) throws SQLException {
		String query = "SELECT measurementID FROM standard_measurement WHERE measurementName = '" + measurement + "';";
		ResultSet rs = executeQuery(query);
		if(rs.next()) {
			int measurementID = rs.getInt("measurementID");
			return measurementID;
		} else
			if(!addStandardMeasurement(measurement))
				return -1;
			return requestMeasurementID(measurement);
		}

	private boolean addStandardMeasurement(String measurement) throws SQLException { // TO BE CHECKED
		String query = "INSERT INTO standard_measurement(measurementID, measurementName) values(null,\"" + measurement + "\");";
		int effectedRows = executeUpdate(query);
		return effectedRows > 0;
	}
	
	private String addCuisineToDB(String currentCuisine) throws SQLException {
		String query = "INSERT INTO cuisine (cuisineID,cuisineName) VALUES (null,\"" + currentCuisine + "\");";
		int effectedRows = executeUpdate(query);
		if(effectedRows > 0)
			return getCuisineID(currentCuisine);
		else { 
			disconnectFromDB(null);
			throw new SQLException("Could not add new cuisine type.");
		}
	}
}
