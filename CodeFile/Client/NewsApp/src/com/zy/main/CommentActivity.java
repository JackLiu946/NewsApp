package com.zy.main;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.njupt.zhb.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class CommentActivity extends Activity {
	private ArrayList<HashMap<String, Object>> commentData; // �洢������Ϣ�����ݼ���
	private TextView textView;

	@SuppressWarnings("unchecked")
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_detail_layout);

		commentData = new ArrayList<HashMap<String, Object>>();
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();		
		Serializable serializable = bundle.getSerializable("commentData");
		commentData = (ArrayList<HashMap<String, Object>>) serializable;
		//showTip(commentData.toString());
		
		textView = (TextView)findViewById(R.id.commentBody);
		textView.setMovementMethod(ScrollingMovementMethod.getInstance());
		String commentResult = "";
		// ����JSON��ʽ������������ӵ����ݼ��ϵ���
		for (int i = 0; i < commentData.size(); i++) {
			
			String uID = (String) commentData.get(i).get("userID");
			String uContent = (String) commentData.get(i).get("content");
			String uCreateTime = (String) commentData.get(i).get("createTime");
			commentResult = commentResult +  "\n\n�û�ID:"+uID+"\n"+"ʱ��:"+uCreateTime+"\n"+"��������:"+uContent;
		}
		textView.setText(commentResult);
	}
	
	private void showTip(String msg)
	{
		Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_SHORT).show();
	}
}
