package com.example.imageloader.load;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * http工具类
 * @author miaowei
 *
 */
public class HttpTools {

	public static final int METHOD_GET = 1;
	
	private Context mContext;
	
	public HttpTools(Context context) {
	
		mContext = context;
	}
	
	/**
	 * 
	 * 返回byte[]数组
	 * @param path 请求url
	 * @param other
	 * @param value 请求类型 get或post
	 * @return
	 */
	public byte[] getByte(String path, String other, int value) {
		
		return null;

	}
	/**
	 * 返回输入流
	 * @param path 请求url
	 * @param other 
	 * @param value 请求类型 get或post
	 * @return
	 */
	public Bitmap getStream(String path, String other, int value) {
	
		Bitmap bitmap = null;
		InputStream isInputStream = null;
		HttpURLConnection con = null;
		try {
			URL mImageUrl = new URL(path);
			con = (HttpURLConnection) mImageUrl.openConnection();
			con.setConnectTimeout(10 * 1000);
			con.setReadTimeout(10 * 1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			isInputStream = con.getInputStream();
			bitmap = BitmapFactory.decodeStream(con.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				con.disconnect();
			}
		}
		return bitmap;

	}
}
