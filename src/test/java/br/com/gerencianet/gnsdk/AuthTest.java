package br.com.gerencianet.gnsdk;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Test for Auth class
 * @author Filipe Mata
 */

public class AuthTest {
	private Auth authenticator;
	private JSONObject success;

    @Mock
    private JSONObject credentials;
    
    @Mock
    private Request requester;
    
    @Before
    public void setUp(){
    	MockitoAnnotations.initMocks(this);
    	success = new JSONObject();
    	success.put("access_token", "token");
    	success.put("expires_in", 500);
    	success.put("token_type", "bearer");
    }
    
    @Test(expected=Exception.class)
    public void shouldNotCreateAuthWithoutClientId() throws Exception
    {
    	when(credentials.has("clientId")).thenReturn(false);
    	when(credentials.has("clientSecret")).thenReturn(true);
		authenticator = new Auth(credentials, "post", "v1/authorize");		
    }
    
    @Test(expected=Exception.class)
    public void shouldNotCreateAuthWithoutClientSecret() throws Exception
    {
    	when(credentials.has("clientId")).thenReturn(true);
    	when(credentials.has("clientSecret")).thenReturn(false);
		authenticator = new Auth(credentials, "post", "v1/authorize");		
    }
    
    @Test(expected=Exception.class)
    public void shouldNotCreateAuthWithoutClientSecretAndClientId() throws Exception
    {
    	when(credentials.has("clientId")).thenReturn(false);
    	when(credentials.has("clientSecret")).thenReturn(false);
		authenticator = new Auth(credentials, "post", "v1/authorize");		
    }
    
    @Test
    public void shouldSetPropertiesCorrectly() throws Exception
    {
    	when(credentials.has("clientId")).thenReturn(true);
    	when(credentials.has("clientSecret")).thenReturn(true);
    	when(credentials.has("partnerToken")).thenReturn(true);
    	when(credentials.getString("partnerToken")).thenReturn("teste");
    	when(credentials.getString("baseUri")).thenReturn("https://sandbox.gerencianet.com.br");
		authenticator = new Auth(credentials, "post", "v1/authorize");
    }
    
    @Test
    public void shouldAuthorizeSuccessfully(){
    	when(credentials.has("clientId")).thenReturn(true);
    	when(credentials.has("clientSecret")).thenReturn(true);
    	when(credentials.getString("baseUri")).thenReturn("https://sandbox.gerencianet.com.br");
    	when(credentials.getString("clientId")).thenReturn("client_id");
    	when(credentials.getString("clientSecret")).thenReturn("client_secret");
    	
		try {
			authenticator = new Auth(credentials, "post", "v1/authorize");
			Date expectedBefore = new Date();
			when(requester.send(authenticator.getAuthBody())).thenReturn(success);
			authenticator.setRequest(requester);
			authenticator.authorize();
			verify(requester, times(1)).addHeader("Authorization", "Basic " + authenticator.getAuthCredentials());
			verify(requester, times(1)).send(authenticator.getAuthBody());
			Assert.assertTrue(authenticator.getAccessToken().equals("token"));
			Assert.assertTrue(authenticator.getExpires().compareTo(expectedBefore) > 0);
			Assert.assertTrue(authenticator.getTokenType().equals("bearer"));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Shouldn't throwed any exception.");
		}
    }
}
