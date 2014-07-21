package com.ijoomer.library.jomsocial;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerResponseValidator;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomPrivacyDataProvider.
 *
 * @author tasol
 *
 */
public class JomPrivacyDataProvider extends IjoomerResponseValidator {

    private Context mContext;

    private final String USER = "user";
    private final String PREFERENCES = "preferences";
    private final String FORM = "form";
    private final String TABLENAME = "preferences";
    private final String VALUE = "value";
    private final String NAME = "name";
    private final String FORMDATA = "formData";
    private final String GROUP_NAME = "group_name";

    /**
     * Constructor
     *
     * @param context
     *            represented {@link Context}
     */
    public JomPrivacyDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This method used to get user privacy setting list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getPrivacySetting(final WebCallListener target) {

        final IjoomerCaching ic = new IjoomerCaching(mContext);
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            protected void onPreExecute() {
                ArrayList<HashMap<String, String>> privacyData = getFields();
                if (privacyData != null) {
                    target.onCallComplete(200, getErrorMessage(), privacyData, null);
                }
            }

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, USER);
                iw.addWSParam(EXTTASK, PREFERENCES);
                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(FORM, "1");
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);
                iw.WSCall(new ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        if (num >= 100) {
                            target.onProgressUpdate(95);
                        } else {
                            target.onProgressUpdate((int) num);
                        }
                    }
                });

                if (validateResponse(iw.getJsonObject())) {
                    return ic.cacheData(iw.getJsonObject(), true, TABLENAME);
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, false);
            }
        }.execute();

    }

    /**
     * This method used to submit user privacy setting.
     *
     * @param privacyField
     *            represented privacy setting field list
     * @param target
     *            represented {@link WebCallListener}
     */
    public void submitPrivacySetting(final ArrayList<HashMap<String, String>> privacyField, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, USER);
                iw.addWSParam(EXTTASK, PREFERENCES);
                JSONObject taskData = new JSONObject();
                JSONArray formData = new JSONArray();
                try {
                    taskData.put(FORM, "0");

                    for (HashMap<String, String> hashMap : privacyField) {
                        if (getStringArray(hashMap.get(VALUE)) != null) {
                            String[] names = getStringArray(hashMap.get(NAME));
                            String[] values = getStringArray(hashMap.get(VALUE));

                            int size = values.length;
                            for (int j = 0; j < size; j++) {
                                JSONObject value = new JSONObject();
                                value.put(NAME, names[j]);
                                value.put(VALUE, values[j].equalsIgnoreCase("null") ? "1" : values[j]);
                                formData.put(value);
                            }
                        } else {

                            JSONObject value = new JSONObject();
                            value.put(NAME, hashMap.get(NAME));
                            value.put(VALUE, hashMap.get(VALUE));
                            formData.put(value);
                        }
                    }
                    taskData.put(FORMDATA, formData);

                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);
                iw.WSCall(new ProgressListener() {

                    @Override
                    public void transferred(long num) {
                        if (num >= 100) {
                            target.onProgressUpdate(95);
                        } else {
                            target.onProgressUpdate((int) num);
                        }
                    }
                });

                if (validateResponse(iw.getJsonObject())) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (getResponseCode() == 200) {
                    new IjoomerCaching(mContext).updateTable(privacyField, TABLENAME);
                }
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();

    }

    /**
     * This method used to get status of privacy settings existence
     *
     * @return {@link boolean}
     */
    public boolean isPrivacySettingExists() {
        try {
            IjoomerCaching ic = new IjoomerCaching(mContext);
            return ic.isTableExists(TABLENAME);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method used to get user privacy setting field list for group.
     *
     * @param groupName
     *            represented privacy setting group name
     * @return {@link ArrayList<HashMap<String, String>>}
     */
    public ArrayList<HashMap<String, String>> getFields(String groupName) {
        try {
            IjoomerCaching ic = new IjoomerCaching(mContext);
            return ic.getDataFromCache(TABLENAME, "SELECT * FROM " + TABLENAME + " where " + GROUP_NAME + "='" + groupName + "'");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method used to get user privacy setting field list.
     *
     * @return {@link ArrayList<HashMap<String, String>>}
     */
    public ArrayList<HashMap<String, String>> getFields() {
        try {
            IjoomerCaching ic = new IjoomerCaching(mContext);
            return ic.getDataFromCache(TABLENAME, "SELECT * FROM " + TABLENAME);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method used to get user privacy setting field group list.
     *
     * @return {@link ArrayList<HashMap<String, String>>}
     */
    public ArrayList<HashMap<String, String>> getFieldGroups() {
        try {
            IjoomerCaching ic = new IjoomerCaching(mContext);
            return ic.getDataFromCache(TABLENAME, "SELECT " + GROUP_NAME + " FROM " + TABLENAME + " group by " + GROUP_NAME);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method is used to get String Array from String
     *
     * @param value
     *            represented string
     * @return {@link String[]}
     */
    private String[] getStringArray(String value) {
        try {
            JSONArray temp = new JSONArray(value);
            int length = temp.length();
            if (length > 0) {
                String[] recipients = new String[length];
                for (int i = 0; i < length; i++) {
                    recipients[i] = temp.getString(i);
                }
                return recipients;
            }
        } catch (Exception e) {
        }
        return null;

    }

}
