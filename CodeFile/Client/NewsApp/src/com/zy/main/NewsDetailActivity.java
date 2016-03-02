package com.zy.main;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.zy.service.SyncHttp;

import edu.njupt.zhb.main.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class NewsDetailActivity extends Activity{
	
	String newsID = "";
	String userID = "";
	
	private ArrayList<HashMap<String, Object>> mNewsData; // �洢������Ϣ�����ݼ���
	private ArrayList<HashMap<String, Object>> commentData; // �洢������Ϣ�����ݼ���
	
    private TextView newsTitle;
    private TextView newsBody;
    private Button commentDetail;
    private LinearLayout mNewsReplyEditLayout;	//���Żظ��Ĳ���
    private LinearLayout mNewsReplyImgLayout;	//����ͼƬ�ظ��Ĳ���
    private EditText mNewsReplyEditText;		//���Żظ����ı���
    private boolean keyboardShow;				//�����Ƿ���ʾ
    private Button ReplyBtn;
    
    String titleTest = "";
    
    int commentNumber = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsdetails_layout);
		
		newsTitle = (TextView)findViewById(R.id.newsTitle);
		newsBody = (TextView)findViewById(R.id.newsBody);
		commentDetail = (Button)findViewById(R.id.newsdetail_titlebar_comments);
		ReplyBtn = (Button)findViewById(R.id.news_reply_post);
		newsBody.setMovementMethod(ScrollingMovementMethod.getInstance());
		ImageButton newsReplyImgBtn = (ImageButton) findViewById(R.id.news_reply_img_btn);
		mNewsReplyEditLayout = (LinearLayout) findViewById(R.id.news_reply_edit_layout);
		mNewsReplyImgLayout = (LinearLayout) findViewById(R.id.news_reply_img_layout);
		mNewsReplyEditText = (EditText) findViewById(R.id.news_reply_edittext);
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		newsID = bundle.getString("newsID");
		userID = bundle.getString("userID");
		
		mNewsData = new ArrayList<HashMap<String, Object>>();
		commentData = new ArrayList<HashMap<String, Object>>();
		getDetailNewsAsyncTack upImageAsync = new getDetailNewsAsyncTack();
		upImageAsync.execute(newsID,mNewsData);
		
		commentDetail.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(commentNumber>0){
					Intent intent = new Intent(NewsDetailActivity.this,
							CommentActivity.class);
					intent.putExtra("commentData", commentData);
					startActivity(intent);
				}
			}
		});
		
		newsReplyImgBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				InputMethodManager m = (InputMethodManager) mNewsReplyEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				mNewsReplyEditLayout.setVisibility(View.VISIBLE);
				mNewsReplyImgLayout.setVisibility(View.GONE);
				mNewsReplyEditText.requestFocus();
				//��ʾ���뷨
				m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
				keyboardShow = true;
			}
		});
		
		ReplyBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				InputMethodManager m = (InputMethodManager) mNewsReplyEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
				String str = mNewsReplyEditText.getText().toString();
				if(str.equals("")){
					Toast.makeText(NewsDetailActivity.this, "����Ϊ��",
							Toast.LENGTH_SHORT).show();
				}
				else {
					/*
					 * getDetailNewsAsyncTack upImageAsync = new getDetailNewsAsyncTack();
		upImageAsync.execute(newsID,mNewsData);
					 * */
					addCommentAsyncTack aCommentAsync = new addCommentAsyncTack();
					aCommentAsync.execute(str);
					mNewsReplyEditLayout.setVisibility(View.GONE);
					mNewsReplyImgLayout.setVisibility(View.VISIBLE);
				}
				/*InputMethodManager m = (InputMethodManager) mNewsReplyEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				mNewsReplyEditLayout.setVisibility(View.VISIBLE);
				mNewsReplyImgLayout.setVisibility(View.GONE);
				mNewsReplyEditText.requestFocus();
				//��ʾ���뷨
				m.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
				keyboardShow = true;*/
				
			}
		});
	}
	
	/**
	 * ��ȡָ�����͵������б�
	 * 
	 * @param cid
	 * @return
	 */
	private int getNewsByID(int newsID, List<HashMap<String, Object>> newsList) {
		
		// ����:http://10.0.2.2:8080/web/getSpecifyCategoryNews
		// wifi��������192.168.220.1
		String url = "http://192.168.1.107:8080/newsWeb/GetNewsByNewsID";
		String params = "newsID=" + newsID;
		SyncHttp syncHttp = new SyncHttp();

		try {
			// ͨ��HttpЭ�鷢��Get���󣬷����ַ���
			String retStr = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			int retCode = jsonObject.getInt("ret");
			if (retCode == 0) {
				JSONObject dataObj = jsonObject.getJSONObject("data");
				// ��ȡ������Ŀ
				int totalNum = dataObj.getInt("totalnum");
				if (totalNum > 0) {
					// ��ȡ�������ż���
					JSONArray newslistArray = dataObj.getJSONArray("newsList");
					// ����JSON��ʽ������������ӵ����ݼ��ϵ���
					for (int i = 0; i < newslistArray.length(); i++) {
						JSONObject newsObject = (JSONObject) newslistArray
								.opt(i);
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						hashMap.put("title",newsObject.getString("ntitle"));
						titleTest = newsObject.getString("ntitle");
						hashMap.put("body",newsObject.getString("body"));
						hashMap.put("createTime",newsObject.getString("ncreateTime"));
						newsList.add(hashMap);
					}
					return 0;
				} else {
					// ��һ�μ��������б�
					return 1;
				}
			} else {
				return 3; // ��������ʧ��
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 3; // ��������ʧ��
		}
	}
	
	private class getDetailNewsAsyncTack extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			getNewsByID(Integer.valueOf(newsID), mNewsData);
			return 1;
		}
	
		@Override
		protected void onPostExecute(Integer result) {
			String s1 = (String) mNewsData.get(0).get("title");
			String s2 = (String) mNewsData.get(0).get("body");
			newsTitle.setText(s1);
			newsBody.setText(s2 + "\n" + "\n"+ "\n"+ "\n");
			getCommentAsyncTack getComment = new getCommentAsyncTack();
			getComment.execute(newsID,commentData);
		}
	}
	
