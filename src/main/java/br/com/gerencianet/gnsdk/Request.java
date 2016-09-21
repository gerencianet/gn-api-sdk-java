package br.com.gerencianet.gnsdk;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import org.json.JSONObject;
import org.json.JSONTokener;

import br.com.gerencianet.gnsdk.exceptions.AuthorizationException;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

/**
 * This class is responsible to create an HttpURLConnection Object,
 * generate the request body and send it to a given endpoint. The send method return a response for that request.
 * @author Filipe Mata
 *
 */
public class Request {
	
	private HttpURLConnection client;
	
	public Request(String method, HttpURLConnection conn) throws IOException {
		this.client = conn;
		this.client.setRequestProperty("Content-Type", "application/json");
		this.client.setRequestProperty("charset", "UTF-8");
		this.client.setRequestProperty("api-sdk", "java-"+ Config.getVersion());	 
		
		this.client.setRequestMethod(method.toUpperCase());
	}
	
	public void addHeader(String key, String value){
    	client.setRequestProperty(key, value);
	}
	
	public JSONObject send(JSONObject requestOptions) throws AuthorizationException, GerencianetException, IOException{	
    	byte[] postDataBytes;
		postDataBytes = requestOptions.toString().getBytes("UTF-8");
		this.client.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
		
		if(!client.getRequestMethod().toLowerCase().equals("get")){
			client.setDoOutput(true);
			OutputStream os = client.getOutputStream();
			os.write(postDataBytes);
			os.flush();
			os.close();
		}			

		int responseCode = client.getResponseCode();
		if (responseCode == HttpURLConnection.HTTP_OK) {
			InputStream responseStream = client.getInputStream();
			JSONTokener responseTokener = new JSONTokener(responseStream);
			return new JSONObject(responseTokener);
		}else if(responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
			throw new AuthorizationException();
		}else{
			InputStream responseStream = client.getErrorStream();
			JSONTokener responseTokener = new JSONTokener(responseStream);
			JSONObject response = new JSONObject(responseTokener);
			throw new GerencianetException(response);
		}	
			
	}
}
