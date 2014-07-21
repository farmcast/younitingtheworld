package com.ijoomer.oauth;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

import com.Facebook.entities.Profile;
import com.ijoomer.common.classes.IjoomerResponseValidator;
import com.ijoomer.custom.interfaces.IjoomerSharedPreferences;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;
import com.smart.framework.SmartActivity;

/**
 * This Class Contains All Methods Related To IjoomerOauth.
 * 
 * @author tasol
 * 
 */
public final class IjoomerOauth extends IjoomerResponseValidator implements IjoomerSharedPreferences {

	private static Context context;

	private final String LOGIN = "login";
	private final String USERNAME = "username";
	private final String PASSWORD = "password";
	private final String LAT = "lat";
	private final String LONG = "long";
	private final String DEVICETOKEN = "devicetoken";
	private final String TYPE = "type";
	private final String ANDROID = "android";
	private final String LOGOUT = "logout";
	private final String FBLOGIN = "fblogin";
	private final String NAME = "name";
	private final String EMAIL = "email";
	private final String BIGPIC = "bigpic";
	private final String REGOPT = "regopt";
	private final String FBID = "fbid";
	private final String RESETPASSWORD = "resetPassword";
	private final String STEP = "step";
	private final String RETRIVEUSERNAME = "retriveUsername";
	private final String TOKEN = "token";
	private final String CRYPT = "crypt";
	private String resetPasswordCrypt = "";
	private String resetPasswordUserId = "";
	

	/**
	 * This method used to get password crypt used for password reset.
	 * 
	 * @return
	 */
	public String getResetPasswordCrypt() {
		return resetPasswordCrypt;
	}

	/**
	 * This method used to get user id used for password reset.
	 * 
	 * @return
	 */
	public String getResetPasswordUserId() {
		return resetPasswordUserId;
	}

	public static IjoomerOauth ijoomerOauth = null;

	/**
	 * Constructor.
	 * 
	 * @param mContext
	 */
	private IjoomerOauth(Context mContext) {
		super(mContext);
	}

	/**
	 * This method used to get class instance.
	 * 
	 * @param mContext
	 * @return
	 */
	public static IjoomerOauth getInstance(Context mContext) {

		if (ijoomerOauth == null) {
			ijoomerOauth = new IjoomerOauth(mContext);
		}
		context = mContext;
		return ijoomerOauth;
	}

