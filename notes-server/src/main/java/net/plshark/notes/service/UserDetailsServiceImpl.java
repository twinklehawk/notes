package net.plshark.notes.service;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.plshark.notes.Role;
import net.plshark.notes.User;
import net.plshark.notes.repo.UserRepository;

/**
 * Implementation of the UserDetailsService
 */
@Named
@Singleton
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Create a new instance
     * @param userRepository the user repository
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository, "userRepository cannot be null");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user;
        try {
            user = userRepository.getForUsername(username);
        } catch (DataAccessException e) {
            throw new UsernameNotFoundException("No matching user for " + username, e);
        }
        List<Role> userRoles = userRepository.getRolesForUser(user.getId().getAsLong());

        Set<GrantedAuthority> authorities = new HashSet<>(userRoles.size());
        userRoles.forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                authorities);
    }
}
