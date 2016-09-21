package br.com.gerencianet.gnsdk;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import br.com.gerencianet.gnsdk.exceptions.AuthorizationException;

/**
 * Test for Endpoints class
 * @author Filipe Mata
 */

public class EndpointsTest {
	private Endpoints endpoints;
	
	@Mock
	private APIRequest apiRequester;
	
	@Mock
	private Config config;
	
	@Before
    public void setUp(){
    	MockitoAnnotations.initMocks(this);
    }
	
	@Test 
	public void shouldSetConfigPropertiesWithJsonCorrectly() throws Exception{
		JSONObject options = mock(JSONObject.class);
		endpoints = new Endpoints(options);
	}
	
	@Test 
	public void shouldSetConfigPropertiesWithMapCorrectly() throws Exception{
		Map<String, Object> options = new HashMap<String, Object>();
		endpoints = new Endpoints(options);
	}
	
	@Test
	public void shouldGenerateRequestWithJsonBodySuccessfuly() throws Exception{
		JSONObject ep = new JSONObject();
		JSONObject charge = new JSONObject();
		charge.put("method", "post");
		charge.put("route", "/v1/charge");
		
		JSONObject authorize = new JSONObject();
		authorize.put("method", "post");
		authorize.put("route", "/v1/authorize");
		
		ep.put("authorize", authorize);
		ep.put("charge", charge);
		JSONObject options = mock(JSONObject.class);
		when(options.has("clientId")).thenReturn(true);
		when(options.has("clientSecret")).thenReturn(true);
		when(options.getString("baseUri")).thenReturn("https://sandbox.gerencianet.com.br");
		Mockito.when(config.getEndpoints()).thenReturn(ep);
		Mockito.when(config.getOptions()).thenReturn(options);
		
		endpoints = new Endpoints(config, apiRequester);
		JSONObject body = mock(JSONObject.class);
		endpoints.call("charge", new HashMap<String, String>(), body);
		verify(apiRequester, times(1)).send();
	}
	
	@Test
	public void shouldGenerateRequestWithMapBodySuccessfuly() throws Exception{
		JSONObject ep = new JSONObject();
		JSONObject charge = new JSONObject();
		charge.put("method", "post");
		charge.put("route", "/v1/charge");
		
		JSONObject authorize = new JSONObject();
		authorize.put("method", "post");
		authorize.put("route", "/v1/authorize");
		
		ep.put("authorize", authorize);
		ep.put("charge", charge);
		
		Map<String, Object> body = new HashMap<String,Object>();
		body.put("item", 1);
		JSONObject expectedResponse = new JSONObject("{\"status\": 200}");
		
		JSONObject options = mock(JSONObject.class);
		when(options.has("clientId")).thenReturn(true);
		when(options.has("clientSecret")).thenReturn(true);
		when(options.getString("baseUri")).thenReturn("https://sandbox.gerencianet.com.br");
		Mockito.when(config.getEndpoints()).thenReturn(ep);
		Mockito.when(config.getOptions()).thenReturn(options);
		when(apiRequester.send()).thenReturn(expectedResponse);
		
		endpoints = new Endpoints(config, apiRequester);
		HashMap<String, Object> response = (HashMap<String, Object>) endpoints.call("charge", new HashMap<String, String>(), body);
		
		Assert.assertTrue(response.containsKey("status"));
		Assert.assertTrue(response.get("status").equals(200));
	}
	
	@Test(expected=Exception.class)
	public void shouldThrowExceptionForWrongMethod() throws Exception{
		JSONObject ep = new JSONObject();

		JSONObject authorize = new JSONObject();
		authorize.put("method", "post");
		authorize.put("route", "/v1/authorize");
		
		ep.put("authorize", authorize);
		Mockito.when(config.getEndpoints()).thenReturn(ep);
		
		endpoints = new Endpoints(config, apiRequester);
		JSONObject body = mock(JSONObject.class);
		endpoints.call("charge", new HashMap<String, String>(), body);
	}
	
	@Test
	public void shouldForwarWithParamsAndQueryString() throws Exception{
		JSONObject ep = new JSONObject();
		JSONObject charge = new JSONObject();
		charge.put("method", "post");
		charge.put("route", "/v1/charge/:id");
		
		JSONObject authorize = new JSONObject();
		authorize.put("method", "post");
		authorize.put("route", "/v1/authorize");
		
		ep.put("authorize", authorize);
		ep.put("charge", charge);
		JSONObject options = mock(JSONObject.class);
		when(options.has("clientId")).thenReturn(true);
		when(options.has("clientSecret")).thenReturn(true);
		when(options.getString("baseUri")).thenReturn("https://sandbox.gerencianet.com.br");
		Mockito.when(config.getEndpoints()).thenReturn(ep);
		Mockito.when(config.getOptions()).thenReturn(options);
		JSONObject body = mock(JSONObject.class);
		
		endpoints = new Endpoints(config);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", "1");
		params.put("parcel", "1");
		try
		{
			endpoints.call("charge", params, body);
		}
		catch(AuthorizationException e){
			Assert.assertTrue(endpoints.getRequester().getRoute().equals("/v1/charge/1?parcel=1"));
		}
	}
	
	@Test
	public void shouldForwarWithParams() throws Exception{
		JSONObject ep = new JSONObject();
		JSONObject charge = new JSONObject();
		charge.put("method", "post");
		charge.put("route", "/v1/charge/:id/parcel/:parcel");
		
		JSONObject authorize = new JSONObject();
		authorize.put("method", "post");
		authorize.put("route", "/v1/authorize");
		
		ep.put("authorize", authorize);
		ep.put("charge", charge);
		JSONObject options = mock(JSONObject.class);
		when(options.has("clientId")).thenReturn(true);
		when(options.has("clientSecret")).thenReturn(true);
		when(options.getString("baseUri")).thenReturn("https://sandbox.gerencianet.com.br");
		Mockito.when(config.getEndpoints()).thenReturn(ep);
		Mockito.when(config.getOptions()).thenReturn(options);

		endpoints = new Endpoints(config);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", "1");
		try
		{
			endpoints.call("charge", params, new HashMap<String, Object>());
		}
		catch(AuthorizationException e){
			Assert.assertTrue(endpoints.getRequester().getRoute().equals("/v1/charge/1/parcel/:parcel"));
		}
	}
	
	@Test
	public void shouldForwarWithQueryString() throws Exception {
		JSONObject ep = new JSONObject();
		JSONObject charge = new JSONObject();
		charge.put("method", "post");
		charge.put("route", "/v1/charge");
		
		JSONObject authorize = new JSONObject();
		authorize.put("method", "post");
		authorize.put("route", "/v1/authorize");
		
		ep.put("authorize", authorize);
		ep.put("charge", charge);
		
		JSONObject options = mock(JSONObject.class);
		when(options.has("clientId")).thenReturn(true);
		when(options.has("clientSecret")).thenReturn(true);
		when(options.getString("baseUri")).thenReturn("https://sandbox.gerencianet.com.br");
		Mockito.when(config.getEndpoints()).thenReturn(ep);
		Mockito.when(config.getOptions()).thenReturn(options);
		JSONObject body = mock(JSONObject.class);
		
		endpoints = new Endpoints(config, apiRequester);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", "1");
		params.put("token", "45646546894621");
		try
		{
			endpoints.call("charge", params, body);
		}
		catch(AuthorizationException e){
			String route = endpoints.getRequester().getRoute();
			Assert.assertTrue(route.equals("/v1/charge?token=45646546894621&id=1") || route.equals("/v1/charge?id=1&token=45646546894621"));
		}
	}
}
