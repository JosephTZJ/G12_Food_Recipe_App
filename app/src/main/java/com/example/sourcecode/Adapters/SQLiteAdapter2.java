package com.example.sourcecode.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;


public class SQLiteAdapter2 {
    private static final String MYDATABASE_NAME = "MY_DATABASE";
    private static final int MYDATABASE_VERSION = 1;

    // Define table and column names
    private static final String DATABASE_TABLE = "MY_RECIPE_TABLE";
    private static final String COLUMN_FIELD1 = "RecipeTitle";
    private static final String COLUMN_FIELD2 = "Ingredient";
    private static final String COLUMN_FIELD3 = "Step";
    private static final String COLUMN_FIELD4 = "Date";

    // Create table query
    private static final String SCRIPT_CREATE_DATABASE =
            "CREATE TABLE " + DATABASE_TABLE +
                    " ( id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIELD1 + " TEXT, " +
                    COLUMN_FIELD2 + " TEXT, " +
                    COLUMN_FIELD3 + " TEXT, " +
                    COLUMN_FIELD4 + " TEXT);";

    // variables
    private Context context;
    private SQLiteHelper sqLiteHelper;
    private SQLiteDatabase sqLiteDatabase;

    public SQLiteAdapter2(Context c){
        context = c;
    }

    public SQLiteAdapter2 openToWrite() throws android.database.SQLException{

        // create a table with MYDATABASE_NAME and the version of MYDATABASE_VERSION
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME,
                null, MYDATABASE_VERSION);

        // open to write
        sqLiteDatabase = sqLiteHelper.getWritableDatabase();
        return this;
    }

    // open the database to read
    public SQLiteAdapter2 openToRead() throws android.database.SQLException{
        sqLiteHelper = new SQLiteHelper(context, MYDATABASE_NAME, null, MYDATABASE_VERSION);

        //open to read
        sqLiteDatabase = sqLiteHelper.getReadableDatabase();

        return this;
    }

    // insert data into the column
    public long insert (String content, String content_2, String content_3, String content_4){

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_FIELD1, content);
        contentValues.put(COLUMN_FIELD2, content_2);
        contentValues.put(COLUMN_FIELD3, content_3);
        contentValues.put(COLUMN_FIELD4, content_4);

        return sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public long delete (String content, String content_2, String content_3, String content_4){
        try{
            String selection = "RecipeTitle = ? AND Ingredient = ? AND Step = ? AND Date = ?";
            String[] selectionArgs = {content, content_2, content_3, content_4};

            return sqLiteDatabase.delete(DATABASE_TABLE, selection, selectionArgs);
        } catch (Exception e){
            e.printStackTrace();
            return -1; // An error occurred
        }
    }

    public String queueAll(String thedate){

        String[] columns = new String[] {COLUMN_FIELD1, COLUMN_FIELD2, COLUMN_FIELD3, COLUMN_FIELD4};
        String selection = "Date = ?";
        String[] selectionArgs = {thedate};
        Cursor cursor = sqLiteDatabase.query(DATABASE_TABLE, columns,
                selection, selectionArgs,
                null, null, null);
        String result = "";

        int index_CONTENT = cursor.getColumnIndex(COLUMN_FIELD1);
        int index_CONTENT_2 = cursor.getColumnIndex(COLUMN_FIELD2);
        int index_CONTENT_3 = cursor.getColumnIndex(COLUMN_FIELD3);
        int index_CONTENT_4 = cursor.getColumnIndex(COLUMN_FIELD4);

        for(cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()){
            result = result + "*" + cursor.getString(index_CONTENT) + "* \n"
                    + cursor.getString(index_CONTENT_2) + "\n"
                    + cursor.getString(index_CONTENT_3) + "\n"
                    + cursor.getString(index_CONTENT_4) + "\n";
        }

        return result;

    }

    public Cursor queryAllByDate(String theDate) {
        String[] columns = new String[] {COLUMN_FIELD1, COLUMN_FIELD2, COLUMN_FIELD3, COLUMN_FIELD4};
        String selection = "Date = ?";
        String[] selectionArgs = {theDate};

        return sqLiteDatabase.query(DATABASE_TABLE, columns, selection, selectionArgs, null, null, null);
    }


    //--------------------------------------------------------------------------------------------

    //close the database
    public void close(){
        sqLiteHelper.close();
    }

    //delete all the content in the table / delete the table --> so that data will not be repeated
    public int deleteAll(){
        return sqLiteDatabase.delete(DATABASE_TABLE, null, null);
    }


    public class SQLiteHelper extends SQLiteOpenHelper{

        // constructor with 4 parameters
        public SQLiteHelper(@Nullable Context context,
                            @Nullable String name,
                            @Nullable SQLiteDatabase.CursorFactory factory,
                            int version) {
            super(context, name, factory, version);
        }

        // to create the database
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase){
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }

        // version control
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(SCRIPT_CREATE_DATABASE);
        }
    }
}
