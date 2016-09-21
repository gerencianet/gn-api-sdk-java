## Updating subscriptions

### Changing the metadata

You can update the `custom_id` or the `notification_url` of a subscription at any time you want.

Is important to know that it updates all the charges of the subscription. If you want to update only one, see [Updating charges](/docs/CHARGE_UPDATE.md).

```java
import java.util.HashMap;
import org.json.JSONObject;
import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

public class TesteGN 
{
	public static void main(String[] args)
	{
		HashMap<String, String> params = new HashMap<>();
		params.put("id", "1000");
		
		JSONObject body = new JSONObject();
		body.put("notification_url", "http://localhost");
		body.put("custom_id", "Custom Subscription 0001");
		
		JSONObject options = new JSONObject();
		options.put("client_id", "client_id");
		options.put("client_secret", "client_secret");
		options.put("sandbox", true); 
		
		try {
			Gerencianet gn = new Gerencianet(options);
			JSONObject subscription = gn.call("updateSubscriptionMetadata", params, body);
			System.out.println(subscription);
		}catch (GerencianetException e){
			System.out.println(e.getCode());
			System.out.println(e.getError());
			System.out.println(e.getErrorDescription());
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}

```

If everything goes well. the return will be:

```json
{
	"code":200
}
```
