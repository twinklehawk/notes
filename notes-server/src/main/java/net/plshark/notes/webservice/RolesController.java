package net.plshark.notes.webservice;

import java.util.Objects;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.plshark.BadRequestException;
import net.plshark.users.Role;
import net.plshark.users.service.UserManagementService;
import reactor.core.publisher.Mono;

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
        this.userMgmtService = Objects.requireNonNull(userMgmtService, "userMgmtService cannot be null");
    }

    /**
     * Insert a new role
     * @param role the role to insert
     * @return the inserted role or BadRequestException if the role already has an ID set
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Role> insert(@RequestBody Role role) {
        if (role.getId().isPresent())
            return Mono.error(new BadRequestException("Cannot insert role with ID already set"));
        return userMgmtService.saveRole(role);
    }

    /**
     * Delete a role by ID
     * @param id the role ID
     * @return an empty result
     */
    @DeleteMapping(path = "/{id}")
    public Mono<Void> delete(@PathVariable("id") long id) {
        return userMgmtService.deleteRole(id);
    }
}
