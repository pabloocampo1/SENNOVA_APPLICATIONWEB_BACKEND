package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.application.usecases.UsageUseCase;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.domain.port.EquipmentPersistencePort;
import com.example.sennova.domain.port.ReagentPersistencePort;
import com.example.sennova.domain.port.UsagePersistencePort;
import com.example.sennova.web.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsageServiceImpl implements UsageUseCase {
    private final UsagePersistencePort usagePersistencePort;
    private final EquipmentPersistencePort equipmentPersistencePort;
    private final ReagentPersistencePort reagentPersistencePort;

    @Autowired
    public UsageServiceImpl(UsagePersistencePort usagePersistencePort, EquipmentPersistencePort equipmentPersistencePort, ReagentPersistencePort reagentPersistencePort) {
        this.usagePersistencePort = usagePersistencePort;
        this.equipmentPersistencePort = equipmentPersistencePort;
        this.reagentPersistencePort = reagentPersistencePort;
    }

    @Override
    @Transactional
    public UsageModel save(@Valid UsageModel usageModel) {
        return this.usagePersistencePort.save(usageModel);
    }

    @Override
    @Transactional
    public UsageModel update(Long id, UsageModel usageModel) {
        if (!this.usagePersistencePort.existsById(id)) {
            throw new IllegalArgumentException("No se encontro el elemento con id: " + id);
        }

        if (usageModel.getEquipmentUsageId() == null) {
            throw new IllegalArgumentException("No se pudo editar el elemento, hubo un error en el sistema.: " + id);
        }
        return this.usagePersistencePort.update(usageModel, id);
    }

    @Override
    public List<UsageModel> getAllByName(@Valid String name) {
        return this.usagePersistencePort.findAllByName(name);
    }

    @Override
    public Page<UsageModel> getAll(@Valid Pageable pageable) {
        return this.usagePersistencePort.findAll(pageable);
    }

    @Override
    public List<UsageModel> getAll() {
        return this.usagePersistencePort.findAll();
    }

    @Override
    public UsageModel getById(@Valid Long id) {
        return this.usagePersistencePort.findById(id);
    }


    // Before deleting a usage, all references in the equipment entities are manually cleared
    // to avoid referential integrity errors and maintain consistency in the database.
    @Override
    @Transactional
    public void deleteById(Long id) {

        UsageModel usageModel = this.usagePersistencePort.findById(id);
        List<EquipmentModel> listEquipment = this.equipmentPersistencePort.findAllByUsage(usageModel);

        for (EquipmentModel equipment : listEquipment){
            equipment.setUsage(null);
            this.equipmentPersistencePort.save(equipment);
        }

        List<ReagentModel> reagentList = this.reagentPersistencePort.findAllByUsage(usageModel);
        for (ReagentModel reagent : reagentList){
            reagent.setUsage(null);
            this.reagentPersistencePort.save(reagent);
        }

        this.usagePersistencePort.deleteById(id);
    }
}
