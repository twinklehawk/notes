package net.plshark.notes.service;

import java.util.Objects;

import org.springframework.security.crypto.password.PasswordEncoder;

import net.plshark.notes.Role;
import net.plshark.notes.User;
import net.plshark.notes.repo.RoleRepository;
import net.plshark.notes.repo.UserRepository;

/**
 * UserManagementService implementation
 */
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
        this.userRepo = userRepository;
        this.roleRepo = roleRepository;
        this.passwordEncoder = passwordEncoder;
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
        roleRepo.delete(roleId);
    }

    @Override
    public void grantRoleToUser(long userId, long roleId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeRoleFromUser(long userId, long roleId) {
        // TODO Auto-generated method stub

    }

    @Override
    public void updateUserPassword(long userId, String currentPassword, String newPassword) {
        // TODO Auto-generated method stub

    }
}
