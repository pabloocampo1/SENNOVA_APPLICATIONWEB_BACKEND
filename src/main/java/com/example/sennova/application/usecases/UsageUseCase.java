package com.example.sennova.application.usecases;

import com.example.sennova.domain.model.UsageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsageUseCase {
    UsageModel save(UsageModel usageModel);
    UsageModel update(Long id, UsageModel usageModel);
    List<UsageModel> getAllByName(String name);
    Page<UsageModel> getAll(Pageable pageable);
    List<UsageModel> getAll();
    UsageModel getById(Long id);
    void deleteById(Long id);
}
