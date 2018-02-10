package net.plshark.users.service;

import java.util.Objects;

import javax.inject.Named;
import javax.inject.Singleton;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import net.plshark.ObjectNotFoundException;
import net.plshark.users.Role;
import net.plshark.users.User;
import net.plshark.users.repo.RolesRepository;
import net.plshark.users.repo.UserRolesRepository;
import net.plshark.users.repo.UsersRepository;
import net.plshark.users.service.UserManagementService;

/**
 * UserManagementService implementation
 */
@Named
@Singleton
public class UserManagementServiceImpl implements UserManagementService {

    private final UsersRepository userRepo;
    private final RolesRepository roleRepo;
    private final UserRolesRepository userRolesRepo;
    private final PasswordEncoder passwordEncoder;

    /**
     * Create a new instance
     * @param userRepository the repository for accessing users
     * @param roleRepository the repository for accessing roles
     * @param userRolesRepo the repository for accessing user roles
     * @param passwordEncoder the encoder to use to encode passwords
     */
    public UserManagementServiceImpl(UsersRepository userRepository, RolesRepository roleRepository,
            UserRolesRepository userRolesRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = Objects.requireNonNull(userRepository, "userRepository cannot be null");
        this.roleRepo = Objects.requireNonNull(roleRepository, "roleRepository cannot be null");
        this.userRolesRepo = Objects.requireNonNull(userRolesRepo, "userRolesRepo cannot be null");
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
        userRolesRepo.deleteUserRolesForUser(userId);
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
        userRolesRepo.deleteUserRolesForRole(roleId);
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
        userRolesRepo.insertUserRole(userId, roleId);
    }

    @Override
    public void grantRoleToUser(User user, Role role) throws ObjectNotFoundException {
        grantRoleToUser(user.getId().get(), role.getId().get());
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
        userRolesRepo.deleteUserRole(userId, roleId);
    }

    @Override
    public void updateUserPassword(long userId, String currentPassword, String newPassword)
            throws ObjectNotFoundException {
        Objects.requireNonNull(currentPassword, "currentPassword cannot be null");
        Objects.requireNonNull(newPassword, "newPassword cannot be null");

        String newPasswordEncoded = passwordEncoder.encode(newPassword);
        String currentPasswordEncoded = passwordEncoder.encode(currentPassword);

        try {
            userRepo.updatePassword(userId, currentPasswordEncoded, newPasswordEncoded);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("User not found", e);
        }
    }

    @Override
    public Role getRoleByName(String name) throws ObjectNotFoundException {
        try {
            return roleRepo.getForName(name);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException("Failed to find role " + name, e);
        }
    }
}