	/**
	 * This method used to authenticate user.
	 * 
	 * @param userName
	 *            represented user name
	 * @param passWord
	 *            represented password
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void authenticateUser(final String userName, final String passWord, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(TASK, LOGIN);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(USERNAME, userName);
					taskData.put(PASSWORD, passWord);
					taskData.put(LAT, getLatitude());
					taskData.put(LONG, getLongitude());
					taskData.put(DEVICETOKEN, getDeviceUDID(context));
					taskData.put(TYPE, ANDROID);
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
						JSONObject loginParams = new JSONObject();
						loginParams.put(TASK, LOGIN);
						loginParams.put(TASKDATA, taskData);
						((SmartActivity) context).getSmartApplication().writeSharedPreferences(SP_LOGIN_REQ_OBJECT, loginParams.toString());
					} catch (Exception e) {
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();

	}

	/**
	 * This method used to logout from application.
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void logout(final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(TASK, LOGOUT);
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
//					new IjoomerCaching(context).resetDataBase();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}

	/**
	 * This method used to authenticate existing site member with facebook
	 * connect.
	 * 
	 * @param profile
	 *            represented facebook user object
	 * @param userName
	 *            represented user name
	 * @param password
	 *            represented password
	 * @param target
	 *            represented {@link WebCallListener}
	 */
    public void authenticateUserWithFacebook(final Profile profile, final String userName, final String password, final WebCallListener target) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(TASK, FBLOGIN);
                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(NAME, profile.getName());
                    taskData.put(USERNAME, userName);
                    taskData.put(PASSWORD, password);
                    taskData.put(EMAIL, profile.getEmail());
                    taskData.put(BIGPIC, profile.getPicture());
                    taskData.put(FBID, profile.getId());
                    taskData.put(REGOPT, "1");
                    taskData.put(TYPE, ANDROID);
                    taskData.put(DEVICETOKEN, getDeviceUDID(context));
                    taskData.put(LAT, getLatitude());
                    taskData.put(LONG, getLongitude());
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
                        JSONObject loginParams = new JSONObject();
                        loginParams.put(TASK, FBLOGIN);
                        loginParams.put(TASKDATA, taskData);
                        ((SmartActivity) context).getSmartApplication().writeSharedPreferences(SP_LOGIN_REQ_OBJECT, loginParams.toString());
                    } catch (Exception e) {
                    }
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
            }
        }.execute();
    }

	/**
	 * This method used to authenticate new user with facebook connect.
	 * 
	 * @param profile
	 *            represented facebook user object
	 * @param userName
	 *            represented user name
	 * @param target
	 *            represented {@link WebCallListener}
	 */
    public void authenticateUserWithFacebook(final Profile profile, final String userName, final WebCallListener target) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(TASK, FBLOGIN);
                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(NAME, profile.getName());
                    taskData.put(USERNAME, userName);
                    taskData.put(PASSWORD, profile.getId());
                    taskData.put(EMAIL, profile.getEmail());
                    taskData.put(BIGPIC, profile.getPicture());
                    taskData.put(FBID, profile.getId());
                    taskData.put(REGOPT, "2");
                    taskData.put(TYPE, ANDROID);
                    taskData.put(DEVICETOKEN, getDeviceUDID(context));
                    taskData.put(LAT, getLatitude());
                    taskData.put(LONG, getLongitude());

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
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
            }
        }.execute();

    }

	public void autoLogin(final String reqObject, final WebCallListener target) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				try {
					JSONObject taskData = new JSONObject(reqObject);
					iw.addWSParam(taskData);
				} catch (Throwable e) {
				}
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
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}

	/**
	 * This method used to authenticate user facebook connect.
	 * 
	 * @param profile
	 *            represented facebook response data
	 * @param target
	 *            represented {@link WebCallListener}
	 */

    public void authenticateUserWithFacebook(final Profile profile, final WebCallListener target) {

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                IjoomerWebService iw = new IjoomerWebService();
                iw.reset();
                iw.addWSParam(TASK, FBLOGIN);
                JSONObject taskData = new JSONObject();
                try {
                    taskData.put(NAME, profile.getName());
                    taskData.put(USERNAME, profile.getUsername());
                    taskData.put(PASSWORD, profile.getId());
                    taskData.put(DEVICETOKEN, getDeviceUDID(context));
                    taskData.put(EMAIL, profile.getEmail());
                    taskData.put(BIGPIC, profile.getPicture());
                    taskData.put(REGOPT, "0");
                    taskData.put(FBID, profile.getId());
                    taskData.put(LAT, getLatitude());
                    taskData.put(LONG, getLongitude());
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
                        JSONObject loginParams = new JSONObject();
                        loginParams.put(TASK, FBLOGIN);
                        loginParams.put(TASKDATA, taskData);
                        ((SmartActivity) context).getSmartApplication().writeSharedPreferences(SP_LOGIN_REQ_OBJECT, loginParams.toString());
                    } catch (Exception e) {
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                target.onProgressUpdate(100);
                target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
            }
        }.execute();

    }

	/**
	 * This method used to get forgot user password step 1.
	 * 
	 * @param email
	 *            represented user email
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void forgetPasswordStep1(final String email, final WebCallListener target) {
		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(TASK, RESETPASSWORD);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(STEP, "1");
					taskData.put(EMAIL, email);
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
				validateResponse(iw.getJsonObject());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();

	}

	/**
	 * This method used to get forgot user password step 2.
	 * 
	 * @param userName
	 *            represented user name
	 * @param token
	 *            represented user token (getting on mail in the step 1)
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void forgetPasswordStep2(final String userName, final String token, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(TASK, RESETPASSWORD);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(STEP, "2");
					taskData.put(USERNAME, userName);
					taskData.put(TOKEN, token);
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
						resetPasswordCrypt = iw.getJsonObject().getString(CRYPT);
						resetPasswordUserId = iw.getJsonObject().getString("userid");
					} catch (Throwable e) {
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}

		}.execute();
	}

	/**
	 * This method used to get forgot user password step 3.
	 * 
	 * @param newPassword
	 *            represented user new password
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void forgetPasswordStep3(final String newPassword, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(TASK, RESETPASSWORD);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(STEP, "3");
					taskData.put(CRYPT, getResetPasswordCrypt());
					taskData.put("userid", getResetPasswordUserId());
					taskData.put(PASSWORD, newPassword);
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

				validateResponse(iw.getJsonObject());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();
	}

	/**
	 * This method used to get forgot user name.
	 * 
	 * @param email
	 *            represented user email
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void forgetUserName(final String email, final WebCallListener target) {

		new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();
				iw.addWSParam(TASK, RETRIVEUSERNAME);
				JSONObject taskData = new JSONObject();
				try {
					taskData.put(EMAIL, email);
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
				validateResponse(iw.getJsonObject());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				target.onProgressUpdate(100);
				target.onCallComplete(getResponseCode(), getErrorMessage(), null, null);
			}
		}.execute();

	}
}
