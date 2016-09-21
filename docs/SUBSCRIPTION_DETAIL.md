## Detailing subscriptions

Works just like the last example, but here you pass the subscription id:

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
		
		JSONObject options = new JSONObject();
		options.put("client_id", "client_id");
		options.put("client_secret", "client_secret");
		options.put("sandbox", true); 
		
		try {
			Gerencianet gn = new Gerencianet(options);
			JSONObject response = gn.call("detailSubscription", params, new JSONObject());
			System.out.println(response);
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
If everything goes well, the return will be:

```json
{
  "code": 200,
  "data": {
    "subscription_id": 12,
    "value": 2000,
    "status": "new",
    "payment_method": "banking_billet",
    "next_expire_at": "2015-06-14",
    "interval": 1,
    "repeats": 2,
    "processed_amount": 0,
    "created_at": "2015-05-14 15:39:14",
    "history": [
      {
        "charge_id": 233,
        "status": "new",
        "created_at": "2015-05-14 15:39:14"
      }
    ]
  }
}
```

Note that if you [detail a charge](/docs/CHARGE_DETAIL.md) that belongs to a subscription, the response will have a `subscription_id`.
