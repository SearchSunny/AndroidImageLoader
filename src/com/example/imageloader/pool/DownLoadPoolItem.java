/**
 * 
 */
package com.example.imageloader.pool;

/**
 * @author hw
 *预加载池管理器的队列项
 */
public class DownLoadPoolItem {
	/**
	 * 下载地址
	 */
	private String url;
	/**
	 * 下载类型
	 */
	private int type;
	/**
	 * 是否已经下载完成
	 */
	private boolean over;
	public DownLoadPoolItem(String url,int type,boolean over){
		this.url = url;
		this.type = type;
		this.over = over;
	}
	
	public void setUrl(String url){
		this.url = url;
	}
	
	public void setType(int type){
		this.type = type;
	}
	/**
	 * 是否已经下载完成
	 * @param over
	 */
	public void setIsOver(boolean over){
		this.over = over;
	}
	
	public String getUrl(){return url;}
	public int getType(){return type;}
	public boolean getIsOver(){return over;}
}
