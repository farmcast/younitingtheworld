package com.ijoomer.caching;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

/**
 * This Class Contains Method IjoomerAdvance Database DataHelper.
 * @author tasol
 *
 */
public class IjoomerDataHelper {

    private String databaseName;
    private int databaseVersion;
    private String databaseSql;

    private IjoomerArrayList tables = new IjoomerArrayList();
    private Context context;
    private SQLiteDatabase db;
    public static IjoomerDataHelper smartDataHelper;


    /**
     * This method used to get database name.
     * @return {@link String}
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * This method used to set database name.
     * @param databaseName represented database name
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * This method used to get database version.
     * @return {@link Integer}
     */
    public int getDatabaseVersion() {
        return databaseVersion;
    }

    /**
     * This method used to set database version.
     * @param databaseVersion represented database version
     */
    public void setDatabaseVersion(int databaseVersion) {
        this.databaseVersion = databaseVersion;
    }


    /**
     * This method used to get database sql.
     * @return {@link String}}
     */
    public String getDatabaseSql() {
        return databaseSql;
    }

    /**
     * This method used to set database sql.
     * @param databaseSql represented databse sql
     */
    public void setDatabaseSql(String databaseSql) {
        this.databaseSql = databaseSql;
    }

    /**
     * This method used to set database cache configuration.
     */
    public void setCacheConfiguration() {
        databaseName = "ijoomer";
        databaseSql = "ijoomer.sql";
        databaseVersion = 1;
    }

    /**
     * Constructor
     * @param context represented {@link Context}
     * @throws IOException {@link Exception}
     */
    private IjoomerDataHelper(Context context) throws IOException {
        setCacheConfiguration();
        IjoomerOpenHelper openHelper = new IjoomerOpenHelper(context, databaseName, databaseVersion, databaseSql);
        this.db = openHelper.getWritableDatabase();
        grabTables();

    }

    /**
     * Constructor
     * @param context represented {@link Context}}
     * @param dbName represented database name
     * @param dbVersion represented database version
     * @param dbSQL represented database sql
     * @throws IOException {@link Exception}
     */
    private IjoomerDataHelper(Context context, String dbName, int dbVersion, String dbSQL) throws IOException {
        databaseName = dbName;
        databaseVersion = dbVersion;
        databaseSql = dbSQL;
        this.context = context;
        IjoomerOpenHelper openHelper = new IjoomerOpenHelper(this.context, databaseName, databaseVersion, databaseSql);
        this.db = openHelper.getWritableDatabase();
        grabTables();
    }

    /**
     * This method used to grab database table.
     */
    @SuppressWarnings("unchecked")
    public void grabTables() {
        Cursor cur = this.db.rawQuery("SELECT * FROM sqlite_master", new String[0]);
        cur.moveToFirst();
        String tableName;

        while (cur.getPosition() < cur.getCount()) {
            tableName = cur.getString(cur.getColumnIndex("name"));
            System.out.println("Table Name = " + tableName);
            if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence") && (!cur.getString(cur.getColumnIndex("type")).equalsIgnoreCase("index"))) {
                this.tables.add(new IjoomerTable(this.db, tableName));
            }
            cur.moveToNext();
        }
        cur.close();
    }


    /**
     * This method used to get database table list.
     * @return {@link IjoomerArrayList}
     * @throws Exception {@link Exception}
     */
    public IjoomerArrayList getTableList() throws Exception {
        if (this.tables.size() == 0) {
            Exception t = new Exception("There are no tables to show.");
            throw t;
        }
        return this.tables;
    }

    /**
     * This method used to get database.
     * @return {@link SQLiteDatabase}
     */
    public SQLiteDatabase getDB() {
        return db;
    }

    /**
     * This method used to add table in database.
     * @param tableName represented table name
     */
    @SuppressWarnings("unchecked")
    public void addTable(String tableName) {
        this.tables.add(new IjoomerTable(this.db, tableName));
    }


    /**
     * This method used to get {@link IjoomerDataHelper} instance.
     * @param context {@link Context}
     * @return {@link IjoomerDataHelper}
     */
    public static IjoomerDataHelper getInstance(Context context) {
        if (smartDataHelper == null) {
            try {
                smartDataHelper = new IjoomerDataHelper(context);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return smartDataHelper;
    }
}
