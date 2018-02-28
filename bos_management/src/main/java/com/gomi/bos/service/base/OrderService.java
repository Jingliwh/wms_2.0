package com.gomi.bos.service.base;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import com.gomi.bos.domain.base.Order;

public interface OrderService {
	
	@Path("/saveOrder")
	@POST
	@Consumes({"application/xml","application/json"})
	public void saveOrder(Order  order);

}
