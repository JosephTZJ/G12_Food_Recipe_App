package com.example.sourcecode.Adapters;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.sourcecode.Helpers.Recipe;

public class SQLiteAdapter {

    private static int DB_version = 1;
    private static String DATABASE_NAME = "RecipeDB";
    private static String KEY_ID="id";
    private static String RECIPE_TITLE = "recipeTitle";
    private static String RECIPE_IMAGE = "recipeImage";
    private static String FAVOURTIE_STATUS = "fStatus";
    private static String INGREDIENTS = "ingredients";
    private static String STEPS = "steps";

    private static String TABLE_NAME = "favouriteTable";
    private static String TABLE_FAV_ID_NAME = "favoriteRecipeIdTable";
    private static String TABLE_SHOP_LIST = "shoplist";

    // TABLE for downloaded recipe - storing all recipe details
    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " TEXT,"
            + RECIPE_TITLE + " TEXT,"
            + RECIPE_IMAGE + " TEXT," + FAVOURTIE_STATUS + " TEXT,"
            + INGREDIENTS + " TEXT," + STEPS + " TEXT)";

    // TABLE for favourited recipe - storing id of favorite recipe
    private static String CREATE_TABLE_2 =  "CREATE TABLE " + TABLE_FAV_ID_NAME + "(" + KEY_ID + " TEXT)";

    // TABLE for ingredients shopping list - storing ingredients of the recipe
    private static String CREATE_TABLE_3 = "CREATE TABLE " + TABLE_SHOP_LIST + "(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RECIPE_TITLE + " TEXT," +
                    INGREDIENTS + " TEXT)";

    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    //constructor
    public SQLiteAdapter(Context c){
        context = c;
    }

    //open database to insert data/to write data
    public SQLiteAdapter openToWrite() throws android.database.SQLException{

        sqLiteHelper = new SQLiteHelper(context, DATABASE_NAME,
                null, DB_version);

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

    //insert downloads into db
    public long insert_recipe(String recipe_title, String recipe_image, String id, List<String> ingredients, List<String> steps){

        ContentValues contentValues = new ContentValues();

        contentValues.put(RECIPE_TITLE, recipe_title);
        contentValues.put(RECIPE_IMAGE, recipe_image);
        contentValues.put(KEY_ID, id);

        contentValues.put(INGREDIENTS, new JSONArray(ingredients).toString());
        contentValues.put(STEPS, new JSONArray(steps).toString());

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
    }

    // insert favourited id into db
    public long insert_fav_recipe_id(String id){
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_ID, id);

        return sqLiteDatabase.insert(TABLE_FAV_ID_NAME, null, contentValues);
    }

    public void remove_fav_recipe_id(String id)
    {
        // Define the WHERE clause to specify which row to delete
        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {id};

        // Delete the row from the database
        sqLiteDatabase.delete(TABLE_FAV_ID_NAME, whereClause, whereArgs);

        System.out.println("Removal finished");
    }


    // remove favourite recipe details from download
    public void remove_fav_recipe_from_id(String id){
        // Define the WHERE clause to specify which row to delete
        String whereClause = KEY_ID + " = ?";
        String[] whereArgs = {id};

        // Delete the row from the database
        sqLiteDatabase.delete(TABLE_NAME, whereClause, whereArgs);

        System.out.println("Removal finished");
    }

    public long insert_ingredient(String recipe_title, List<String> ingredients) {

        JSONArray ingredientsJsonArray = new JSONArray(ingredients);
        String ingredient = ingredientsJsonArray.toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(RECIPE_TITLE, recipe_title);
        contentValues.put(INGREDIENTS, ingredient);

        System.out.println("Inserting finished");

        return sqLiteDatabase.insert(TABLE_SHOP_LIST, null, contentValues);
    }

