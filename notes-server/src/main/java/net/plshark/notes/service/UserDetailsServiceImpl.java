package net.plshark.notes.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.plshark.notes.Role;
import net.plshark.notes.User;
import net.plshark.notes.repo.UsersRepository;
import net.plshark.notes.repo.UserRolesRepository;

/**
 * Implementation of the UserDetailsService
 */
@Named
@Singleton
public class UserDetailsServiceImpl implements UserAuthenticationService {

    private final UsersRepository userRepo;
    private final UserRolesRepository userRolesRepo;

    /**
     * Create a new instance
     * @param userRepo the user repository
     * @param userRolesRepo the user roles repository
     */
    public UserDetailsServiceImpl(UsersRepository userRepo, UserRolesRepository userRolesRepo) {
        this.userRepo = Objects.requireNonNull(userRepo, "userRepository cannot be null");
        this.userRolesRepo = Objects.requireNonNull(userRolesRepo, "userRolesRepo cannot be null");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userRepo.getForUsername(username);
        } catch (DataAccessException e) {
            throw new UsernameNotFoundException("No matching user for " + username, e);
        }
        List<Role> userRoles = userRolesRepo.getRolesForUser(user.getId().getAsLong());

        return UserInfo.forUser(user, userRoles);
    }

    @Override
    public long getUserIdForAuthentication(Authentication auth) {
        long userId;

        if (auth.getPrincipal() instanceof UserInfo)
            userId = ((UserInfo) auth.getPrincipal()).getUserId();
        else
            userId = userRepo.getForUsername(auth.getName()).getId().getAsLong();

        return userId;
    }

    /**
     * UserDetails implementation that allows retrieving the user ID
     */
    static class UserInfo extends org.springframework.security.core.userdetails.User {

        private static final long serialVersionUID = -5943477264654485111L;
        private final long userId;

        public UserInfo(long userId, String username, String password,
                Collection<? extends GrantedAuthority> authorities) {
            super(username, password, authorities);
            this.userId = userId;
        }

        public long getUserId() {
            return userId;
        }

        /**
         * Build a UserInfo for a user and its roles
         * @param user the user
         * @param userRoles the user's roles
         * @return the built UserInfo
         */
        public static UserInfo forUser(User user, List<Role> userRoles) {
            Set<GrantedAuthority> authorities = new HashSet<>(userRoles.size());
            userRoles.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName())));
            return new UserInfo(user.getId().getAsLong(), user.getUsername(), user.getPassword(), authorities);
        }
    }
}
