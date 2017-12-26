package net.plshark.notes.service;

import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.plshark.notes.ObjectNotFoundException;
import net.plshark.notes.Role;
import net.plshark.notes.User;
import net.plshark.notes.repo.RoleRepository;
import net.plshark.notes.repo.UserRepository;

/**
 * UserManagementService implementation
 */
@Named
@Singleton
public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create a new instance
     * @param userRepository the repository for accessing users
     * @param roleRepository the repository for accessing roles
     * @param passwordEncoder the encoder to use to encode passwords
     */
    public UserManagementServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepo = Objects.requireNonNull(userRepository, "userRepository cannot be null");
        this.roleRepo = Objects.requireNonNull(roleRepository, "roleRepository cannot be null");
        this.passwordEncoder = Objects.requireNonNull(passwordEncoder, "passwordEncoder cannot be null");
    }

    @Override
    public User saveUser(User user) {
        if (user.getId().isPresent())
            throw new IllegalArgumentException("Updating a user is not supported");
        Objects.requireNonNull(user.getUsername(), "username cannot be null");
        Objects.requireNonNull(user.getPassword(), "password cannot be null");

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepo.insert(user);
    }

    @Override
    public void deleteUser(long userId) {
        // TODO delete rows from user_roles table
        userRepo.delete(userId);
    }

    @Override
    public Role saveRole(Role role) {
        if (role.getId().isPresent())
            throw new IllegalArgumentException("Updating a role is not supported");

        return roleRepo.insert(role);
    }

    @Override
    public void deleteRole(long roleId) {
        // TODO delete rows from user_roles table
        roleRepo.delete(roleId);
    }

    @Override
    public void grantRoleToUser(long userId, long roleId) throws ObjectNotFoundException {
        try {
            userRepo.getForId(userId);
            roleRepo.getForId(roleId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("User or role not found", e);
        }
        // if something else is deleting users/roles at the same time as this runs, the row can be inserted
        // with no matching user or role, but it doesn't really matter
        userRepo.insertUserRole(userId, roleId);
    }

    @Override
    public void removeRoleFromUser(long userId, long roleId) throws ObjectNotFoundException {
        try {
            userRepo.getForId(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("User not found", e);
        }
        // if something else is deleting users at the same time as this runs, the row can be inserted
        // with no matching user, but it doesn't really matter
        userRepo.deleteUserRole(userId, roleId);
    }

    @Override
    public void updateUserPassword(long userId, String currentPassword, String newPassword)
            throws ObjectNotFoundException {
        // TODO this needs to be in a transaction or update needs to update password where password = currentPassword
        Objects.requireNonNull(currentPassword, "currentPassword cannot be null");
        Objects.requireNonNull(newPassword, "newPassword cannot be null");

        User user;

        try {
            user = userRepo.getForId(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("No user for ID " + userId, e);
        }

        if (!passwordEncoder.matches(currentPassword, user.getPassword()))
            // TODO better exception
            throw new IllegalArgumentException("Invalid credentials");

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.update(user);
    }
}
