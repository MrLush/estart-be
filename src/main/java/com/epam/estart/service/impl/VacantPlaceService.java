package com.epam.estart.service.impl;

import com.epam.estart.dto.Project;
import com.epam.estart.dto.VacantPlace;
import com.epam.estart.entity.ProjectEntity;
import com.epam.estart.entity.VacantPlaceEntity;
import com.epam.estart.repository.VacantPlaceRepository;
import java.util.Set;
import org.springframework.stereotype.Service;

@Service
public class VacantPlaceService
    extends AbstractService<Long, VacantPlace, VacantPlaceEntity, VacantPlaceRepository> {

  VacantPlaceService(VacantPlaceRepository repository) {
    super(repository);
  }

  @Override
  public Class<VacantPlaceEntity> getEntityClass() {
    return VacantPlaceEntity.class;
  }

  @Override
  public Class<VacantPlace> getDTOClass() {
    return VacantPlace.class;
  }

  public Set<VacantPlaceEntity> createAllByProjectEntity(Project project) {
    Set<VacantPlaceEntity> vacantPlaces = modelMapper.map(project, ProjectEntity.class).getVacantPlaces();
    vacantPlaces.forEach(vacantPlacesEntity -> vacantPlacesEntity.setProjectId(project.getId()));
    repository.saveAll(vacantPlaces);
    return vacantPlaces;
  }

  public void removeAll(Set<VacantPlaceEntity> vacantPlacesEntities) {
    repository.deleteAll(vacantPlacesEntities);
  }
}
