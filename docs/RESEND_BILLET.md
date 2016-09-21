### Resending billet

To resend the charge's billet, the charge must have a `waiting` status, and the payment method chosen must be `banking_billet`.

If the charge contemplates these requirements, you just have to provide the charge id and a email to resend the billet:

```java
import java.util.HashMap;
import org.json.JSONObject;
import br.com.gerencianet.gnsdk.Gerencianet;
import br.com.gerencianet.gnsdk.exceptions.GerencianetException;

public class create 
{
	public static void main(String[] args)
	{
		HashMap<String, String> params = new HashMap<>();
		params.put("id", "1000");
		
		JSONObject body = new JSONObject();
		body.put("email", "oldbuck@gerencianet.com.br")
		
		JSONObject options = new JSONObject();
		options.put("client_id", "client_id");
		options.put("client_secret", "client_secret");
		options.put("sandbox", true); 
		
		try {
			Gerencianet gn = new Gerencianet(options);
			JSONObject response = gn.call("resendBillet", params, body);
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
