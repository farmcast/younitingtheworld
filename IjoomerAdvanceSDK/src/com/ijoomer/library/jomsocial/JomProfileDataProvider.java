package com.ijoomer.library.jomsocial;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomProfileDataProvider.
 *
 * @author tasol
 *
 */
public class JomProfileDataProvider extends IjoomerPagingProvider {
    private Context mContext;

    private final String PROFILE = "profile";
    private final String UPDATEPROFILE = "updateProfile";
    private final String NAME = "name";
    private final String FRIEND = "friend";
    private final String ADDFRIEND = "addFriend";
    private final String REMOVEFRIEND = "removeFriend";
    private final String MEMBERID = "memberID";
    private final String MESSAGE = "message";
    private final String USER = "user";
    private final String USERDETAIL = "userDetail";
    private final String TABLENAMEUSERDETAIL = "userdetails";
    private final String TABLENAMEUSERPROFILE = "userProfile";
    private final String GROUP_NAME = "group_name";
    private final String FORM = "form";
    private final String VALUE = "value";
    private final String FORMDATA = "formData";
    private final String LIKE = "like";
    private final String UNLIKE = "unlike";
    private final String DISLIKE = "dislike";
    private final String PRIVACY = "privacy";

    /**
     * Constructor
     *
     * @param context
     *            represented {@link Context}
     */
    public JomProfileDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This method used to update user profile.
     *
     * @param filePath
     *            represented avatar path user (optional - leave blank if not
     *            required)
     * @param name
     *            represented name of user (optional - leave blank if not
     *            required)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void updateUserProfile(final String filePath, final String name, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, USER);
                iw.addWSParam(EXTTASK, UPDATEPROFILE);

