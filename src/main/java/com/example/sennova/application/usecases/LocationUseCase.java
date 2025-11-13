package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.LocationModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationUseCase {
    LocationModel save(LocationModel locationModel);
    LocationModel update(Long id, LocationModel locationModel);
    List<LocationModel> getAllByName(String name);
    LocationModel getById(Long id);
    void deleteById(Long id);
    Page<LocationModel> getAllPage(Pageable pageable);
    List<LocationModel> getAll();

}
