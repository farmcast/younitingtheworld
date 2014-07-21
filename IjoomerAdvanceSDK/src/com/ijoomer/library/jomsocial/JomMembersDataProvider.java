package com.ijoomer.library.jomsocial;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomMembersDataProvider.
 *
 * @author tasol
 *
 */
public class JomMembersDataProvider extends IjoomerPagingProvider {

    private Context mContext;

    private final String FRIEND = "friend";
    private final String MEMBERS = "members";
    private final String SEARCH = "search";
    private final String QUERY = "query";


    /**
     * This method used to check provider execute any request call.
     *
     * @return {@link boolean}
     */
    private boolean isCalling = false;

    public boolean isCalling() {
        return isCalling;
    }

    /**
     * Constructor
     *
     * @param context
     *            represented {@link Context}
     */
    public JomMembersDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This method used to get site member list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getMembersList(final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, FRIEND);
                    iw.addWSParam(EXTTASK, MEMBERS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(PAGENO, "" + getPageNo());
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
                        try {
                            setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
                            iw.getJsonObject().remove(TOTAL);
                            iw.getJsonObject().remove(PAGELIMIT);
                            return new IjoomerCaching(mContext).cacheData(iw.getJsonObject(), false, MEMBERS);
                        } catch (Throwable e) {
                        }
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
                    super.onPostExecute(result);
                    isCalling = false;
                    target.onProgressUpdate(100);
                    target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
                }
            }.execute();
        } else {
            target.onProgressUpdate(100);
            target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
        }

    }

    /**
     * This method used to get searched user list.
     *
     * @param keyWord
     *            represented search key word
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getSearchMembersList(final String keyWord, final WebCallListener target) {
        if (hasNextPage()) {
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, FRIEND);
                    iw.addWSParam(EXTTASK, SEARCH);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(PAGENO, "" + getPageNo());
                        taskData.put(QUERY, keyWord);
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
                        try {
                            setPagingParams(Integer.parseInt(iw.getJsonObject().getString(PAGELIMIT)), Integer.parseInt(iw.getJsonObject().getString(TOTAL)));
                            iw.getJsonObject().remove(TOTAL);
                            iw.getJsonObject().remove(PAGELIMIT);
                            return new IjoomerCaching(mContext).cacheData(iw.getJsonObject(), false, MEMBERS);
                        } catch (Throwable e) {
                        }
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
        } else {
            target.onProgressUpdate(100);
            target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
        }

    }

    /**
     * This method used to get searched member list.
     *
     * @param searchChar
     *            represented search key word
     * @return
     */
    public ArrayList<HashMap<String, String>> searchMember(String searchChar) {
        return new IjoomerCaching(mContext).getDataFromCache(MEMBERS, "select * from " + MEMBERS + " where " + USER_NAME + " like '%" + searchChar + "%' order by " + USER_NAME);
    }

    /**
     * This method used to get member list from database.
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getMemmberFromDB() {
        return new IjoomerCaching(mContext).getDataFromCache(MEMBERS, "select * from " + MEMBERS + " order by " + USER_NAME);
    }
}
