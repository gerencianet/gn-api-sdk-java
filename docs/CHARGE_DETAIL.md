## Detailing charges

Instantiate the module:

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
		
		JSONObject options = new JSONObject();
		options.put("client_id", "client_id");
		options.put("client_secret", "client_secret");
		options.put("sandbox", true); 
		
		try {
			Gerencianet gn = new Gerencianet(options);
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

It's very simple to get details from a charge. You just need the id:

```java
HashMap<String, String> params = new HashMap<>();
params.put("id", "1000");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("detailCharge", params, new JSONObject());
	System.out.println(response);
}catch (GerencianetException e){
	System.out.println(e.getCode());
	System.out.println(e.getError());
	System.out.println(e.getErrorDescription());
}
catch (Exception e) {
	System.out.println(e.getMessage());
}

```

As response, you will receive all the information about the charge (including if it belongs to a subscription or carnet):

```json
{
  "code": 200,
  "data": {
    "charge_id": 1000,
    "subscription_id": 12,
    "total": 5000,
    "status": "new",
    "custom_id": "Product 0001",
    "created_at": "2015-05-14",
    "notification_url": "http://yourdomain.com",
    "items": [
      {
        "name": "Product 1",
        "value": 1000,
        "amount": 1
      }
      {
        "name": "Product 2",
        "value": 2000,
        "amount": 2
      }
    ],
    "history": [
      {
        "message": "Cobrança criada",
        "created_at": "2015-05-14 15:39:14"
      }
    ]
  }
}

```
