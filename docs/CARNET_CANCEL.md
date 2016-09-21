### Canceling the carnet

Only `active` carnets can be canceled:

```java
HashMap<String, String> params = new HashMap<>();
params.put("id", "1002");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("cancelCarnet", params, new JSONObject());
	System.out.println(response);
}catch (GerencianetException e){
	System.out.println(e.getCode());
	System.out.println(e.getError());
	System.out.println(e.getErrorDescription());
}catch (Exception e) {
	System.out.println(e.getMessage());
}

```

If everything goes well, the return will be:

```json
{
	"code":200
}
```
