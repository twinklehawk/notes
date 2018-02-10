package net.plshark.users.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Interface for authenticating a user and retrieving details on the authenticated user
 */
public interface UserAuthenticationService extends UserDetailsService {

    /**
     * Get the user ID of the authenticated user
     * @param auth the authenticated user
     * @return the user ID
     */
    long getUserIdForAuthentication(Authentication auth);
}
