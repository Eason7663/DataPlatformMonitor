package com.gw.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import com.gw.utils.HttpRequest;
public class MonitorApp {
	//LOGGER
	private static Logger logger = Logger.getLogger(MonitorApp.class);
	private List<String> listIPList;
	private static boolean logPrint;
	
	public List<String> getListIPList() {
		return listIPList;
	}

	public void setListIPList(List<String> listIPList) {
		this.listIPList = listIPList;
	}
	private static String url = "http://10.15.107.77:12345/jso-web/JsoRequest";
	private static String param = "filename=jso-365-news.jar&funname=gw.sh.func.News365NewsRequest&arg=%7B%22obj%22%3A%22SZ002064.stk%22%2C%22type%22%3A%221%22%7D&port=7000&username=kftest&charset=UTF-8&ip=";

	public MonitorApp(String strConf) {
		// TODO Auto-generated constructor stub
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(strConf));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] arr = prop.getProperty("IP").split(",");
		this.listIPList = Arrays.asList(arr);
		//日志开关打印具体返回信息
		MonitorApp.logPrint = Boolean.parseBoolean(prop.getProperty("DEBUG_ON"));
		
	}
	
	/**
	 * @Description 从配置文件中读出将包含IP地址的String，再转成List<String>
	 * @param strConf
	 * @return
	 * @return_type List<String>
	 * @author Eason
	 * @date 2017年8月25日 下午4:19:39  
	 */
	public List<String> creatList(String strConf) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(strConf));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] arr = prop.getProperty("IP").split(",");
		return Arrays.asList(arr);
	}
	
	/**
	 * @Description oboslete
	 * @param deErrlogPath
	 * @return_type void
	 * @author Eason
	 * @date 2017年8月25日 下午5:04:48  
	 */
	public void initEnv(String deErrlogPath){
		//删除error文件如果存在
        File file=new File(deErrlogPath);
        if(file.exists()&&file.isFile()){
        	try {
            	if (file.delete()) {
    				logger.info("error.log 删除成功！");
    			}
			} catch (SecurityException e) {
				// TODO: handle exception
				e.printStackTrace();
			}


        }
	}

	public static void main(String[] args) {
		try {
			MonitorApp mApp = new MonitorApp(".//conf//monitor.properties");
			//清理环境
//			mApp.initEnv(".//logs//error.log");

			for (String stringIP : mApp.getListIPList()) {
				StringBuilder paramBuilder = new StringBuilder(param);
				paramBuilder.append(stringIP);
				try {
					String result = HttpRequest.sendPost(url, paramBuilder.toString());
					if (result.indexOf("共耗时")==-1||result.indexOf("结果集")==-1) {
						logger.info("有服务器(stringIP)未返回数据->error.log");
						logger.error(stringIP);
					}else {
						if (logPrint) {
							logger.info(stringIP + ":返回数据为：\n" + result);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					logger.error(stringIP);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			
		}


	}

}
