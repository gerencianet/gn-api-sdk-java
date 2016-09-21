## Adding information to carnet's history

It is possible to add information to the history of a carnet. These informations will be listed when [detailing a carnet](/docs/CARNET_DETAIL.md).

The process to add information to history is shown below:


```java
import java.util.HashMap;
import org.json.JSONObject;
import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

public class CreateCarnetHistory 
{
	public static void main(String[] args) 
	{
		JSONObject options = new JSONObject();
		options.put("client_id", "Client_Id");
		options.put("client_secret", "Client_Secret");
		options.put("sandbox", true);
		
		HashMap<String, String> params = new HashMap<>();
		params.put("id", "0");
		
	   JSONObject body = new JSONObject();
		body.put("description", "This carnet is about a service");
		
		try {
			Gerencianet gn = new Gerencianet(options);
			JSONObject response = gn.call("createCarnetHistory", params, body);
			System.out.println(response);
		}catch (GerencianetException e){
			System.out.println(e.getCode());
			System.out.println(e.getError());
			System.out.println(e.getErrorDescription());
		}
		catch (Exception e) {
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
