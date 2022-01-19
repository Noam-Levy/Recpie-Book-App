package model;

import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	}

	public static DBManager getInstance() throws SQLException {
		if (_instance == null)
			_instance = new DBManager();
		return _instance;
	}

	private void connectToDB() throws SQLException {
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

	public boolean searchUser(String userName) throws SQLException { // TO BE CHECKED
		String query = "SELECT * FROM accounttable WHERE userName = '" + userName + "'";
		ResultSet rs = executeQuery(query);
		boolean success = rs.next();
		disconnectFromDB(rs);	
		return success;
	}

	public boolean addUser(User user) throws SQLException {
		String query = "INSERT INTO accounttable (userID, userName, password)"
				+		" VALUES(" + user.getUserID() + ",'" + user.getUserName() + "','" + user.getPassword() + "');";
		int effectedRows = executeUpdate(query);
		disconnectFromDB(null);	
		return effectedRows > 0;
	}

	public boolean addRecipe(Recipe recipe) throws SQLException {
		String id = recipe.getRecipeID();
		// transaction - to do
		boolean success =  saveRecipeToDB(recipe) && saveInstructionsToDB(id, recipe.getInstructions()) 
				&& saveIngredientsToDB(id, recipe.getIngrediants()) && saveCuisineToDB(id, recipe.getCuisine());

		return success;
	}

	public ArrayList<Recipe> getUserFavorites(int userID) throws SQLException {
		
		String query = "Select recipeID FROM user_favorites WHERE userID = " + userID + ";";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<Recipe> favoriteRecipiesOfUser =  new ArrayList<Recipe>();
		while(rs.next()) {
			favoriteRecipiesOfUser.add(requestRecipeByID(rs.getString("recipeID")));	// maybe disconnect because requestRecipeByID
		}
		
		disconnectFromDB(rs);
		return favoriteRecipiesOfUser;
	}
	

	public Recipe requestRecipeByID(String recipeID) throws SQLException { // TO BE CHECKED
		String query = "Select * FROM recipe WHERE recipeID = " + recipeID + ";";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
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
		disconnectFromDB(rs);
		return recipe;
	}

	private void addCuisineToRecipe(Recipe recipe) throws SQLException { // TO BE CHECKED
		String query = "Select cuisineID, cuisineName FROM cuisine NATURAL JOIN recipe_cuisine";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return; // recipe might not have cuisine
		}
		while(rs.next())
			recipe.addCuisine(((Integer)(rs.getInt("cuisineID"))).toString(), rs.getString("cuisineName"));
		disconnectFromDB(rs);
	}

	private void addInstructionsListToRecipe(Recipe recipe) throws SQLException { // TO BE CHECKED
		String query = "Select stepNumber, stepInfo FROM recipe_instructions WHERE recipeID = " + recipe.getRecipeID() + ";";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			throw new SQLException("Cannot retrive instructions list to requested recipe");
		}	
		while(rs.next())
			recipe.addInstruction(rs.getInt("stepNumber"), rs.getString("stepInfo"));
		disconnectFromDB(rs);
	}

	private void addIngredientsListToRecipe(Recipe recipe) throws SQLException { // TO BE CHECKED!
		String query = "SELECT ingredientID, ingredientName, measurementName, amount, form FROM ingredient"
				+ "NATURAL JOIN recipe_ingredients NATURAL JOIN standard_measurement;";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			throw new SQLException("Cannot retrive ingredients list to requested recipe");
		}
		while(rs.next())
			recipe.addIngrediant(new Ingredient(((Integer)(rs.getInt("ingredientID"))).toString(), rs.getString("ingredientName"), 
					rs.getString("measurementName"), rs.getFloat("amount"), rs.getString("form")));
		disconnectFromDB(rs);

	}

	private boolean saveRecipeToDB(Recipe recipe) throws SQLException { // TO BE CHECKED
		String query = "INSERT INTO recipe (recipeID, recipeName, cookTime, servings)" 
				+	" VALUES (" + recipe.getRecipeID() +",'" + recipe.getRecipieName() 
				+ 	"'," + recipe.getCookTime() + "," + recipe.getServings() + ");";
		int effectedRows = executeUpdate(query);
		disconnectFromDB(null);	
		return effectedRows > 0;
	}

	private boolean saveInstructionsToDB(String recipeID, HashMap<Integer, String> instructions) throws SQLException { // TO BE CHECKED
		String query = "INSERT INTO recipe_instructions (recipeID, stepNumber, stepInfo) VALUES ";
		for (Entry<Integer, String> entry : instructions.entrySet()) {
			int stepNumber = entry.getKey();
			String instruction = entry.getValue();
			query.concat("(" + recipeID +"," + stepNumber + ",'" + instruction + "'), ");
		}
		// replace last ", " to ";"
		query = query.substring(0, query.lastIndexOf(','));
		query.concat(";");
		int effectedRows = executeUpdate(query);
		disconnectFromDB(null);	
		return effectedRows > 0;
	}

	private boolean saveIngredientsToDB(String recipeID, ArrayList<Ingredient> ingredients) throws SQLException { // TO BE CHECKED
		String query = "INSERT INTO recipe_ingredients (recipeID, ingredientID, measurementID, amount, form) VALUES ";
		for (Ingredient i : ingredients) {
			int measurementID = requestMeasurementID(i.measurement);
			String ingredientID = i.getIngrediantID();
			float amount = i.getAmount();
			String form = i.getFrom();
			query.concat("(" + recipeID +"," + ingredientID + "," + measurementID + "," + amount + ",'" + form + "'), ");
		}
		// replace last ", " to ";"
		query = query.substring(0, query.lastIndexOf(','));
		query.concat(";");
		int effectedRows = executeUpdate(query);
		disconnectFromDB(null);	
		return effectedRows > 0;
	}

	private int requestMeasurementID(String measurement) throws SQLException { // TO BE CHECKED
		String query = "SELECT measurementID FROM standard_measurement WHERE measurementName = '" + measurement + "';";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			throw new SQLException("Error retrieving data from database");
		}
		int measurementID = rs.getInt("measurementID");
		disconnectFromDB(rs);
		return measurementID;
	}

	private boolean saveCuisineToDB(String recipeID, HashMap<String, String> cuisine) throws SQLException { // TO BE CHECKED
		String query = "INSERT INTO recipe_cuisine (recipeID, cuisineID) VALUES ";
		for (Entry<String, String> entry : cuisine.entrySet()) {
			String cuisineID = entry.getKey();
			query.concat("(" + recipeID +"," + cuisineID + "), ");
		}
		// replace last ", " to ";"
		query = query.substring(0, query.lastIndexOf(','));
		query.concat(";");
		int effectedRows = executeUpdate(query);
		disconnectFromDB(null);	
		return effectedRows > 0;
	}

	public Recipe searchByName(String recipeName) throws SQLException {
		
		String query = "Select recipeID FROM recipe WHERE recipeName = " + recipeName + ";";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return null;
		}
		
		Recipe r =  requestRecipeByID(rs.getString("recipeID"));	// maybe disconnect because requestRecipeByID
		
		disconnectFromDB(rs);
		return r;
	}
	
	public ArrayList<Recipe> searchByCuisine(String cuisine) throws SQLException {
		
		String query = "Select recipeID FROM (SELECT cuisineID FROM cuisine WHERE cuisineName = " + cuisine + ") JOIN recipe_cuisine;";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<Recipe> recipies =  new ArrayList<Recipe>();
		while(rs.next()) {
			recipies.add(requestRecipeByID(rs.getString("recipeID")));	// maybe disconnect because requestRecipeByID
		}
		
		disconnectFromDB(rs);
		return recipies;
	
	}
	
	
	public ArrayList<Recipe> searchByIngredients(Ingredient[] userIngredients) throws SQLException {
		
		StringBuffer allIngredients = new StringBuffer();
		for (int i = 0; i < userIngredients.length; i++) {
			allIngredients.append(userIngredients[i].IngredientID );
			if(i< (userIngredients.length - 1)) {
				allIngredients.append(" OR ingredientID = ");
			}
		}
		
		
		String query = "Select recipeID FROM recipe_ingredients WHERE ingredientID = " + allIngredients.toString() + ";";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<Recipe> recipies =  new ArrayList<Recipe>();
		while(rs.next()) {
			recipies.add(requestRecipeByID(rs.getString("recipeID")));	// maybe disconnect because requestRecipeByID
		}
		
		disconnectFromDB(rs);
		return recipies;
	
	}
	
	
	public ArrayList<Recipe> showAllRecipies() throws SQLException {
		
		String query = "Select recipeID FROM recipe ;";
		ResultSet rs = executeQuery(query);
		if(!(rs.next())) {
			disconnectFromDB(rs);
			return null;
		}
		ArrayList<Recipe> recipies =  new ArrayList<Recipe>();
		while(rs.next()) {
			recipies.add(requestRecipeByID(rs.getString("recipeID")));	// maybe disconnect because requestRecipeByID
		}
		
		disconnectFromDB(rs);
		return recipies;
	
	}
	
	public boolean addToUserFavorites(String userID , String recipeID ) throws SQLException { 
		String query = "INSERT INTO user_favorites (userID,recipeID) VALUES ("+userID +"," +recipeID+");" ;
		
		int effectedRows = executeUpdate(query);
		disconnectFromDB(null);	
		return effectedRows > 0;
	}


}
