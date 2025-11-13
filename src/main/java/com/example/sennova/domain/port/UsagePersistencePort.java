package com.example.sennova.domain.port;

import com.example.sennova.domain.model.UsageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsagePersistencePort {
    UsageModel save(UsageModel usageModel);
    UsageModel update(UsageModel usageModel, Long id);
    List<UsageModel> findAll();
    Page<UsageModel>findAll(Pageable pageable);
    boolean existsById(Long id);
    List<UsageModel> findAllByName(String name);
    UsageModel findById(Long id);
    void deleteById(Long id);


}
