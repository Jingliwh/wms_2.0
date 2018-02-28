package com.crm.service.base;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import cn.itcast.crm.domain.Customer;

public interface CustomerService {
	
	@Path(value="noassociationcustomers")
	@GET
	@Produces({"application/xml","application/json"})
	public List<Customer> findNoAssociationCustomers();//返回未关联的客户列表
	
	@Path("hadassociationfixedareacustomers/{fixedareaid}")
	@GET
	@Produces({"application/xml","application/json"})
	public List<Customer> findHadAssociationFixedAreaCustomers(@PathParam("fixedareaid")String fixedAreaId);

	@Path("asssociatecustomerstofixedarea")
	@PUT
	public void asssociateCustomersToFixedArea(@QueryParam("customerIdsStr")String customerIdsStr,@QueryParam("fixedAreaId")String fixedAreaId);
	
	@Path("register")
	@POST
	@Consumes({"application/xml","application/json"})
	public void register(Customer customer);
	
	@Path("findByTelephone/{telephone}")
	@GET
	@Produces({"application/xml","application/json"})
	public Customer findByTelephone(@PathParam("telephone")String  telephone);
	
	@Path("updateType/{telephone}")
	@PUT
	public void updateType(@PathParam("telephone")String  telephone);
	
	@Path("login")
	@GET
	@Produces({"application/xml","application/json"})
	public Customer login(@QueryParam("telephone")String  telephone,@QueryParam("password")String password);

	@Path("findByTelephone/{telephone}")
	@GET
	@Consumes({"application/xml","application/json"})
	public  String findFixedAreaIdByAddress(@QueryParam("address") String address);
}
