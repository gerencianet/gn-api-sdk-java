## Updating carnets

### Changing the metadata

You can update the `custom_id` or the `notification_url` of a carnet at any time you want.

Is important to know that it updates all the charges of the carnet. If you want to update only one, see [Updating charges](/docs/CHARGE_UPDATE.md).

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
Then update metadata:

```java
HashMap<String, String> params = new HashMap<>();
params.put("id", "0");
		
JSONObject body = new JSONObject();
body.put("custom_id", "Carnet 0001");
body.put("notification_url", "http://domain.com/notification");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("updateCarnetMetadata", params, body);
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

If everything goes well, the return will be:

```json
{
	"code":200
}
```

### Updating the expiration date of a parcel

To update or set an expiration date to a parcel, the parcel must have a `waiting` or 'unpaid' status. You just have to provide the `carnet_id`, the number of the parcel (`parcel`) and a new expiration date (`expire_at`):

```java
HashMap<String, String> params = new HashMap<>();
params.put("id", "0");
params.put("parcel", "1");
		
JSONObject body = new JSONObject();
body.put("expire_at", "2018-01-01");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("updateParcel", params, body);
	System.out.println(response);
}catch (GerencianetException e){
	System.out.println(e.getCode());
	System.out.println(e.getError());
	System.out.println(e.getErrorDescription());
}
catch (Exception e) {
	e.printStackTrace();
	System.out.println(e.getMessage());
}

```

If everything goes well, the return will be:

```json
{
	"code":200
}
```