private int getCommentByNewsID(int newsID, List<HashMap<String, Object>> commentList) {
		
		// ����:http://10.0.2.2:8080/web/getSpecifyCategoryNews
		// wifi��������192.168.220.1
		String url = "http://192.168.1.107:8080/newsWeb/GetCommentServlet";
		String params = "newsID=" + newsID;
		SyncHttp syncHttp = new SyncHttp();

		try {
			// ͨ��HttpЭ�鷢��Get���󣬷����ַ���
			String retStr = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			int retCode = jsonObject.getInt("ret");
			if (retCode == 0) {
				JSONObject dataObj = jsonObject.getJSONObject("data");
				// ��ȡ������Ŀ
				int totalNum = dataObj.getInt("totalnum");
				commentNumber = totalNum;
				if (totalNum > 0) {
					// ��ȡ�������ż���
					JSONArray commentlistArray = dataObj.getJSONArray("commentList");
					// ����JSON��ʽ������������ӵ����ݼ��ϵ���
					for (int i = 0; i < commentlistArray.length(); i++) {
						JSONObject commentObject = (JSONObject) commentlistArray.opt(i);
						HashMap<String, Object> hashMap = new HashMap<String, Object>();
						hashMap.put("commentID",commentObject.getString("commentID"));
						hashMap.put("content",commentObject.getString("content"));
						hashMap.put("createTime",commentObject.getString("createTime"));
						hashMap.put("userID",commentObject.getString("userID"));
						hashMap.put("newsID",commentObject.getString("newsID"));
						
						commentList.add(hashMap);
					}
					return 0;
				} else {
					// ��һ�μ��������б�
					return 1;
				}
			} else {
				return 3; // ��������ʧ��
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 3; // ��������ʧ��
		}
	}
	
	private class getCommentAsyncTack extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			return getCommentByNewsID(Integer.valueOf(newsID), commentData);
		}
	
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 0){
				commentDetail.setText(String.valueOf(commentNumber)+"����");
			}
		}
	}
	
	private int addComment(int newsID, int userID, String content) {
		
		// ����:http://10.0.2.2:8080/web/getSpecifyCategoryNews
		// wifi��������192.168.220.1
		String url = "http://192.168.1.107:8080/newsWeb/AddCommentServlet";
		String params = "newsID=" + newsID +"&userID="+userID + "&content="+content;
		Log.i("response", params);
		SyncHttp syncHttp = new SyncHttp();

		try {
			// ͨ��HttpЭ�鷢��Get���󣬷����ַ���
			String retStr = syncHttp.httpGet(url, params);
			JSONObject jsonObject = new JSONObject(retStr);
			int retCode = jsonObject.getInt("ret");
			if (retCode == 0) {
				return 0;
			} else {
				return 1; // ��������ʧ��
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 1; // ��������ʧ��
		}
	}
	
	private class addCommentAsyncTack extends AsyncTask<Object, Integer, Integer> {

		@Override
		protected Integer doInBackground(Object... params) {
			return addComment(Integer.valueOf(newsID),Integer.valueOf(userID), (String)params[0]);
		}
	
		@Override
		protected void onPostExecute(Integer result) {
			if(result == 0){
				showTip("�ϴ����۳ɹ�������");
				getCommentAsyncTack getComment = new getCommentAsyncTack();
				getComment.execute(newsID,commentData);
			}else{
				showTip("�ϴ�����ʧ�ܣ�����");
			}
		}
	}

	private void showTip(String msg)
	{
		Toast.makeText(NewsDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
	}
	
}
