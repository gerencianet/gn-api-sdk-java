package br.com.gerencianet.gnsdk;

import org.json.JSONObject;

/**
 * This class is used to create an Object with all needed configurations used in
 * Gerencianet API. This configurations include the Endpoints and URLs of
 * Gerencianet API, and credential data of Gerencianet client.
 * 
 * @author Filipe Mata
 *
 */
public class Config {
	private final static String version = "2.0.0";
	private JSONObject conf = new JSONObject();
	private JSONObject endpoints = new JSONObject();
	private JSONObject urls = new JSONObject();
	private JSONObject options = new JSONObject();

	public Config(JSONObject options, JSONObject config) throws Exception {
		this.endpoints = config;
		this.options = options;
	}

	public JSONObject getEndpoints() {
		return (JSONObject)endpoints.get("APIs");
	}

	public JSONObject getUrls() {
		return urls;
	}

	public void setConf(JSONObject options) {
		boolean sandbox = false;
		boolean debug = false;

		if (options.has("sandbox"))
			sandbox = options.getBoolean("sandbox");
		if (options.has("debug"))
			debug = options.getBoolean("debug");

		this.conf.put("sandbox", sandbox);
		this.conf.put("debug", debug);

		if (options.has("client_id"))
			this.conf.put("clientId", options.getString("client_id"));
		if (options.has("client_secret"))
			this.conf.put("clientSecret", options.getString("client_secret"));
		if (options.has("certificate"))
			this.conf.put("certificate", options.getString("certificate"));
		if (options.has("partner_token"))
			this.conf.put("partnerToken", options.getString("partner_token"));
		if (options.has("url")) {
			this.conf.put("baseUri", options.getString("url"));
		} else {
			String baseUri = this.urls.getString("production");
			if (this.conf.getBoolean("sandbox") == true)
				baseUri = this.urls.getString("sandbox");

			this.conf.put("baseUri", baseUri);
		}

		if (options.has("x-skip-mtls-checking")) {
			this.conf.put("headers", options.getString("x-skip-mtls-checking"));
		}

		if (options.has("x-idempotency-key")) {
			this.conf.put("headers", options.getString("x-idempotency-key"));
		}
	}

	public JSONObject getOptions() {
		return this.conf;
	}

	public static String getVersion() {
		return Config.version;
	}

	public void setURLs(JSONObject parURLs) {
		this.urls = parURLs;
		this.setConf(this.options);
	}

}