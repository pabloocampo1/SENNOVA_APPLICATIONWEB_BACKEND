package com.example.sennova.domain.port;

import com.example.sennova.domain.model.RoleModel;

import java.util.List;

public interface RolePersistencePort {
   RoleModel findByName(String nameRole);
   List<RoleModel> getAll();
}
