package net.plshark.notes.service;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import net.plshark.notes.Role;
import net.plshark.notes.User;
import net.plshark.notes.repo.RoleRepository;
import net.plshark.notes.repo.UserRepository;

/**
 * Tests for {@link UserDetailsServiceImpl}
 */
public class UserDetailsServiceImplTest {

    /**
     * Verify a user and its roles are mapped to the correct UserDetails and
     * GrantedAuthority
     */
    @Test
    public void loadUserByUsernameTest() {
        UserRepository userRepo = Mockito.when(Mockito.mock(UserRepository.class).getForUsername("user"))
                .thenReturn(new User(25, "user", "pass")).getMock();
        RoleRepository roleRepo = Mockito.when(Mockito.mock(RoleRepository.class).getRolesForUser(25))
                .thenReturn(Arrays.asList(new Role(3, "normal-user"), new Role(5, "admin"))).getMock();
        UserDetailsService service = new UserDetailsServiceImpl(userRepo, roleRepo);

        UserDetails userDetails = service.loadUserByUsername("user");
        Assert.assertEquals("user", userDetails.getUsername());
        Assert.assertEquals("pass", userDetails.getPassword());
        Assert.assertEquals(2, userDetails.getAuthorities().size());
        Assert.assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("normal-user")));
        Assert.assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("admin")));
    }

    /**
     * Verify a UsernameNotFoundException is thrown when the user is not found
     */
    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsernameNotFoundTest() {
        UserRepository userRepo = Mockito.when(Mockito.mock(UserRepository.class).getForUsername("user"))
                .thenThrow(new EmptyResultDataAccessException(1)).getMock();
        UserDetailsService service = new UserDetailsServiceImpl(userRepo, Mockito.mock(RoleRepository.class));

        service.loadUserByUsername("user");
    }

    /**
     * Verify nothing blows up when the roles list is empty
     */
    @Test
    public void loadUserByUsernameNoRolesTest() {
        UserRepository userRepo = Mockito.when(Mockito.mock(UserRepository.class).getForUsername("user"))
                .thenReturn(new User(25, "user", "pass")).getMock();
        RoleRepository roleRepo = Mockito.when(Mockito.mock(RoleRepository.class).getRolesForUser(25))
                .thenReturn(Collections.emptyList()).getMock();
        UserDetailsService service = new UserDetailsServiceImpl(userRepo, roleRepo);

        UserDetails userDetails = service.loadUserByUsername("user");
        Assert.assertEquals("user", userDetails.getUsername());
        Assert.assertEquals("pass", userDetails.getPassword());
        Assert.assertEquals(0, userDetails.getAuthorities().size());
    }
}
