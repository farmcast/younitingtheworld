package com.ijoomer.library.jomsocial;

import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerPagingProvider;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To JomGroupDataProvider.
 *
 * @author tasol
 *
 */
public class JomGroupDataProvider extends IjoomerPagingProvider {
    private Context mContext;
    private final String GROUP = "group";
    private final String GROUPS = "groups";
    private final String DETAIL = "detail";
    private final String MEMBERS = "members";
    private final String WAITING = "waiting";
    private final String BANMEMBERS = "banMembers";
    private final String INVITE = "invite";
    private final String JOIN = "join";
    private final String LEAVE = "leave";
    private final String ANNOUNCEMENT = "announcement";
    private final String DISCUSSION = "discussion";
    private final String REPLY = "reply";
    private final String TYPE = "type";
    private final String HITS = "hits";
    private final String ALL = "all";
    private final String SEARCH = "search";
    private final String QUERY = "query";
    private final String CATID = "catID";
    private final String MY = "my";
    private final String PENDING = "pending";
    private final String SORT = "sort";
    private final String UNIQUEID = "uniqueID";
    private final String ANNOUNCEMENTID = "announcementID";
    private final String DISCUSSIONID = "discussionID";
    private final String ADMIN = "admin";
    private final String EDITAVATAR = "editAvatar";
    private final String SENDMAIL = "sendmail";
    private final String TITLE = "title";
    private final String MESSAGE = "message";
    private final String REPORT = "report";
    private final String LIKE = "like";
    private final String DISLIKE = "dislike";
    private final String UNLIKE = "unlike";
    private final String UNPUBLISH = "unpublish";
    private final String DELETE = "delete";
    private final String SETADMIN = "setAdmin";
    private final String APPROVEMEMBER = "approveMember";
    private final String BAN = "ban";
    private final String REMOVEMEMBER = "removeMember";
    private final String ADDWALL = "addWall";
    private final String ADDGROUP = "addGroup";
    private final String ADDANNOUNCEMENT = "addAnnouncement";
    private final String ADDDISCUSSIONREPLY = "addDiscussionReply";
    private final String ADDDISCUSSION = "addDiscussion";
    private final String DELETEANNOUNCEMENT = "deleteAnnouncement";
    private final String DELETEDISCUSSION = "deleteDiscussion";
    private final String FILE = "file";
    private final String FIELDS = "fields";
    private final String VALUE = "value";
    private final String MEMBERID = "memberID";
    private final String FRIENDLIST = "friendList";
    private final String NAME = "name";
    private final String COMMENT = "comment";
    private final String LOCKDISCUSSION = "lockDiscussion";
    private final String WALLID = "wallID";
    private final String FILES = "files";
    private final String UPLOADFILE = "uploadFile";
    private final String DISCUSSIONDETAIL = "discussionDetail";
    private final String INVITATION = "invitation";
    private final String FRIENDS = "friends";

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
    public JomGroupDataProvider(Context context) {
        super(context);
        mContext = context;
    }

