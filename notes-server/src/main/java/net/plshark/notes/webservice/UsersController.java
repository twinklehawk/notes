package net.plshark.notes.webservice;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.plshark.notes.service.UserManagementService;

/**
 * Controller providing web service methods for users
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserManagementService userMgmtService;

    /**
     * Create a new instance
     * @param userManagementService the service to use to modify users
     */
    public UsersController(UserManagementService userManagementService) {
        this.userMgmtService = userManagementService;
    }
}
