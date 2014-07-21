package com.ijoomer.common.classes;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.ijoomer.caching.IjoomerCaching;
import com.ijoomer.common.classes.IjoomerResponseValidator;
import com.ijoomer.common.configuration.IjoomerApplicationConfiguration;
import com.ijoomer.weservice.IjoomerWebService;
import com.ijoomer.weservice.ProgressListener;
import com.ijoomer.weservice.WebCallListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This Class Contains All Method Related To IcmsArticleDetailDataProvider.
 * 
 * @author tasol
 * 
 */
public class IjoomerMenuDataProvider extends IjoomerResponseValidator {
	private Context mContext;
	private final String SUBMENU = "subMenu";
	private final String MENUID = "menuid";
	private ArrayList<HashMap<String, String>> data1;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            represented {@link Context}
	 */
	public IjoomerMenuDataProvider(Context context) {
		super(context);
		mContext = context;
	}

	/***
	 * This method is used to get article Detail
	 * 
	 * @param id
	 *            represented article id
	 * 
	 * @param target
	 *            represented {@link WebCallListener}
	 */
	public void getSubMenu(final String id, final WebCallListener target) {

		new AsyncTask<Void, Void, ArrayList<HashMap<String, String>>>() {

			@Override
			protected ArrayList<HashMap<String, String>> doInBackground(Void... params) {

				IjoomerWebService iw = new IjoomerWebService();
				iw.reset();

				iw.addWSParam(TASK, SUBMENU);

				JSONObject taskData = new JSONObject();
				try {
					taskData.put(MENUID, id);
				} catch (Throwable e) {
				}
				iw.addWSParam(TASKDATA, taskData);

				if (IjoomerApplicationConfiguration.isCachEnable) {
					data1 = new ArrayList<HashMap<String, String>>();
					try {

						data1 = new IjoomerCaching(mContext).getDataFromCache(SUBMENU, "select * from '" + SUBMENU + "' where reqObject='" + iw.getWSParameter() + "'");
						if (data1 != null && data1.size() > 0) {
							((Activity) mContext).runOnUiThread(new Runnable() {

								@Override
								public void run() {
									target.onProgressUpdate(100);
									target.onCallComplete(200, "", data1, null);

								}
							});

						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
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
					try {
						iw.getJsonObject().remove("code");
						if (IjoomerApplicationConfiguration.isCachEnable) {
							IjoomerCaching caching = new IjoomerCaching(mContext);
							caching.setReqObject(iw.getWSParameter().toString());
							return caching.cacheData(iw.getJsonObject(), false, SUBMENU);
						} else {
							return new IjoomerCaching(mContext).parseData(iw.getJsonObject());
						}
					} catch (Throwable e) {
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(ArrayList<HashMap<String, String>> result) {
				super.onPostExecute(result);

				target.onCallComplete(getResponseCode(), getErrorMessage(), result, null);
				target.onProgressUpdate(100);

			}
		}.execute();
	}
}
