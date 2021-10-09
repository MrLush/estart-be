package com.epam.estart.service.impl;

import com.epam.estart.dto.User;
import com.epam.estart.entity.UserEntity;
import com.epam.estart.entity.UserRoleEntity;
import com.epam.estart.entity.UserTagEntity;
import com.epam.estart.repository.UserRepository;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<UUID, User, UserEntity, UserRepository> {
  private final UserRoleService userRoleService;
  private final UserTagService userTagService;

  UserService(UserRepository repository, UserRoleService userRoleService, UserTagService userTagService) {
    super(repository);
    this.userRoleService = userRoleService;
    this.userTagService = userTagService;
  }

  @Override
  public Class<UserEntity> getEntityClass() {
    return UserEntity.class;
  }

  @Override
  public Class<User> getDTOClass() {
    return User.class;
  }

  @Override
  public User create(User user) {
    UserEntity userEntity = modelMapper.map(user, UserEntity.class);
    userEntity = repository.save(userEntity);
    userRoleService.createAllByUserEntity(userEntity);
    userTagService.createAllByUserEntity(userEntity);
    return getById(userEntity.getId());
  }

  public User updateAndReturn(User user) {
    UserEntity userEntity = repository.findById(user.getId())
        .orElseThrow(() -> new IllegalArgumentException(String.format("User with id=%s not found!", user.getId())));
    userEntity.setFirstName(user.getFirstName())
        .setLastName(user.getLastName())
        .setEmail(user.getEmail())
        .setAboutMe(user.getAboutMe())
        .setHardSkills(user.getHardSkills())
        .setMainRole(user.getMainRole());
    updateUserRoles(user, userEntity);
    updateUserTags(user, userEntity);
    return getById(userEntity.getId());
  }

  private void updateUserRoles(User user, UserEntity userEntity) {
    Set<UserRoleEntity> oldUserRoles = userEntity.getRoles();
    Set<UserRoleEntity> newUserRoles = user.getRoles().stream()
        .map(s -> new UserRoleEntity().setUserId(user.getId()).setName(s))
        .collect(Collectors.toSet());
    oldUserRoles.removeAll(newUserRoles);
    userRoleService.removeAll(oldUserRoles);
    userEntity.setRoles(newUserRoles);
    userRoleService.createAllByUserEntity(userEntity);
  }

  private void updateUserTags(User user, UserEntity userEntity) {
    Set<UserTagEntity> oldUserTags = userEntity.getTags();
    Set<UserTagEntity> newUserTags = user.getTags().stream()
        .map(s -> new UserTagEntity().setUserId(user.getId()).setName(s))
        .collect(Collectors.toSet());
    oldUserTags.removeAll(newUserTags);
    userTagService.removeAll(oldUserTags);
    userEntity.setTags(newUserTags);
    userTagService.createAllByUserEntity(userEntity);
  }
}
