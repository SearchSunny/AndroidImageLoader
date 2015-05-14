/**
 * 
 */
package com.example.imageloader.pool;

/**
 * @author hw
 *任务工作者
 */
public class Worker {

	/**
	 * 下载地址
	 */
	private String url;
	/**
	 * 下载类型
	 */
	private int type;
	/**
	 * 索引号
	 */
	private int index;
	/**
	 * 
	 * @param url 下载地址
	 * @param type 下载类型
	 * @param index 索引号
	 */
	public Worker(String url,int type,int index){
		this.url = url;
		this.type = type;
		this.index = index;
	}
	
	public void setUrl(String url){this.url = url;}
	public void setType(int type){this.type = type;}
	public void setIndex(int index){this.index = index;}
	
	public String getUrl(){return url;}
	public int getType(){return type;}
	public int getIndex(){return index;}
}
