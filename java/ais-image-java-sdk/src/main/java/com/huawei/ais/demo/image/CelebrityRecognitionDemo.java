package com.huawei.ais.demo.image;

import java.io.File;
import java.io.IOException;

import com.huawei.ais.demo.RequestSetup;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.entity.StringEntity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huawei.ais.demo.ClientContextUtils;
import com.huawei.ais.demo.ResponseProcessUtils;
import com.huawei.ais.sdk.AisAccess;
import com.huawei.ais.sdk.AisAccessWithProxy;
import com.huawei.ais.sdk.util.HttpClientUtils;

/**
 *  名人识别服务的使用示例类
 */
public class CelebrityRecognitionDemo {
	//
	// 名人识别服务的使用示例函数
	//
	private static void celebrityRecognitionDemo() throws IOException {
		//
		// 1. 在ClientContextUtils类中, 配置好访问名人识别服务的基本信息,
		// 然后，在此处生成对应的一个客户端连接对象
		// 		
		// 设置三个超时参数限制连接超时，分别如下
		RequestSetup requestSetup = new RequestSetup(5000, 1000, 5000);

		AisAccess service = new AisAccess(ClientContextUtils.getAuthInfo(), requestSetup.getConnectionTimeout(),
				requestSetup.getConnectionRequestTimeout(), requestSetup.getSocketTimeout());
		
		//
		// 1.a 此处支持使用代理方式访问名人识别服务，用于不能直接访问华为云官网服务的情况, 例如，内网网络。
		// 如果使用此处方式，需要同时在ClientContextUtils中，配置相应的代理服务器的参数类(ProxyHostInfo)
		//
		//AisAccess service = new AisAccessWithProxy(ClientContextUtils.getAuthInfo(), ClientContextUtils.getProxyHost(), connectionTimeout,connectionRequestTimeout, socketTimeout);

		try {
			//
			// 2.构建访问名人识别服务需要的参数
			//
			String uri = "/v1.0/image/celebrity-recognition";
			/**
			 * TODO 运行此样例时可在data目录放入待识别的人像图片，此处仅用一张非人像图片表明设置文件名的位置
			 */
			byte[] fileData = FileUtils.readFileToByteArray(new File("data/image-tagging-demo-1.jpg"));
			String fileBase64Str = Base64.encodeBase64String(fileData);
			
			JSONObject json = new JSONObject();
			json.put("image", fileBase64Str);
			//json.put("threshold", "0.0");
			StringEntity stringEntity = new StringEntity(json.toJSONString(), "utf-8");
			
			// 3.传入名人识别服务对应的uri参数, 传入名人识别服务需要的参数，
			// 该参数主要通过JSON对象的方式传入, 使用POST方法调用服务
			HttpResponse response = service.post(uri, stringEntity);
			
			// 4.验证服务调用返回的状态是否成功，如果为200, 为成功, 否则失败。
			ResponseProcessUtils.processResponseStatus(response);
			
			// 5.处理服务返回的字符流，输出识别结果。
			JSONObject jsonObject = JSON.parseObject(HttpClientUtils.convertStreamToString(response.getEntity().getContent()));
			System.out.println(JSON.toJSONString(jsonObject, true));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			// 6.使用完毕，关闭服务的客户端连接
			service.close();
		}
	}

	//
	// 主入口函数
	//
	public static void main(String[] args) throws IOException {
		// 测试入口函数
		celebrityRecognitionDemo();
	}
}
