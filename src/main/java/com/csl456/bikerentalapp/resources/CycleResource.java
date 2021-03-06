package com.csl456.bikerentalapp.resources;

import com.csl456.bikerentalapp.core.Cycle;
import com.csl456.bikerentalapp.core.UserRole;
import com.csl456.bikerentalapp.db.CycleDAO;
import com.csl456.bikerentalapp.db.SessionDAO;
import com.csl456.bikerentalapp.db.UserDAO;
import com.csl456.bikerentalapp.filter.RolesAllowed;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/cycle")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CycleResource {
	private final CycleDAO cycleDAO;
	private final UserDAO userDAO;
	private final SessionDAO sessionDAO;

	public CycleResource(CycleDAO cycleDAO, UserDAO userDAO,
						 SessionDAO sessionDAO) {
		this.cycleDAO = cycleDAO;
		this.userDAO = userDAO;
		this.sessionDAO = sessionDAO;
	}

	@POST
	@UnitOfWork
	public Cycle createCycle(Cycle cycle) {
		return cycleDAO.create(cycle);
	}

	@GET
	@UnitOfWork
	@RolesAllowed(UserRole.ADMIN)
	public List<Cycle> listCycle(@QueryParam("ownerId") Optional<Integer> ownerId) {
		if (ownerId.isPresent()) {
			return cycleDAO.getCyclesByOwnerId(ownerId.get());
		} else {
			return cycleDAO.findAll();
		}
	}

	@GET
	@UnitOfWork
	@Path("{id}")
	@RolesAllowed(UserRole.ADMIN)
	public Cycle getCycleById(@PathParam("id") int id) {
		return cycleDAO.findById(id);
	}

	@DELETE
	@UnitOfWork
	@Path("{id}")
	@RolesAllowed(UserRole.ADMIN)
	public Cycle removeCycle(@PathParam("id") int id, @HeaderParam(
			"Access_Token") String accessToken) {
		if (cycleDAO.findById(id).getOwnerId() != userDAO.findByUserName(sessionDAO.getUserName(accessToken)).getPersonId()) {
			throw new WebApplicationException("You do not own this cycle",
					Response.Status.UNAUTHORIZED);
		}
		return cycleDAO.remove(id);
	}

	@POST
	@UnitOfWork
	@Path("changeLocation")
	@RolesAllowed(UserRole.ADMIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Cycle changeLocation(@FormParam("id") int id, @FormParam(
			"newLocation") int newLocationId) {
		Cycle cycle = cycleDAO.findById(id);
		cycle.setLocationId(newLocationId);
		return cycleDAO.create(cycle);
	}

}
