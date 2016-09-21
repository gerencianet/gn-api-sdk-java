### Canceling a carnet parcel

To cancel a carnet parcel, it must have status `waiting` or `unpaid`.

```java
HashMap<String, String> params = new HashMap<>();
params.put("id", "1001");
params.put("parcel", "1");

try {
	Gerencianet gn = new Gerencianet(options);
	JSONObject response = gn.call("cancelParcel", params, new JSONObject());
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

```java
{
	"code":200
}
```
