## Adding information to charge's history

It is possible to add information to the history of a charge. These informations will be listed when [detailing a charge](/docs/CHARGE_DETAIL.md).

The process to add information to history is shown below:


```java
import java.util.HashMap;
import org.json.JSONObject;
import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

public class createHistory
{
	public static void main(String[] args)
	{
		HashMap<String, String> params = new HashMap<>();
		
		JSONObject options = new JSONObject();
		options.put("client_id", "client_id");
		options.put("client_secret", "client_secret");
		options.put("sandbox", true); 
		
		HashMap<String, String> params = new HashMap<>();
		params.put("id", "1000");
		
		JSONObject body = new JSONObject();
		body.put("description", "Info to be added to charges history");
		
		try {
			Gerencianet gn = new Gerencianet(options);
			JSONObject response = gn.call("createChargeHistory", params, new JSONObject());
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
	"code":200
}
```
