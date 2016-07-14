package com.wscaller.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * HttpServletResponse 辅助类
 */
public class ResponseUtil {
	private static final Logger log = LoggerFactory.getLogger(ResponseUtil.class);

	//返回文本内容
	public static void sendText(HttpServletResponse response, String text) {
		send(response, "text/plain;charset=UTF-8", text);
	}

	//返回Json数据
	public static void sendJson(HttpServletResponse response, String text) {
		send(response, "application/json;charset=UTF-8", text);
	}

	//返回Xml
	public static void sendXml(HttpServletResponse response, String text) {
		send(response, "text/xml;charset=UTF-8", text);
	}

	//返回text/html
	public static void sendHtml(HttpServletResponse response, String text) {
		send(response, "text/html;charset=UTF-8", text);
	}
	
	public static void sendJsonZip(HttpServletResponse response, String text) {
		
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
        response.setHeader("Content-Encoding", "gzip");  
        // 设置字符集  
        response.setCharacterEncoding("UTF-8");  
		
		try {
			byte[] bts = compress(text.getBytes("UTF-8"));
	        // 设定输出流中内容长度  
	        //response.setContentLength(bts.length); 
			OutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(bts);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 返回内容
	 * @param response
	 * @param contentType
	 * @param text
	 */
	public static void send(HttpServletResponse response, String contentType, String text) {
		response.setContentType(contentType);
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		try {
			response.getWriter().write(text);
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 下载apk安装包
	 * @param response
	 * @param fileName
	 * @param byteArray
	 */
	public static void downloadApk(HttpServletResponse response, String fileName, byte[] byteArray) {
		try {
			System.out.println(fileName);
			// 防止IE缓存
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(fileName.getBytes("GBK"), "UTF-8"));//ISO8859-1 ??
			response.addHeader("Content-Length", "" + byteArray.length);
			response.setContentType("application/octet-stream;charset=GBK");
			response.setContentType("application/vnd.android.package-archive");  //安卓apk类型

			OutputStream out = new BufferedOutputStream(response
					.getOutputStream());
			out.write(byteArray);
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("下载文件错误");
			System.out.println(e.toString());
		}
	}
	/**
	 * 下载文件
	 * @param response
	 * @param fileName
	 * @param byteArray
	 */
	public static void download(HttpServletResponse response, String fileName, byte[] byteArray) {
		try {
			System.out.println(fileName);
			// 防止IE缓存
			response.setHeader("pragma", "no-cache");
			response.setHeader("cache-control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(fileName.getBytes("GBK"), "UTF-8"));//ISO8859-1 ??
			response.addHeader("Content-Length", "" + byteArray.length);
			response.setContentType("application/octet-stream;charset=GBK");

			OutputStream out = new BufferedOutputStream(response
					.getOutputStream());
			out.write(byteArray);
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("下载文件错误");
			System.out.println(e.toString());
		}
	}
	
	public static void img(HttpServletResponse response,
			byte[] byteArray) {
		try {
			response.setContentType("image/jpeg;charset=GBK");
			OutputStream out = new BufferedOutputStream(response
					.getOutputStream());
			out.write(byteArray);
			out.flush();
			out.close();
		} catch (Exception e) {
			log.error("文件错误");
			e.printStackTrace();
		}
	}
	
	/**
	 * Zip 压缩
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(byte[] data) throws IOException{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        // 压缩  
        long s = System.currentTimeMillis();
        GZIPOutputStream gos = new GZIPOutputStream(baos);
		gos.write(data, 0, data.length); 
		gos.finish();  
        byte[] output = baos.toByteArray();  
        baos.flush();  
        baos.close();  
        long e = System.currentTimeMillis();
        log.debug("压缩用时{}毫秒",e-s);
        return output;  
	}
}

