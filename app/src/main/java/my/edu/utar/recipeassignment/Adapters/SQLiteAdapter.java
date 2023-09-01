package my.edu.utar.recipeassignment.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import my.edu.utar.recipeassignment.Helpers.Recipe;

public class SQLiteAdapter {

    private static int DB_version = 1;
    private static String DATABASE_NAME = "RecipeDB";
    private static String TABLE_NAME = "favouriteTable";
    private static String KEY_ID="id";
    private static String RECIPE_TITLE = "recipeTitle";
    private static String RECIPE_IMAGE = "recipeImage";
    private static String FAVOURTIE_STATUS = "fStatus";
    private static String INGREDIENTS = "ingredients";
    private static String STEPS = "steps";

    private static String TABLE_FAV_ID_NAME = "favoriteRecipeIdTable";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " TEXT,"
            + RECIPE_TITLE + " TEXT,"
            + RECIPE_IMAGE + " TEXT," + FAVOURTIE_STATUS + " TEXT,"
            + INGREDIENTS + " TEXT," + STEPS + " TEXT)";

    private static String CREATE_TABLE_2 =  "CREATE TABLE " + TABLE_FAV_ID_NAME + "(" + KEY_ID + " TEXT)";


    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    //constructor
    public SQLiteAdapter(Context c){
        context = c;
    }

    public SQLiteAdapter(FavouritesAdapter favouritesAdapter) {
    }

    //open database to insert data/to write data
    public SQLiteAdapter openToWrite() throws android.database.SQLException{

        //create a table with MYDATABASE_NAME and the version of MYDATABASE_VERSION
        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME,
                null, DB_version);

        //open to write
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    //open the database to read
    public SQLiteAdapter openToRead() throws android.database.SQLException{

        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME, null, DB_version);

        //open to read
        sqLiteDatabase= sqLiteHelper.getReadableDatabase();

