package net.plshark.notes.service;

import net.plshark.notes.Role;
import net.plshark.notes.User;
import net.plshark.notes.repo.RoleRepository;
import net.plshark.notes.repo.UserRepository;

public class UserManagementServiceImpl implements UserManagementService {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;

    public UserManagementServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepo = userRepository;
        this.roleRepo = roleRepository;
    }

    @Override
    public User saveUser(User user) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteUser(long userId) {
        // TODO Auto-generated method stub

    }

    @Override
    public Role saveRole(Role role) {
        if (role.getId().isPresent())
            throw new IllegalArgumentException("Updating a role is not supported");

        Role savedRole = roleRepo.insert(role);
        return savedRole;
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
}
