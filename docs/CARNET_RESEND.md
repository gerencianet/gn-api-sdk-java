### Resending the carnet

To resend the carnet, the carnet must have a `active` status.

If the carnet contemplates this requirement, you just have to provide the carnet id and a email to resend it:

```java
HashMap<String, String> params = new HashMap<>();
params.put("id", "0");

JSONObject body = new JSONObject();
body.put("email", "oldbuck@gerencianet.com.br");

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
