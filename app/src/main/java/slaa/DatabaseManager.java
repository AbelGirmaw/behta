package slaa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and columns
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_ROLE = "role";
    public static final String COLUMN_PASSWORD = "password";

    // Create table SQL query
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_FIRST_NAME + " TEXT,"
                    + COLUMN_LAST_NAME + " TEXT,"
                    + COLUMN_USERNAME + " TEXT UNIQUE,"
                    + COLUMN_EMAIL + " TEXT UNIQUE,"
                    + COLUMN_ROLE + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT"
                    + ")";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Empty - we'll create tables only when explicitly called
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Method to create users table explicitly
    public void createUserTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(CREATE_TABLE_USERS);
        } catch (Exception e) {
            // Table may already exist
        } finally {
            db.close();
        }
    }

    // Check if users table exists
    public boolean isUserTableExists() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?",
                new String[]{TABLE_USERS});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Insert a new user (with table existence check)
    public long insertUser(String firstName, String lastName, String username,
                           String email, String role, String password) {
        // Ensure table exists before inserting
        if (!isUserTableExists()) {
            createUserTable();
        }

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_FIRST_NAME, firstName);
        values.put(COLUMN_LAST_NAME, lastName);
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_ROLE, role);
        values.put(COLUMN_PASSWORD, password);

        long id = db.insert(TABLE_USERS, null, values);
        db.close();
        return id;
    }

    // Check if username exists
    public boolean checkUsername(String username) {
        if (!isUserTableExists()) {
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=?",
                new String[]{username},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Check if email exists
    public boolean checkEmail(String email) {
        if (!isUserTableExists()) {
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_EMAIL + "=?",
                new String[]{email},
                null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }

    // Authenticate user
    public boolean authenticateUser(String username, String password) {
        if (!isUserTableExists()) {
            return false;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{COLUMN_ID},
                COLUMN_USERNAME + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{username, password},
                null, null, null);

        boolean authenticated = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return authenticated;
    }
}