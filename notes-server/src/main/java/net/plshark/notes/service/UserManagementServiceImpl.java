package net.plshark.notes.service;

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

    /**
     * Create a new instance
     * @param userRepository the repository for accessing users
     * @param roleRepository the repository for accessing roles
     */
    public UserManagementServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepo = userRepository;
        this.roleRepo = roleRepository;
    }

    @Override
    public User saveUser(User user) {
        if (user.getId().isPresent())
            throw new IllegalArgumentException("Updating a user is not supported");

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