                JSONObject taskData = new JSONObject();
                try {
                    if (name != null)
                        taskData.put(NAME, name);
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);
                if (filePath != null) {
                    iw.WSCall(filePath, new ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            if (num >= 100) {
                                target.onProgressUpdate(95);
                            } else {
                                target.onProgressUpdate((int) num);
                            }
                        }
                    });
                } else {
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

                }

                if (validateResponse(iw.getJsonObject())) {
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();
    }

    /**
     * This method used to send friend request.
     *
     * @param memberID
     *            represented requested user id
     * @param message
     *            represented message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addFriend(final String memberID, final String message, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, FRIEND);
                iw.addWSParam(EXTTASK, ADDFRIEND);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(MEMBERID, memberID);
                    taskData.put(MESSAGE, message);
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
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();
    }

    /**
     * This method is used to remove friend
     *
     * @param memberID
     *            represented member id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeFriend(final String memberID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, FRIEND);
                iw.addWSParam(EXTTASK, REMOVEFRIEND);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(MEMBERID, memberID);
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
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();
    }

    /**
     * This method used to get user details.
     *
     * @param userId
     *            represented user id (0- for login user,userID- for other user)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getUserDetails(final String userId, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        final IjoomerCaching ic = new IjoomerCaching(mContext);
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, USER);
                iw.addWSParam(EXTTASK, USERDETAIL);
                JSONObject taskData = new JSONObject();
                try {
                    if (!userId.equals("0")) {
                        taskData.put(USERID, userId);
                    }
                    taskData.put(FORM, "1");

                } catch (Throwable e) {
                    e.printStackTrace();
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
                    return ic.cacheData(iw.getJsonObject(), true, TABLENAMEUSERDETAIL);
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
     * This method used to get user profile.
     *
     * @param userId
     *            represented user id (0- for login user,userID- for other user)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getUserProfile(final String userId, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();


        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            protected void onPreExecute() {
                if (userId.equals("0")) {
                    ArrayList<HashMap<String, String>> profileData = getUserProfileDB();
                    if (profileData != null) {
                        target.onCallComplete(200, getErrorMessage(), profileData, null);
                    }
                }
            };

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, USER);
                iw.addWSParam(EXTTASK, PROFILE);
                JSONObject taskData = new JSONObject();
                try {
                    if (!userId.equals("0")) {
                        taskData.put(USERID, userId);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
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
                try{
                    final IjoomerCaching ic = new IjoomerCaching(mContext);
                    if (validateResponse(iw.getJsonObject())) {
                        if (userId.equals("0")) {
                            return ic.cacheData(iw.getJsonObject(), true, TABLENAMEUSERPROFILE);
                        } else {
                            return ic.parseData(iw.getJsonObject());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();

    }

    /**
     * This method is used to get status of user profile existence
     *
     * @return {@link boolean}
     */
    public boolean isUserProfileExists() {
        try {
            IjoomerCaching ic = new IjoomerCaching(mContext);
            return ic.isTableExists(TABLENAMEUSERPROFILE);
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * This method is used to get UserProfiles from database
     *
     * @return {@link ArrayList<HashMap<String, String>>}
     */
    public ArrayList<HashMap<String, String>> getUserProfileDB() {
        try {
            IjoomerCaching ic = new IjoomerCaching(mContext);
            return ic.getDataFromCache(TABLENAMEUSERPROFILE, "SELECT * FROM " + TABLENAMEUSERPROFILE);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method used to get user details field list for group.
     *
     * @param groupName
     *            represented user details field group name
     * @return {@link ArrayList<HashMap<String, String>>}
     */
    public ArrayList<HashMap<String, String>> getFields(String groupName) {
        try {
            IjoomerCaching ic = new IjoomerCaching(mContext);
            return ic.getDataFromCache(TABLENAMEUSERDETAIL, "SELECT * FROM " + TABLENAMEUSERDETAIL + " where " + GROUP_NAME + "='" + groupName + "'");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method used to get user details field list.
     *
     * @return {@link ArrayList<HashMap<String, String>>}
     */
    public ArrayList<HashMap<String, String>> getFields() {
        try {
            IjoomerCaching ic = new IjoomerCaching(mContext);
            return ic.getDataFromCache(TABLENAMEUSERDETAIL, "SELECT * FROM " + TABLENAMEUSERDETAIL);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method used to get user details field group list.
     *
     * @return {@link ArrayList<HashMap<String, String>>}
     */
    public ArrayList<HashMap<String, String>> getFieldGroups() {
        try {
            IjoomerCaching ic = new IjoomerCaching(mContext);
            return ic.getDataFromCache(TABLENAMEUSERDETAIL, "SELECT " + GROUP_NAME + " FROM " + TABLENAMEUSERDETAIL + " group by " + GROUP_NAME);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method used to update user details.
     *
     * @param userDetailFields
     *            represented user details field list
     * @param target
     *            represented {@link WebCallListener}
     */
    public void updateUserDetails(final ArrayList<HashMap<String, String>> userDetailFields, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, USER);
                iw.addWSParam(EXTTASK, USERDETAIL);
                JSONObject taskData = new JSONObject();
                JSONObject formData = new JSONObject();
                try {
                    taskData.put(FORM, "0");
                    for (HashMap<String, String> hashMap : userDetailFields) {
                        formData.put("f" + hashMap.get("id"), new JSONArray(Arrays.asList(new String[] { hashMap.get(VALUE), new JSONObject(hashMap.get(PRIVACY)).getString(VALUE) })));
                    }
                    taskData.put(FORMDATA, formData);

                } catch (Throwable e) {
                    e.printStackTrace();
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
                validateResponse(iw.getJsonObject());
                return null;
            }

            @Override
            protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                super.onPostExecute(result);
                if (getResponseCode() == 200) {
                    new IjoomerCaching(mContext).updateTable(userDetailFields, TABLENAMEUSERDETAIL);
                }
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();

    }

    /**
     * This method used to like user profile.
     *
     * @param userId
     *            represented user id (0- for login user,userID- for other user)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void likeUserProfile(final String userId, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, USER);
                iw.addWSParam(EXTTASK, LIKE);
                JSONObject taskData = new JSONObject();
                try {
                    if (!userId.equals("0")) {
                        taskData.put(USERID, userId);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
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
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();

    }

    /**
     * This method used to unlike user profile.
     *
     * @param userId
     *            represented user id (0- for login user,userID- for other user)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void unlikeUserProfile(final String userId, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, USER);
                iw.addWSParam(EXTTASK, UNLIKE);
                JSONObject taskData = new JSONObject();
                try {
                    if (!userId.equals("0")) {
                        taskData.put(USERID, userId);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
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
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();

    }

    /**
     * This method used to dislike user profile.
     *
     * @param userId
     *            represented user id (0- for login user,userID- for other user)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void dislikeUserProfile(final String userId, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, USER);
                iw.addWSParam(EXTTASK, DISLIKE);
                JSONObject taskData = new JSONObject();
                try {
                    if (!userId.equals("0")) {
                        taskData.put(USERID, userId);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
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
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
            }
        }.execute();

    }

}
