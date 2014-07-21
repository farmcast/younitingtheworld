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
 * This Class Contains All Method Related To JomEventDataProvider.
 *
 * @author tasol
 *
 */
public class JomEventDataProvider extends IjoomerPagingProvider {
    private Context mContext;

    private final String EVENT = "event";
    private final String CATEGORIES = "categories";
    private final String EVENTS = "events";
    private final String DETAIL = "detail";
    private final String MEMBERS = "members";
    private final String INVITE = "invite";
    private final String TYPE = "type";
    private final String ALL = "all";
    private final String MY = "my";
    private final String GUEST = "guest";
    private final String SEARCH = "search";
    private final String GROUP = "group";
    private final String PENDING = "pending";
    private final String PAST = "past";
    private final String CATEGORYID = "categoryID";
    private final String GROUPID = "groupID";
    private final String QUERY = "query";
    private final String STARTDATE = "startDate";
    private final String ENDDATE = "endDate";
    private final String RADIUS = "radius";
    private final String LOCATION = "location";
    private final String SORTING = "sorting";
    private final String UNIQUEID = "uniqueID";
    private final String STATUS = "status";
    private final String ADMIN = "admin";
    private final String WAITING = "waiting";
    private final String BLOCKED = "blocked";
    private final String EDITAVATAR = "editAvatar";
    private final String RESPONSE = "response";
    private final String REQUESTINVITE = "requestInvite";
    private final String SENDMAIL = "sendmail";
    private final String TITLE = "title";
    private final String MESSAGE = "message";
    private final String REPORT = "report";
    private final String LIKE = "like";
    private final String DISLIKE = "dislike";
    private final String UNLIKE = "unlike";
    private final String IGNORE = "ignore";
    private final String DELETE = "delete";
    private final String ADDWALL = "addWall";
    private final String SETADMIN = "setAdmin";
    private final String SETUSER = "setUser";
    private final String ADDEVENT = "addEvent";
    private final String FIELDS = "fields";
    private final String VALUE = "value";
    private final String FRIENDLIST = "friendList";
    private final String NAME = "name";
    private final String REMOVEMEMBER = "removeMember";
    private final String BLOCK = "block";
    private final String COMMENT = "comment";
    private final String UNBLOCKMEMBER = "unblockMember";
    private final String APPROVEMEMBER = "approveMember";
    private final String MEMBERID = "memberID";

    private boolean isCalling = false;

    /**
     * This method used to check provider execute any request call.
     *
     * @return {@link boolean}
     */
    public boolean isCalling() {
        return isCalling;
    }

