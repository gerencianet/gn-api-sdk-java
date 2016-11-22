package br.com.gerencianet.gnsdk;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONObject;
import br.com.gerencianet.gnsdk.exceptions.AuthorizationException;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

/**
 * This class is used to create an authenticator Object, 
 * responsible to send needed Gerencianet credentials (Client_Id and Client_Secret) to it's API.
 * @author Filipe Mata
 *
 */

public class Auth {
	private String accessToken;
	private String tokenType;
	private Date expires;
	private Request request;
	private JSONObject authBody;
	private String authCredentials;
	
	public Auth(JSONObject credentials, String method, String authorizeRoute) throws Exception {
		if(!credentials.has("clientId") || !credentials.has("clientSecret")){
			throw new Exception("Client_Id or Client_Secret not found");
		}
		
		String url = credentials.getString("baseUri") + authorizeRoute;
		URL link = new URL(url);
		HttpURLConnection client = (HttpURLConnection) link.openConnection();
		
		this.request = new Request(method, client);
		
		if(credentials.has("partnerToken")){
			this.request.addHeader("partner-token", credentials.getString("partnerToken"));
		}
		
		authBody = new JSONObject();
		authBody.put("grant_type", "client_credentials");
		
		String auth = credentials.getString("clientId") + ":" + credentials.getString("clientSecret");
		this.authCredentials = DatatypeConverter.printBase64Binary(auth.getBytes("UTF-8"));
	}
	
	public void setRequest(Request request) {
		this.request = request;
	}
	
	public void authorize() throws IOException, AuthorizationException, GerencianetException{
		this.request.addHeader("Authorization", "Basic " + this.authCredentials);
		JSONObject response = this.request.send(authBody);
		this.accessToken = response.getString("access_token");
		this.expires = new Date(new Date().getTime() + response.getLong("expires_in"));
		this.tokenType = response.getString("token_type");
		
	}
	
	public Date getExpires() {
		return this.expires;
	}
	
	public String getAccessToken() {
		return this.accessToken;
	}
	
	public String getTokenType() {
		return this.tokenType;
	}
	
	public JSONObject getAuthBody() {
		return authBody;
	}
	
	public String getAuthCredentials() {
		return authCredentials;
	}
}
