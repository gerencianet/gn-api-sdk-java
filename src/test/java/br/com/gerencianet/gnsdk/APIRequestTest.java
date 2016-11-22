package br.com.gerencianet.gnsdk;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.gerencianet.gnsdk.exceptions.AuthorizationException;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

/**
 * Test for APIRequest class
 * @author Filipe Mata
 */

public class APIRequestTest {
	private APIRequest apiRequester;
	
	@Mock
	private Config config;
	
	@Mock
	private Auth authenticator;
	
	@Mock
	private Request requester;
	
	@Mock
	private JSONObject body;
	
	@Before
    public void setUp(){
    	MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void shouldRequestSuccessfully() throws AuthorizationException, GerencianetException, IOException
	{
		when(authenticator.getExpires()).thenReturn(new Date(new Date().getTime() + 500));
		JSONObject options = new JSONObject();
		options.put("baseUri", "https://sandbox.gerencianet.com.br");
		JSONObject body = mock(JSONObject.class);
		
		when(config.getOptions()).thenReturn(options);
		
		apiRequester = new APIRequest(authenticator, requester, body);
		apiRequester.send(); // "post", "/v1/charge", 
		verify(requester, times(1)).send(body);
		
	}
	
	@Test
	public void shouldReauthorizeExpiredToken() throws AuthorizationException, GerencianetException, IOException{
		when(authenticator.getExpires()).thenReturn(new Date());
		JSONObject options = new JSONObject();
		options.put("baseUri", "https://sandbox.gerencianet.com.br");
		JSONObject body = mock(JSONObject.class);
		
		when(config.getOptions()).thenReturn(options);
		
		apiRequester = new APIRequest(authenticator, requester, body);
		apiRequester.send();
		verify(authenticator, times(1)).authorize();
		verify(requester, times(1)).send(body);
	}
	
	@Test
	public void shouldReauthorizeNullToken() throws AuthorizationException, GerencianetException, IOException{
		when(authenticator.getExpires()).thenReturn(null);
		JSONObject options = new JSONObject();
		options.put("baseUri", "https://sandbox.gerencianet.com.br");
		JSONObject body = mock(JSONObject.class);
		when(config.getOptions()).thenReturn(options);
		
		apiRequester = new APIRequest(authenticator, requester, body);
		apiRequester.send();
		verify(authenticator, times(1)).authorize();
		verify(requester, times(1)).send(body);
	}
	
	@Test
	public void shouldReauthorizeWhenServerRespondsWithAuthError() throws GerencianetException, IOException, AuthorizationException{
		JSONObject success = new JSONObject("{status: 200}");
		JSONObject body = mock(JSONObject.class);
		
		when(authenticator.getExpires()).thenReturn(new Date(new Date().getTime() + 500));
		when(requester.send(body)).thenThrow(new AuthorizationException()).thenReturn(success);
		
		JSONObject response = new JSONObject();
		apiRequester = new APIRequest(authenticator, requester, body);
		try{
			response = apiRequester.send();
		}catch(AuthorizationException e){
			verify(authenticator, times(1)).authorize();
			verify(requester, times(2)).send(body);
			Assert.assertTrue(response.equals(success));
		}
		
	}
	
	@Test
	public void shouldSetPropertiesCorrectly() throws Exception{
		JSONObject endpoints = new JSONObject();
		JSONObject authorize = new JSONObject();
		authorize.put("route", "/v1/authorize");
		authorize.put("method", "post");
		endpoints.put("authorize", authorize);
		JSONObject body = new JSONObject("{\"item\": 12}");
		
		JSONObject credentials = mock(JSONObject.class);
		when(credentials.has("clientId")).thenReturn(true);
		when(credentials.has("clientSecret")).thenReturn(true);
		when(credentials.has("partnerToken")).thenReturn(true);
		when(credentials.getString("partnerToken")).thenReturn("teste");
		when(credentials.getString("baseUri")).thenReturn("https://sandbox.gerencianet.com.br");
		when(config.getEndpoints()).thenReturn(endpoints);
		when(config.getOptions()).thenReturn(credentials);
		apiRequester = new APIRequest("post", "/v1/charge", body, config);
		Assert.assertTrue(apiRequester.getRequester() != null);
		Assert.assertTrue(apiRequester.getBody().has("item"));
		Assert.assertTrue(apiRequester.getBody().getInt("item") == 12);
	}
	
}
