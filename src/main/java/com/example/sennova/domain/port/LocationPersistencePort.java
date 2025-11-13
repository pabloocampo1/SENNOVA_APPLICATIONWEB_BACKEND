package com.example.sennova.domain.port;

import com.example.sennova.domain.model.LocationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationPersistencePort {
    LocationModel save(LocationModel locationModel);
    LocationModel findById(Long id);
    LocationModel update(LocationModel locationModel, Long id);
    Page<LocationModel> findAllPage(Pageable pageable);
    void deleteById(Long id);
    Boolean existById(Long id);
    List<LocationModel> findAllByName(String name);
    List<LocationModel> findAll();

}