    /**
     * Constructor
     *
     * @param context
     *            represented {@link Context}
     */
    public JomEventDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This method used to get event categories list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getEventCategoriesList(final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, CATEGORIES);

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
                    return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get all event list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getAllEventList(final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, EVENTS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, ALL);
                        taskData.put(SORTING, "latest");
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get my event list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getMyEventList(final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, EVENTS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, MY);
                        taskData.put(SORTING, "latest");
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method is used to get pending event list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getPendingEventList(final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, EVENTS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, PENDING);
                        taskData.put(SORTING, "latest");
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get past event list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getPastEventList(final String groupID, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, EVENTS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, PAST);
                        if (!groupID.equals("0")) {
                            taskData.put(GROUPID, groupID);
                        }
                        taskData.put(SORTING, "latest");
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get searched event list.
     *
     * @param categoryID
     *            represented event category id (optional - leave null if not
     *            required)
     * @param query
     *            represented event name (optional - leave null if not required)
     * @param start
     *            represented event start date (optional - leave null if not
     *            required)
     * @param end
     *            represented event end date (optional - leave null if not
     *            required)
     * @param redius
     *            represented event lat-long radius (optional - leave null if
     *            not required)
     * @param location
     *            represented event location (optional - leave null if not
     *            required)
     * @param sorting
     *            represented event sorting option (optional - leave null if not
     *            required)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void searchEventList(final String categoryID, final String query, final String start, final String end, final String redius, final String location,
                                final String sorting, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, EVENTS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, SEARCH);
                        if (categoryID != null) {
                            taskData.put(CATEGORYID, categoryID);
                        }
                        if (query != null) {
                            taskData.put(QUERY, query);
                        }
                        if (start != null) {
                            taskData.put(STARTDATE, start);
                        }
                        if (end != null) {
                            taskData.put(ENDDATE, end);
                        }
                        if (redius != null) {
                            taskData.put(RADIUS, redius);
                        }
                        if (location != null) {
                            taskData.put(LOCATION, location);
                        }
                        taskData.put(SORTING, sorting);
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get group event list.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getGroupEventList(final String groupID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, EVENTS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, GROUP);
                        taskData.put(GROUPID, groupID);
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get event details.
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getEventDetails(final String eventID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, DETAIL);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventID);

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
                    return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get event admin user list.
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getEventAdminList(final String eventID, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, MEMBERS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(UNIQUEID, eventID);
                        taskData.put(TYPE, ADMIN);
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get event guest user list.
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getEventGuestList(final String eventID, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, MEMBERS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(UNIQUEID, eventID);
                        taskData.put(TYPE, GUEST);
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get event waiting user list
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getEventWaitUserList(final String eventID, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, MEMBERS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(UNIQUEID, eventID);
                        taskData.put(TYPE, WAITING);
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get event blocked user list.
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getEventBlockedUserList(final String eventID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, MEMBERS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(UNIQUEID, eventID);
                        taskData.put(TYPE, BLOCKED);
                        taskData.put(PAGENO, getPageNo());

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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to edit event avatar.
     *
     * @param filePath
     *            represented new image file path
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void editEventAvatar(final String filePath, final String eventID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, EDITAVATAR);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventID);
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
     * This method used to gave response event attending or not.
     *
     * @param eventID
     *            represented event id
     * @param status
     *            represented attending status(1-Yes,0-No)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void eventResponse(final String eventID, final String status, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, RESPONSE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventID);
                    taskData.put(STATUS, status);
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
     * This method used to send mail all member of event.
     *
     * @param eventID
     *            represented event id
     * @param title
     *            represented mail title
     * @param message
     *            represented mail message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void sendMailToAllMember(final String eventID, final String title, final String message, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, SENDMAIL);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventID);
                    taskData.put(TITLE, title);
                    if (message.trim().length() > 0) {
                        taskData.put(MESSAGE, message);
                    }
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
     * This method used to report event.
     *
     * @param eventID
     *            represented event id
     * @param message
     *            represented report message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void reportEvent(final String eventID, final String message, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, REPORT);
                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, eventID);
                    taskData.put(MESSAGE, message);
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
     * This method used to request for event invitation
     *
     * @param eventID
     *            represented event id
     * @param status
     *            represented event status
     * @param target
     *            represented {@link WebCallListener}
     *
     */
    public void requestInvitation(final String eventID, final String status, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, REQUESTINVITE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventID);
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
     * This method is used to approve event invitation
     *
     * @param userId
     *            represented user id
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void approvWaitingUser(final String userId, final String eventID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, APPROVEMEMBER);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventID);
                    taskData.put(MEMBERID, userId);
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
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
            }
        }.execute();
    }

    /**
     * This method used to like event.
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void likeEvent(final String eventID, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, LIKE);
                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, eventID);
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
     * This method used to unlike event.
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */

    public void unlikeEvent(final String eventID, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, UNLIKE);
                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, eventID);
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
     * This method used to dislike event.
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */

    public void dislikeEvent(final String eventID, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, DISLIKE);
                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, eventID);
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
     * This method used to remove event.
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeEvent(final String eventID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, DELETE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventID);
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
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
            }
        }.execute();
    }

    /**
     * This method used to ignore event
     *
     * @param eventID
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void ignoreEvent(final String eventID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, IGNORE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventID);
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
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
            }
        }.execute();
    }

    /**
     * This method used to add wall on event.
     *
     * @param eventID
     *            represented event id
     * @param message
     *            represented wall message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addWallPost(final String eventID, final String message,final String voiceFilePath, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, ADDWALL);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventID);
                    taskData.put(MESSAGE, message);
                    taskData.put(COMMENT, "0");
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);
                if(voiceFilePath!=null){
                    iw.WSCall(voiceFilePath,new ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            if (num >= 100) {
                                target.onProgressUpdate(95);
                            } else {
                                target.onProgressUpdate((int) num);
                            }
                        }
                    });

                }else{
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
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
            }
        }.execute();
    }

    /**
     * This method used to set user as event admin.
     *
     * @param userId
     *            represented user id
     * @param eventId
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void setUserAsEventAdmin(final String userId, final String eventId, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, SETADMIN);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(USERID, userId);
                    taskData.put(UNIQUEID, eventId);
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
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
            }
        }.execute();
    }

    /**
     * This method used to set user as event member.
     *
     * @param userId
     *            represented user id
     * @param eventId
     *            represented event id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void setUserAsEventMember(final String userId, final String eventId, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, SETUSER);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(USERID, userId);
                    taskData.put(UNIQUEID, eventId);
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
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
            }
        }.execute();
    }

    /**
     * This method used to submit add or edited event.
     *
     * @param eventID
     *            represented event id
     * @param groupID
     *            represented group id (0 - for event,groupID - for group event)
     * @param eventField
     *            represented event field list
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrEditEventSubmit(final String eventID, final String groupID, final ArrayList<HashMap<String, String>> eventField, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, ADDEVENT);
                JSONObject taskData = new JSONObject();
                try {
                    if (!eventID.equals("0")) {
                        taskData.put(UNIQUEID, eventID);
                    }
                    if (!groupID.equals("0")) {
                        taskData.put(GROUPID, groupID);
                    }
                    taskData.put(FIELDS, "0");
                    for (HashMap<String, String> hashMap : eventField) {
                        taskData.put(hashMap.get(NAME), hashMap.get(VALUE));
                    }
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
                    return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to get add or edited event field list.
     *
     * @param eventID
     *            represented event id
     * @param groupID
     *            represented group id (0 - for event,groupID - for group event)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrEditEventFieldList(final String eventID, final String groupID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, ADDEVENT);

                JSONObject taskData = new JSONObject();
                try {
                    if (!eventID.equals("0")) {
                        taskData.put(UNIQUEID, eventID);
                    }
                    if (!groupID.equals("0")) {
                        taskData.put(GROUPID, groupID);
                    }
                    taskData.put(FIELDS, "1");

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
                    return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to invite friend for event.
     *
     * @param eventID
     *            represented event id
     * @param userIDS
     *            represented friends id with (,) separated
     * @param message
     *            represented invitation message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void inviteFriend(final String eventID, final String userIDS, final String message, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, INVITE);

                JSONObject taskData = new JSONObject();
                try {
                    if (eventID != null) {
                        taskData.put(UNIQUEID, eventID);
                    }
                    if (message != null) {
                        taskData.put(MESSAGE, message);
                    }
                    taskData.put(USERID, userIDS);
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
     * This method used to get event invite friend list
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getInviteFriendList(final String groupID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, EVENT);
                    iw.addWSParam(EXTTASK, FRIENDLIST);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(UNIQUEID, groupID);
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
                            iw.getJsonObject().remove(PAGELIMIT);
                            iw.getJsonObject().remove(TOTAL);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return new IjoomerCaching(mContext).parseData(iw.getJsonObject());

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
     * This method used to remove event user.
     *
     * @param userId
     *            represented user id
     * @param eventId
     *            represented event id
     * @param block
     *            represented block user or not
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeOrBlockMember(final String userId, final String eventId, final boolean block, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, REMOVEMEMBER);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(USERID, userId);
                    taskData.put(UNIQUEID, eventId);
                    taskData.put(BLOCK, block ? "1" : "0");
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
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
            }
        }.execute();
    }

    /**
     * This method used unblock event user.
     *
     * @param eventId
     *            represented event id
     * @param userId
     *            represented user id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void unblockMember(final String eventId, final String userId, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, EVENT);
                iw.addWSParam(EXTTASK, UNBLOCKMEMBER);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, eventId);
                    taskData.put(USERID, userId);
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

}
