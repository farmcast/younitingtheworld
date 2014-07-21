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
 * This Class Contains All Method Related To JomMessageDataProvider.
 *
 * @author tasol
 *
 */
public class JomMessageDataProvider extends IjoomerPagingProvider {

    private Context mContext;

    private final String MESSAGE = "message";
    private final String BODY = "body";
    private final String REMOVE = "remove";
    private final String FULL = "full";

    private final String CONVERSATION = "conversation";
    private final String DETAIL = "detail";
    private final String WRITE = "write";
    private final String UNIQUEID = "uniqueID";

    private final String SUBJECT = "subject";
    private boolean isCalling = false;

    /**
     * Constructor
     *
     * @param context
     *            represented {@link Context}
     */
    public JomMessageDataProvider(Context mContext) {
        super(mContext);
        this.mContext = mContext;
    }

    /**
     * This method used to check provider execute any request call.
     *
     * @return {@link boolean}
     */
    public boolean isCalling() {
        return isCalling;
    }

    /**
     * This method is used to get message list
     *
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getMessageList(final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, MESSAGE);
                    iw.addWSParam(EXTTASK, CONVERSATION);

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
     * This method is used to get Message Detail List
     *
     * @param messageID
     *            represented message id
     * @param userID
     *            represented user id
     * @param target
     *            represented {@link WebCallListener}
     */
    public void getMessageDetailsList(final String messageID, final String userID, final WebCallListener target) {
        if (hasNextPage()) {
            isCalling = true;
            new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

                @Override
                protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                    IjoomerWebService iw = new IjoomerWebService();
                    iw.reset();
                    iw.addWSParam(EXTNAME, JOMSOCIAL);
                    iw.addWSParam(EXTVIEW, MESSAGE);
                    iw.addWSParam(EXTTASK, DETAIL);

                    JSONObject taskData = new JSONObject();
                    try {
                        taskData.put(UNIQUEID, messageID);
                        taskData.put(USERID, userID);
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
     * This method is used to send message
     *
     * @param userIDS
     *            represented one or more id of message receivers
     * @param body
     *            represented message
     * @param subject
     *            represented subject of message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void sendMessage(final String userIDS, final String body, final String subject, final String voiceFilePath, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, MESSAGE);
                iw.addWSParam(EXTTASK, WRITE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(BODY, body);
                    taskData.put(SUBJECT, subject);
                    taskData.put(USERID, userIDS);
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);

                if (voiceFilePath != null) {
                    iw.WSCall(voiceFilePath, new ProgressListener() {

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
     * This method is used to reply a message
     *
     * @param messageID
     *            represented message id
     * @param body
     *            represented reply message
     * @param target
     *            represented {@link WebCallListener}
     */
    public void replyMessage(final String messageID, final String body, final String voiceFilePath, final WebCallListener target) {

        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, MESSAGE);
                iw.addWSParam(EXTTASK, WRITE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(BODY, body);
                    taskData.put(UNIQUEID, messageID);
                } catch (Throwable e) {
                }
                iw.addWSParam(TASKDATA, taskData);

                if (voiceFilePath != null) {
                    iw.WSCall(voiceFilePath, new ProgressListener() {

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
     * This method is used to remove message
     *
     * @param messageID
     *            represented message id
     * @param entire
     *            represented entire (false- for alone message,true- whole
     *            conversion)
     * @param target
     *            represented {@link WebCallListener}
     */
    public void removeMessage(final String messageID, final boolean entire, final WebCallListener target) {
        new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

            @Override
            protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(EXTNAME, JOMSOCIAL);
                iw.addWSParam(EXTVIEW, MESSAGE);
                iw.addWSParam(EXTTASK, REMOVE);

                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(UNIQUEID, messageID);
                    taskData.put(FULL, entire ? "1" : "0");
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
