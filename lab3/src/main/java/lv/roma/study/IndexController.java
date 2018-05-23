package lv.roma.study;

import org.jooby.Result;
import org.jooby.View;
import org.jooby.mvc.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import static org.jooby.Results.html;
import static org.jooby.Results.ok;

@Singleton
@Path("/")
public class IndexController {

	private final PurchaseService purchaseService;

    @Inject
    public IndexController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GET
    public View index() {
        return  html("index").put(purchaseService.initialData());
    }

    @GET
    @Path("/new/:owner")
    public Purchase newOwner(String owner) {
    	return purchaseService.newOwner(owner);
    }

    @GET
    @Path("/purchase/:name")
    public Purchase purchase(String name) {
        return purchaseService.findByOwner(name);
    }

    @GET
    @Path("/synchronize/:token")
    public Result synchronize(String token) {
        purchaseService.synchronize(token);
        return ok("Done");
    }

    @POST
    @Path("/purchase")
    public Purchase purchase(@Body Purchase purchase) {
        return purchaseService.addPurchase(purchase);
    }

    @DELETE
    @Path("/purchase/:owner")
    public Purchase purchase(String owner, @Body PurchaseItem purchaseItem) {
        return purchaseService.deletePurchaseItem(owner, purchaseItem);
    }
}
