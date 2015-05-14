package com.example.imageloader.load;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.imageloader.R;

/**
 * 参考图片工具测试类
 * @author miaowei
 *
 */
public class ImageLoaderToolsTest extends Activity{
	/**
	 * 图片URL
	 */
	String picpath = "http://192.168.85.29:6555/roadcamera-web/upload/a5f5fe54s4fd54f5efef545.jpg";
	private ImageView imageView;
	ImageLoaderTools imageLoaderTools;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_loader);
		imageLoaderTools = new ImageLoaderTools(this);
		imageView = (ImageView)findViewById(R.id.imageView);
		Bitmap bitmap = imageLoaderTools.imageLoad(picpath, new ImageLoaderTools.Callback() {
		   
		        @Override
		       public void imageloaded(String picPath, Bitmap bitmap) {
		           if (bitmap == null) {
		               imageView.setImageResource(R.drawable.ic_launcher);
		           } else {
		               imageView.setImageBitmap(bitmap);
		           }
		       }
		   });
		  
		   if (bitmap == null) {
		       imageView.setImageResource(R.drawable.ic_launcher);
		   } else {
		       imageView.setImageBitmap(bitmap);
		   }
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		imageLoaderTools.quit();
	}
}
