package com.wlwx.azkaban;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * https请求工具类
 * @author zjj
 * @date 2017年7月11日 下午4:53:41
 */
public class HttpsRequest {
	
	private static final Logger LOGGER = Logger.getLogger(HttpsRequest.class);
	
	private HttpsRequest(){
		super();
	}
	
	static {
		disableSslVerification();
	}

	private static void disableSslVerification() {
		try {
			TrustManager[] trustAllCerts = { new X509TrustManager() {
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				@Override
				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
				@Override
				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
			} };
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HostnameVerifier allHostsValid = new HostnameVerifier() {
				@Override
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String httpsRequest(String url, String params,
			HttpMethod method) {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Accept", "application/json");
		headers.add("Content-Type",
				MediaType.APPLICATION_FORM_URLENCODED.toString());
		headers.add("Authorization", "Basic XXXXXXXXXXXXXXXXXXXXXXXXXXXX");

		RestTemplate restTemplate = new RestTemplate();
		HttpEntity entity = new HttpEntity(params, headers);
		HttpEntity response = restTemplate.exchange(url, method, entity,
				String.class, new Object[0]);
		return (String) response.getBody();
	}
}