        return this;
    }

    //insert favourite into db
    public long insert_recipe(String recipe_title, String recipe_image, String id, List<String> ingredients, List<String> steps){

        System.out.println("Inserting recipe");


        ContentValues contentValues = new ContentValues();

        contentValues.put(RECIPE_TITLE, recipe_title);
        contentValues.put(RECIPE_IMAGE, recipe_image);
        contentValues.put(KEY_ID, id);
        System.out.println("\n" + recipe_title);

        contentValues.put(INGREDIENTS, new JSONArray(ingredients).toString());
        contentValues.put(STEPS, new JSONArray(steps).toString());


//        JSONArray ingredientsJsonArray = new JSONArray(ingredients);
//        String ingredientsJsonString = ingredientsJsonArray.toString();
//        contentValues.put(String.valueOf(INGREDIENTS), ingredientsJsonString);

        System.out.println("\n" + ingredients);
//
//        JSONArray stepsJsonArray = new JSONArray(steps);
//        String stepsJsonString = stepsJsonArray.toString();
//        contentValues.put(String.valueOf(STEPS), stepsJsonString);
        System.out.println("\n" + steps);


        System.out.println("Inserting finished");

        //update the table with these values
        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);

    }

    // insert favourited id into db
    public long insert_fav_recipe_id(String id){
        System.out.println("Inserting recipe fav id");


        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_ID, id);
        System.out.println("id inserted is:" + id);

        System.out.println("Inserting finished");

        return sqLiteDatabase.insert(TABLE_FAV_ID_NAME, null, contentValues);

    }

    public void remove_fav_recipe_id(String id)
    {
        System.out.println("Removing recipe fav id");

        // Define the WHERE clause to specify which row to delete
        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {id};

        // Delete the row from the database
        sqLiteDatabase.delete(TABLE_FAV_ID_NAME, whereClause, whereArgs);

        System.out.println("Removal finished");
    }

    public void remove_fav_recipe_from_id(String id){
        System.out.println("Removing recipe fav ");

        // Define the WHERE clause to specify which row to delete
        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {id};

        // Delete the row from the database
        sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);

        System.out.println("Removal finished");
    }


    //retrive data from table
    public String queueAll(){

        System.out.println("Entering print all function");

        String[] columns = new String[]{KEY_ID, RECIPE_TITLE, RECIPE_IMAGE, INGREDIENTS, STEPS};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null,
                null, null, null);

        StringBuilder resultBuilder = new StringBuilder();

        String result = "";
        int index_CONTENT = cursor.getColumnIndex(KEY_ID);
        int index_CONTENT_2 = cursor.getColumnIndex(RECIPE_TITLE);
        int index_CONTENT_3 = cursor.getColumnIndex(RECIPE_IMAGE);
        int index_CONTENT_4 = cursor.getColumnIndex(String.valueOf(INGREDIENTS));
        int index_CONTENT_5 = cursor.getColumnIndex(String.valueOf(STEPS));

        cursor.moveToFirst(); // Move the cursor to the first row

        while (!cursor.isAfterLast()) {
            String id = cursor.getString(index_CONTENT);
            String recipeTitle = cursor.getString(index_CONTENT_2);
            String recipeImage = cursor.getString(index_CONTENT_3);
            String ingredientsJSON = cursor.getString(index_CONTENT_4);
            String stepsJSON = cursor.getString(index_CONTENT_5);

            // Process JSON strings for ingredients and steps
            List<String> ingredients = new ArrayList<>();
            try {
                JSONArray ingredientsArray = new JSONArray(ingredientsJSON);
                for (int i = 0; i < ingredientsArray.length(); i++) {
                    ingredients.add(ingredientsArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<String> steps = new ArrayList<>();
            try {
                JSONArray stepsArray = new JSONArray(stepsJSON);
                for (int i = 0; i < stepsArray.length(); i++) {
                    steps.add(stepsArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            result = String.valueOf(resultBuilder.append(id).append(";")
                    .append(recipeTitle).append("; ")
                    .append(recipeImage).append("; ")
                    .append(ingredients.toString()).append("; ")
                    .append(steps.toString()).append("\n"));



            System.out.println("Entering print \n function"+id);
            System.out.println("Entering print all function \n"+recipeTitle);
            System.out.println("Entering print all function \n"+ingredients);
            System.out.println("Entering print all function \n"+steps);



            cursor.moveToNext(); // Move to the next row
        }

        cursor.close(); // Close the cursor when done

        System.out.println("Finished Printing");

        return result;
    }

    public List<String> findRecipeId(){
        String[] columns = new String[]{KEY_ID, RECIPE_TITLE, RECIPE_IMAGE, INGREDIENTS, STEPS};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null,
                null, null, null);

        String result = "";
        int index_CONTENT = cursor.getColumnIndex(KEY_ID);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT) + "\n";
        }

        return Collections.singletonList(result);

    }

    public List<String> findRecipeTitle(){
        String[] columns = new String[]{KEY_ID, RECIPE_TITLE, RECIPE_IMAGE, INGREDIENTS, STEPS};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null,
                null, null, null);

        String result = "";
        int index_CONTENT = cursor.getColumnIndex(RECIPE_TITLE);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT) + "\n";
        }

        return Collections.singletonList(result);


    }

    public String findRecipeIngredients(){
        String[] columns = new String[]{KEY_ID, RECIPE_TITLE, RECIPE_IMAGE, INGREDIENTS, STEPS};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null,
                null, null, null);

        StringBuilder resultBuilder = new StringBuilder();

        String result = "";
        int index_CONTENT = cursor.getColumnIndex(String.valueOf(INGREDIENTS));

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            String ingredientsJSON = cursor.getString(index_CONTENT);

            List<String> ingredients = new ArrayList<>();
            try {
                JSONArray ingredientsArray = new JSONArray(ingredientsJSON);
                for (int i = 0; i < ingredientsArray.length(); i++) {
                    ingredients.add(ingredientsArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            result = String.valueOf(resultBuilder
                    .append(ingredients.toString())
                   );

        }

        return result;


    }

    public String findRecipeSteps(){
        String[] columns = new String[]{KEY_ID, RECIPE_TITLE, RECIPE_IMAGE, INGREDIENTS, STEPS};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null,
                null, null, null);

        StringBuilder resultBuilder = new StringBuilder();

        String result = "";
        int index_CONTENT = cursor.getColumnIndex(String.valueOf(STEPS));

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            String stepsJSON = cursor.getString(index_CONTENT);

            List<String> steps = new ArrayList<>();
            try {
                JSONArray stepsArray = new JSONArray(stepsJSON);
                for (int i = 0; i < stepsArray.length(); i++) {
                    steps.add(stepsArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            result = String.valueOf(resultBuilder
                    .append(steps.toString())
            );

        }

        return result;

    }

    public List<String> findRecipeImage(){
        String[] columns = new String[]{KEY_ID, RECIPE_TITLE, RECIPE_IMAGE, INGREDIENTS, STEPS};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null,
                null, null, null);

        String result = "";
        int index_CONTENT = cursor.getColumnIndex(RECIPE_IMAGE);

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            result = result + cursor.getString(index_CONTENT) + "\n";
        }

        return Collections.singletonList(result);

    }

    // Add a new method to find the title using recipeId
    public String findRecipeTitleByRecipeId(String recipeId) {
        String[] columns = new String[]{KEY_ID, RECIPE_TITLE};
        String selection = KEY_ID + " = ?";
        String[] selectionArgs = {recipeId};

        System.out.println("\nrecipe_id is 2 " + recipeId);


        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, KEY_ID +"=?", new String[] {recipeId}, null, null, null);

        String title = "";

        int index_CONTENT = cursor.getColumnIndex(KEY_ID);
        int index_CONTENT_2 = cursor.getColumnIndex(RECIPE_TITLE);



        if (cursor.moveToFirst()) {
            title = cursor.getString(index_CONTENT_2);
        }

        cursor.close(); // Close the cursor when done

        System.out.println("\nrecipe_title is 2 " + title);


        return title;
    }

    public List<Recipe> getAllFavouriteRecipes() {

        List<Recipe> favouriteRecipes = new ArrayList<>();

        String[] columns = new String[]{KEY_ID, RECIPE_TITLE, RECIPE_IMAGE, INGREDIENTS, STEPS};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null, null, null, null);

        int index_CONTENT = cursor.getColumnIndex(KEY_ID);
        int index_CONTENT_2 = cursor.getColumnIndex(RECIPE_TITLE);
        int index_CONTENT_3 = cursor.getColumnIndex(RECIPE_IMAGE);
        int index_CONTENT_4 = cursor.getColumnIndex(INGREDIENTS);
        int index_CONTENT_5 = cursor.getColumnIndex(STEPS);

        cursor.moveToFirst(); // Move the cursor to the first row

        while (!cursor.isAfterLast()) {
            String id = cursor.getString(index_CONTENT);
            String recipeTitle = cursor.getString(index_CONTENT_2);
            String recipeImage = cursor.getString(index_CONTENT_3);
            String ingredientsJSON = cursor.getString(index_CONTENT_4);
            String stepsJSON = cursor.getString(index_CONTENT_5);

            // Process JSON strings for ingredients and steps
            List<String> ingredients = new ArrayList<>();
            try {
                JSONArray ingredientsArray = new JSONArray(ingredientsJSON);
                for (int i = 0; i < ingredientsArray.length(); i++) {
                    ingredients.add(ingredientsArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            List<String> steps = new ArrayList<>();
            try {
                JSONArray stepsArray = new JSONArray(stepsJSON);
                for (int i = 0; i < stepsArray.length(); i++) {
                    steps.add(stepsArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Create a Recipe object and add it to the list
            Recipe recipe = new Recipe(id, recipeTitle, recipeImage, ingredients, steps);
            favouriteRecipes.add(recipe);

            cursor.moveToNext(); // Move to the next row
        }

        cursor.close(); // Close the cursor when done

        return favouriteRecipes;
    }

    public boolean checkIsFavourite(String id){

        if (sqLiteDatabase == null) {
            // Handle the case where sqLiteDatabase is null, e.g., by returning false or handling the error
            return false;
        }else {

            System.out.println("\nChecking if recipe is a favorite");

            String[] columns = new String[]{KEY_ID};
            String selection = KEY_ID + " = ?";
            String[] selectionArgs = {id};

            Cursor cursor = sqLiteDatabase.query(TABLE_FAV_ID_NAME, columns, selection, selectionArgs,
                    null, null, null);

            boolean isFavourite = cursor.getCount() > 0; // Check if cursor has rows

            System.out.println("\nIs the recipe with ID " + id + " a favorite? " + isFavourite);

            return isFavourite;
        }
    }

    public List<String> getAllFavouriteRecipesId() {
        List<String> ids = new ArrayList<>(); // Create a list to store IDs

        String[] columns = new String[]{KEY_ID};
        Cursor cursor = sqLiteDatabase.query(TABLE_FAV_ID_NAME, columns, null, null, null, null, null);

        int index_CONTENT = cursor.getColumnIndex(KEY_ID);
        String result = "";

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            String id = cursor.getString(index_CONTENT);
            ids.add(id);
//            result = result + cursor.getString(index_CONTENT) + " ";
        }

        cursor.close(); // Close the cursor when you're done with it

        return ids; // Return the list of IDs

    }




    //close db
    public void close(){
        sqLiteHelper.close();
    }

    //delete all the content in the table / delete the table -->so that data will not b repeated
    public int deleteAll(){

        return sqLiteDatabase.delete(TABLE_NAME, null, null);
    }




    //super class of SQLiteOpenHelper --> implemennt both the override
    //methods which creates the database
    public class SQLiteHelper extends SQLiteOpenHelper {

        //constructor with 4 parameters
        public SQLiteHelper (@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //to create database
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
            sqLiteDatabase.execSQL(CREATE_TABLE_2);

        }

        //version control
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            //Q2
            //to upgrade the database version, to inform that I have a new version
            sqLiteDatabase.execSQL(CREATE_TABLE);
            sqLiteDatabase.execSQL(CREATE_TABLE_2);


        }
    }

}