    /**
     * This method used to get all group list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getAllGroupList(final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, GROUPS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, ALL);
                        taskData.put(SORT, "latest");
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
     * This method is used to search groups
     *
     * @param categoryID
     *            represented category id (optional - set null if not required)
     * @param query
     *            represented query to search
     * @param sorting
     *            represented sorting (optional - set null if not required)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void searchGroupList(final String categoryID, final String query, final String sorting, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, GROUPS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, SEARCH);
                        if (sorting != null) {
                            taskData.put(SORT, sorting);
                        }
                        taskData.put(QUERY, query);
                        if (categoryID != null) {
                            taskData.put(CATID, categoryID);
                        }
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
     * This method use to get group discussion list.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getDiscussionList(final String groupID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, DISCUSSION);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(PAGENO, "" + getPageNo());
                        taskData.put(UNIQUEID, groupID);

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
     * This method used to get group announcement list.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getAnnouncementList(final String groupID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, ANNOUNCEMENT);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(PAGENO, "" + getPageNo());
                        taskData.put(UNIQUEID, groupID);

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
     * This method used to get my group list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getMyGroupList(final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, GROUPS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, MY);
                        taskData.put(SORT, "latest");
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
     * This method used to pending group list.
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getPendingGroupList(final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, GROUPS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, PENDING);
                        taskData.put(SORT, "latest");
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
     * This method used to get group details.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getGroupDetails(final String groupID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, DETAIL);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);

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
     * This method used to get group member list.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getGroupWaitingMemberList(final String groupID, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, MEMBERS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(UNIQUEID, groupID);
                        taskData.put(WAITING, "1");
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
     * This method used to get group member list.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getGroupMemberList(final String groupID, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, MEMBERS);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(UNIQUEID, groupID);
                        taskData.put(WAITING, "0");
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
     * This method used to get group ban member list.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getGroupBanMemberList(final String groupID, final WebCallListener target) {

        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, BANMEMBERS);

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
     * This method used to edit group avatar.
     *
     * @param filePath
     *            represented image file path
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void editGroupAvatar(final String filePath, final String groupID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, EDITAVATAR);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
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
     * This method is used to accept group invitation
     *
     * @param groupID
     *            represented group id
     * @param status
     *            represented status for group join
     * @param target
     *            represented {@link WebCallListener}
     */
    public void groupInvitation(final String groupID, final String status, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, INVITATION);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(TYPE, status);
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
     * This method used to send mail to group all member.
     *
     * @param groupID
     *            represented group id
     * @param title
     *            represented mail title
     * @param message
     *            represented mail message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void sendMailToAllMember(final String groupID, final String title, final String message, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, SENDMAIL);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
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
     * This method used to report group or group discussion.
     *
     * @param groupID
     *            represented group id
     * @param discussionID
     *            represented group discussion id(0- for group
     *            report,discussionID- for group discussion)
     * @param message
     *            represented report message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void reportGroupOrDiscussion(final String groupID, final String discussionID, final String message, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, REPORT);
                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(MESSAGE, message);