    // get the ingredients of recipe from database
    public Map<String, List<String>> getShoppingListData() {
        Map<String, List<String>> shoppingListData = new HashMap<>();
        String[] columns = new String[]{RECIPE_TITLE, INGREDIENTS};
        Cursor cursor = sqLiteDatabase.query(TABLE_SHOP_LIST, columns, null, null, null, null, null);

        int indexTitle = cursor.getColumnIndex(RECIPE_TITLE);
        int indexIngredients = cursor.getColumnIndex(INGREDIENTS);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String title = cursor.getString(indexTitle);
            String ingredientsJSON = cursor.getString(indexIngredients);

            List<String> ingredients = new ArrayList<>();
            try {
                JSONArray ingredientsArray = new JSONArray(ingredientsJSON);
                for (int i = 0; i < ingredientsArray.length(); i++) {
                    ingredients.add(ingredientsArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            shoppingListData.put(title, ingredients);

            cursor.moveToNext();
        }

        cursor.close();

        return shoppingListData;
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

    // Find title using recipeId
    public Recipe findRecipeByRecipeId(String recipeId) {
        String[] columns = new String[]{KEY_ID, RECIPE_TITLE, RECIPE_IMAGE, INGREDIENTS, STEPS};
        String selection = KEY_ID + " = ?";
        String[] selectionArgs = {recipeId};

        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        Recipe recipe = null;

        if (cursor.moveToFirst()) {
            int indexId = cursor.getColumnIndex(KEY_ID);
            int indexTitle = cursor.getColumnIndex(RECIPE_TITLE);
            int indexIngredients = cursor.getColumnIndex(INGREDIENTS);
            int indexSteps = cursor.getColumnIndex(STEPS);
            int imageURL = cursor.getColumnIndex(RECIPE_IMAGE);


            String id = cursor.getString(indexId);
            String title = cursor.getString(indexTitle);
            String ingredientsJson = cursor.getString(indexIngredients); // Retrieve as JSON
            String stepsJson = cursor.getString(indexSteps);
            String image = cursor.getString(imageURL);

            List<String> ingredients = parseJsonArray(ingredientsJson);
            List<String> steps = parseJsonArray(stepsJson);

            recipe = new Recipe(id, title, image, ingredients, steps);
        }

        cursor.close();


        return recipe;
    }



    // get the downloaded recipe details from database
    public List<Recipe> getAllDownloadRecipes() {

        List<Recipe> favouriteRecipes = new ArrayList<>();

        String[] columns = new String[]{KEY_ID, RECIPE_TITLE, RECIPE_IMAGE, INGREDIENTS, STEPS};
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME, columns, null, null, null, null, null);

        int index_CONTENT = cursor.getColumnIndex(KEY_ID);
        int index_CONTENT_2 = cursor.getColumnIndex(RECIPE_TITLE);
        int index_CONTENT_3 = cursor.getColumnIndex(RECIPE_IMAGE);
        int index_CONTENT_4 = cursor.getColumnIndex(INGREDIENTS);
        int index_CONTENT_5 = cursor.getColumnIndex(STEPS);

        cursor.moveToFirst();

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

            Recipe recipe = new Recipe(id, recipeTitle, recipeImage, ingredients, steps);
            favouriteRecipes.add(recipe);

            cursor.moveToNext();
        }

        cursor.close();

        return favouriteRecipes;
    }

    public boolean checkIsFavourite(String id){

        if (sqLiteDatabase == null) {
            return false;
        }else {

            String[] columns = new String[]{KEY_ID};
            String selection = KEY_ID + " = ?";
            String[] selectionArgs = {id};

            Cursor cursor = sqLiteDatabase.query(TABLE_FAV_ID_NAME, columns, selection, selectionArgs,
                    null, null, null);

            boolean isFavourite = cursor.getCount() > 0; // Check if cursor has rows

            return isFavourite;
        }
    }

    // Get all favourite recipe id from database
    public List<String> getAllFavouriteRecipesId() {
        List<String> ids = new ArrayList<>(); // Create a list to store IDs

        String[] columns = new String[]{KEY_ID};
        Cursor cursor = sqLiteDatabase.query(TABLE_FAV_ID_NAME, columns, null, null, null, null, null);

        int index_CONTENT = cursor.getColumnIndex(KEY_ID);
        //String result = "";

        for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext())
        {
            String id = cursor.getString(index_CONTENT);
            ids.add(id);
        }

        cursor.close();

        return ids;

    }


    //close db
    public void close(){
        sqLiteHelper.close();
    }

    //delete all the content in the table / delete the table
    public int deleteAll(){

        return sqLiteDatabase.delete(TABLE_NAME, null, null);
    }


    private List<String> parseJsonArray(String jsonArrayString) {
        List<String> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonArrayString);
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }



    public class SQLiteHelper extends SQLiteOpenHelper {

        public SQLiteHelper (@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // create database
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
            sqLiteDatabase.execSQL(CREATE_TABLE_2);
            sqLiteDatabase.execSQL(CREATE_TABLE_3);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(CREATE_TABLE);
            sqLiteDatabase.execSQL(CREATE_TABLE_2);
            sqLiteDatabase.execSQL(CREATE_TABLE_3);

        }
    }

}
