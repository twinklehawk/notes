package net.plshark.notes.webservice;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.plshark.notes.BadRequestException;
import net.plshark.notes.Role;
import net.plshark.notes.service.UserManagementService;

/**
 * Controller to provide web service methods for roles
 */
@RestController
@RequestMapping("/roles")
public class RolesController {

    private final UserManagementService userMgmtService;

    /**
     * Create a new instance
     * @param userMgmtService the service to use to create, delete, and modify roles
     */
    public RolesController(UserManagementService userMgmtService) {
        this.userMgmtService = userMgmtService;
    }

    /**
     * Insert a new role
     * @param role the role to insert
     * @return the inserted role
     * @throws BadRequestException if the role already has an ID set
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Role insert(@RequestBody Role role) throws BadRequestException {
        if (role.getId().isPresent())
            throw new BadRequestException("Cannot insert role with ID already set");
        return userMgmtService.saveRole(role);
    }

    /**
     * Delete a role by ID
     * @param id the role ID
     */
    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable("id") long id) {
        userMgmtService.deleteRole(id);
    }
}