                    if (!discussionID.equals("0")) {
                        taskData.put(DISCUSSIONID, discussionID);
                        taskData.put(TYPE, DISCUSSION);
                    } else {
                        taskData.put(TYPE, GROUP);
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
     * This method used to like group.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void likeGroup(final String groupID, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, LIKE);
                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, groupID);
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
     * This method used to unlike group.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void unlikeGroup(final String groupID, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, UNLIKE);
                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, groupID);
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
     * This method used to dislike group.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void dislikeGroup(final String groupID, final WebCallListener target) {
        final IjoomerWebService iw = new IjoomerWebService();
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, DISLIKE);
                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, groupID);
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
     * This method used to remove group.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeGroup(final String groupID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, DELETE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
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
     * This method used to unpublish group.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void unpublishGroup(final String groupID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, UNPUBLISH);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
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
     * this method is used to approve user for it's join group request
     *
     * @param userId
     *            represented user id
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void approvWaitingUser(final String userId, final String groupID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, APPROVEMEMBER);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
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
     * This method used to set group used as admin.
     *
     * @param userId
     *            represented user id
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void setUserAsGroupAdmin(final String userId, final String groupID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, SETADMIN);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(MEMBERID, userId);
                    taskData.put(ADMIN, "1");
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
     * This method used to set group used as member.
     *
     * @param userId
     *            represented user id
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void setUserAsGroupMember(final String userId, final String groupID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, SETADMIN);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(MEMBERID, userId);
                    taskData.put(ADMIN, "0");
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
     * This method is used to ban user from group
     *
     * @param userId
     *            represented user id
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     *
     */
    public void setUserAsGroupBan(final String userId, final String groupID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, BAN);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(MEMBERID, userId);
                    taskData.put(BAN, "1");
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
     * This method is used to unban user for group
     *
     * @param userId
     *            represented user id
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     *
     */
    public void setUserAsGroupUnban(final String userId, final String groupID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, BAN);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(MEMBERID, userId);
                    taskData.put(BAN, "0");
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
     * This method is used to remove group member
     *
     * @param userId
     *            represented user id
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeGroupMember(final String userId, final String groupID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, REMOVEMEMBER);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
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
     * This method used to submit add or edited group.
     *
     * @param groupID
     *            represented group id (0- for new group,groupID- for edit
     *            group)
     * @param groupField
     *            represented group field
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrEditGroupSubmit(final String groupID, final ArrayList<HashMap<String, String>> groupField, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, ADDGROUP);
                JSONObject taskData = new JSONObject();
                try {

                    if (!groupID.equals("0")) {
                        taskData.put(UNIQUEID, groupID);
                    }
                    taskData.put(FIELDS, "0");
                    for (HashMap<String, String> hashMap : groupField) {
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
     * This method used to get group field list.
     *
     * @param groupID
     *            represented group id (0- for new group,groupID- for edit
     *            group)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrEditGroupFieldList(final String groupID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, ADDGROUP);

                JSONObject taskData = new JSONObject();
                try {
                    if (!groupID.equals("0")) {
                        taskData.put(UNIQUEID, groupID);
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
     * This method used to add or edit group announcement.
     *
     * @param groupID
     *            represented group id
     * @param announcementID
     *            represented announcement id (0- for new
     *            announcement,announcementID- for edit announcement)
     * @param title
     *            represented announcement title
     * @param message
     *            represented announcement message
     * @param isAllowMemberToUploadFile
     *            represented allow member to upload file
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrEditGroupAnnouncement(final String groupID, final String announcementID, final String title, final String message, final String isAllowMemberToUploadFile,
                                           final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, ADDANNOUNCEMENT);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    if (!announcementID.equals("0")) {
                        taskData.put(ANNOUNCEMENTID, announcementID);
                    }
                    taskData.put(TITLE, title);
                    taskData.put(MESSAGE, message);
                    taskData.put(FILE, isAllowMemberToUploadFile);

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
     * This method used to remove group announcement.
     *
     * @param groupID
     *            represented group id
     * @param announcementID
     *            represented group announcement id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeAnnouncement(final String groupID, final String announcementID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, DELETEANNOUNCEMENT);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(ANNOUNCEMENTID, announcementID);

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
     * This method used to add or edit group discussion reply.
     *
     * @param discussionID
     *            represented discussion id (0- for new discussion
     *            reply,discussionID- for edit discussion reply)
     * @param message
     *            represented discussion reply message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrEditDiscussionReplies(final String discussionID, final String discussionReplieID, final String message, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, ADDDISCUSSIONREPLY);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, discussionID);
                    if (!discussionReplieID.equals("0")) {
                        taskData.put(WALLID, discussionReplieID);
                    }
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
     * This method used to remove group discussion.
     *
     * @param groupID
     *            represented group id
     * @param discussionID
     *            represented discussion id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeDiscussion(final String groupID, final String discussionID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, DELETEDISCUSSION);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(DISCUSSIONID, discussionID);
                    taskData.put(TYPE, DISCUSSION);

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
     * This method used to remove group discussion reply.
     *
     * @param groupID
     *            represented group id
     * @param discussionID
     *            represented discussion id
     * @param replayID
     *            represented discussion reply id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeDiscussionReplay(final String groupID, final String discussionID, final String replayID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, DELETEDISCUSSION);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(DISCUSSIONID, discussionID);
                    taskData.put(WALLID, replayID);
                    taskData.put(TYPE, REPLY);

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
     * This method used to add or edit group discussion .
     *
     * @param groupID
     *            represented group id
     * @param discussionID
     *            represented discussion id (0- for new discussion
     *            ,discussionID- for edit discussion)
     * @param title
     *            represented discussion title
     * @param message
     *            represented discussion message
     * @param isAllowMemberToUploadFile
     *            represented allow member to upload file
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addOrEditGroupDiscussion(final String groupID, final String discussionID, final String title, final String message, final String isAllowMemberToUploadFile, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, ADDDISCUSSION);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    if (!discussionID.equals("0")) {
                        taskData.put(DISCUSSIONID, discussionID);
                    }
                    taskData.put(TITLE, title);
                    taskData.put(MESSAGE, message);
                    taskData.put(FILE, isAllowMemberToUploadFile);

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
     * This method used to lock or unlock group discussion.
     *
     * @param groupID
     *            represented group id
     * @param discussionID
     *            represented group discussion id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void lockUnlockDiscussion(final String groupID, final String discussionID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, LOCKDISCUSSION);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(DISCUSSIONID, discussionID);
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
     * This method used to get group announcement file list.
     *
     * @param announcementID
     *            represented group announcement id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getAnouncementFileList(final String announcementID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, FILES);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, ANNOUNCEMENT);
                        taskData.put(UNIQUEID, announcementID);
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
     * This method used to get group discussion file list.
     *
     * @param discussionID
     *            represented group discussion id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getDiscussionFileList(final String discussionID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, FILES);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, DISCUSSION);
                        taskData.put(UNIQUEID, discussionID);
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
     * This method used to get group file list.
     *
     * @param groupID
     *            represented group id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getGroupFileList(final String groupID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, FILES);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(TYPE, GROUP);
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
     * This method is used to download file
     *
     * @param fileID
     *            represented file id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void downloadFile(final String fileID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, FILES);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(TYPE, HITS);
                    taskData.put(UNIQUEID, fileID);

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
     * This method is used to remove file
     *
     * @param fileID
     *            represented file id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeFile(final String fileID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, FILES);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(TYPE, DELETE);
                    taskData.put(UNIQUEID, fileID);

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
     * This method used to get group discussion reply list.
     *
     * @param discussionID
     *            represented group discussion id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getDiscussionReplayList(final String discussionID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, GROUP);
                    iw.addWSParam(EXTTASK, DISCUSSIONDETAIL);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(UNIQUEID, discussionID);
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
     * This method used to invite group friend.
     *
     * @param groupID
     *            represented group id
     * @param userIDS
     *            represented friends id with (,) separated
     * @param message
     *            represented invitation message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void inviteFriend(final String groupID, final String userIDS, final String message, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, INVITE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    if (message != null) {
                        taskData.put(MESSAGE, message);
                    }
                    taskData.put(FRIENDS, userIDS);
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
     * This method used to join group.
     *
     * @param userID
     *            represented used id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void joinGroup(final String userID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, JOIN);

                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, userID);
                } catch (JSONException e) {
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
     * This method used to leave group.
     *
     * @param userID
     *            represented user id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void leaveGroup(final String userID, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, LEAVE);

                JSONObject taskData = new JSONObject();

                try {
                    taskData.put(UNIQUEID, userID);
                } catch (JSONException e) {
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
     * This method used to get group invite friend list.
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
                    iw.addWSParam(EXTVIEW, GROUP);
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
     * This method is used to add group wall
     *
     * @param groupID
     *            represented group id
     * @param message
     *            represented wall message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void addWallPost(final String groupID, final String message, final String voiceFilePath, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            IjoomerWebService iw = new IjoomerWebService();

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, ADDWALL);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, groupID);
                    taskData.put(MESSAGE, message);
                    taskData.put(COMMENT, "0");
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);
                if (voiceFilePath != null) {
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
                target.onCallComplete(getResponseCode(), getErrorMessage(), result, iw.getJsonObject());
            }
        }.execute();
    }

    /**
     * This method is used to upload discussion file
     *
     * @param filePath
     *            represented file path
     * @param discussionID
     *            represented discussion id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void uploadDiscussionFile(final String filePath, final String discussionID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, UPLOADFILE);
                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, discussionID);
                    taskData.put(TYPE, DISCUSSION);
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);
                if (filePath != null) {
                    iw.WSCall(FILES, filePath, new ProgressListener() {

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
     * This method is used to upload Announcement File
     *
     * @param filePath
     *            represented file path
     * @param announcementID
     *            represented announcement id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void uploadAnnouncementFile(final String filePath, final String announcementID, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, GROUP);
                iw.addWSParam(EXTTASK, UPLOADFILE);
                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, announcementID);
                    taskData.put(TYPE, ANNOUNCEMENT);
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);
                if (filePath != null) {
                    iw.WSCall(FILES, filePath, new ProgressListener() {

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

}
