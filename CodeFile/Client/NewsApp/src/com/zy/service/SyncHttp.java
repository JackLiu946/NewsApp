package com.zy.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.zy.model.Parameter;


public class SyncHttp {
	/**
	 * ͨ��Get��ʽ��������
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String httpGet(String url, String params) throws Exception {
		String response = null;	//������Ϣ
		//ƴ������URl
		if(null != params && !params.equals("")) {
			url += "?" + params;
		}
		
		int timeOutConnection = 3000;
		int timeOutSocket = 5000; 
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeOutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeOutSocket);
		
		//����HttpClientʵ��
		HttpClient httpClient = new DefaultHttpClient();
		//����GET����ʵ��
		Log.i("url", url);
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse httpResponse = httpClient.execute(httpGet);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if(statusCode == HttpStatus.SC_OK) {
				//��÷��ؽ��
				response = EntityUtils.toString(httpResponse.getEntity());
			}
			else{
				response = "������:" + statusCode;
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new Exception(e);
		}
		return response;
	}
	
	/**
	 * ͨ��post��ʽ��������
	 * @param url
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public String httpPost(String url, List<Parameter> params) throws Exception {
		String response  = null;
		int timeOutConnection = 3000;
		int timeOutSocket = 5000; 
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, timeOutConnection);
		HttpConnectionParams.setSoTimeout(httpParams, timeOutSocket);
		
		
		//����HttpClientʵ��
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
		if(params.size() > 0) {
			//����post�������
			httpPost.setEntity(new UrlEncodedFormEntity(buildNameValuePair(params), HTTP.UTF_8));
		}
		
		//ʹ��execute��������Http Post ���󣬲�����HttpResponse����
		HttpResponse httpResponse = httpClient.execute(httpPost);
		
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if(statusCode == HttpStatus.SC_OK) {
			//��÷��ؽ��
			response = EntityUtils.toString(httpResponse.getEntity());
		}
		else {
			response = "������:" + statusCode;
		}
		return response;
	}
		
	/**
	 * ��Paramster���ͼ���ת��ΪNameValuePair���ͼ���
	 * @param params
	 * @return
	 */
	private List<BasicNameValuePair> buildNameValuePair (List<Parameter> params) {
		List<BasicNameValuePair> result = new ArrayList<BasicNameValuePair>();
		for(Parameter param : params) {
			BasicNameValuePair pair = new BasicNameValuePair(param.getName(), param.getValue());
			result.add(pair);
		}
		return result;
	}
}
