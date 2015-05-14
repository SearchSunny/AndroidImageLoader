package com.example.imageloader.load;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * 参考图片工具类
 * @author miaowei
 *
 */
public class ImageLoaderTools {

	/**
	 * HTTP请求
	 */
	 private HttpTools httptool;
	 
     private Context mContext;
 
     /**
      * 是否停止线程
      */
     private boolean isLoop = true;
 
     /**
      * 图片缓存
      */
     private HashMap<String, SoftReference<Bitmap>> mHashMap_caches;
 
     /**
      * 下载对列
      */
     private ArrayList<ImageLoadTask> maArrayList_taskQueue;
 
     private Handler mHandler = new Handler() {
         public void handleMessage(android.os.Message msg) {
             ImageLoadTask loadTask = (ImageLoadTask) msg.obj;
             loadTask.callback.imageloaded(loadTask.path, loadTask.bitmap);
         };
     };
 
    private Thread mThread = new Thread() {
 
         public void run() {
 
             while (isLoop) {
            	 //之前开启的线程在发现 maArrayList_taskQueue.size() > 0 后就进入下载逻辑
                while (maArrayList_taskQueue.size() > 0) {
 
                     try {
                         ImageLoadTask task = maArrayList_taskQueue.remove(0);
                         //下载类型
                         if (Constant.LOADPICTYPE == 1) {
                             byte[] bytes = httptool.getByte(task.path, null, HttpTools.METHOD_GET);
                             
                             task.bitmap = BitmapFactory.decodeByteArray(bytes, 40, 40);
                             
                         } else if (Constant.LOADPICTYPE == 2) {
                             
                        	 task.bitmap = httptool.getStream(task.path, null, HttpTools.METHOD_GET);
                         }
 
                         if (task.bitmap != null) {
                             mHashMap_caches.put(task.path, new SoftReference<Bitmap>(task.bitmap));
                             //替换Url中非字母和非数字的字符
                             final String subUrl = task.path.replaceAll("[^\\w]", "");
                             //将bitmap保存到对应文件目录
                             BitMapTools.saveBitmap(subUrl, task.bitmap);
                             Message msg = Message.obtain();
                             msg.obj = task;
                             mHandler.sendMessage(msg);
                        }
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 }
                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
             }
 
         };
 
     };
 
     /**
      * 进行实例化过程中完成了网络工具 HttpTools初始化
      * 新建一个图片缓存 Map
      * 新建一个下载队列
      * 开启下载线程的操作
      * @param context
      */
     public ImageLoaderTools(Context context) {
         this.mContext = context;
         httptool = new HttpTools(context);
         mHashMap_caches = new HashMap<String, SoftReference<Bitmap>>();
         maArrayList_taskQueue = new ArrayList<ImageLoaderTools.ImageLoadTask>();
         mThread.start();
     }
 
     /**
      * 图片实例
      * @author miaowei
      *
      */
     private class ImageLoadTask {
        String path; //图片地址
        Bitmap bitmap; //图片
        Callback callback; //回调函数
    }

    public interface Callback {
        void imageloaded(String path, Bitmap bitmap);
    }

    /**
     * 停止线程
     */
    public void quit() {
        isLoop = false;
    }

   /**
    * 在执行 imageLoad() 方法加载图片时会首先去缓存 mHashMap_caches 中查找该图片是否已经被下载过，
    * 如果已经下载过则直接返回与之对应的 bitmap 资源，
    * 如果没有查找到则会往 maArrayList_taskQueue 中添加下载任务并唤醒对应的下载线程，
    * 之前开启的线程在发现 maArrayList_taskQueue.size() > 0 后就进入下载逻辑，
    * 下载完任务完成后将对应的图片资源加入缓存 mHashMap_caches 并更新 UI，下载线程执行 wait() 方法被挂起。
    * @param path
    * @param callback
    * @return
    */
   public Bitmap imageLoad(String path, Callback callback) {
        Bitmap bitmap = null;
        //1\首先去缓存 mHashMap_caches 中查找该图片是否已经被下载过
        //已经下载过则直接返回与之对应的 bitmap 资源
      
        if (mHashMap_caches.containsKey(path)) {
           bitmap = mHashMap_caches.get(path).get();
            if (bitmap == null) {
                mHashMap_caches.remove(path);
            } else {
                return bitmap;
           }
        }
       //2\到文件查找是否存在
      //替换Url中非字母和非数字的字符
        final String subUrl = path.replaceAll("[^\\w]", "");
       if (BitMapTools.isFileExists(subUrl) && BitMapTools.getFileSize(subUrl) > 0) {
		
    	   bitmap = BitMapTools.getBitmap(subUrl);
	   }
       if (bitmap != null) {
    	   
            return bitmap;
       }
        //3\没有查找到则会往 maArrayList_taskQueue 中添加下载任务并唤醒对应的下载线程
        ImageLoadTask task = new ImageLoadTask();
        task.path = path;
        task.callback = callback;
        maArrayList_taskQueue.add(task);

        //唤醒对应的下载线程
        synchronized (mThread) {
            mThread.notify();
        }

        return null;
    }
	
   
   
}
