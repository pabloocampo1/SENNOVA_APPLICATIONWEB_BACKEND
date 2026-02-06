package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.dto.inventory.ReagentInventory.ReagentSummaryStatistics;
import com.example.sennova.application.dto.inventory.ReagentInventory.UsageReagentRequest;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.LocationUseCase;
import com.example.sennova.application.usecases.ReagentUseCase;
import com.example.sennova.application.usecases.UsageUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.constants.ReagentStateCons;
import com.example.sennova.domain.constants.RoleConstantsNotification;
import com.example.sennova.domain.constants.TypeNotifications;
import com.example.sennova.domain.constants.UnitsOfMeasureEnum;
import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.model.ReagentModel;
import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.domain.model.UserModel;
import com.example.sennova.domain.port.ReagentPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentMediaFilesEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryReagentsEntities.ReagentsUsageRecords;
import com.example.sennova.infrastructure.persistence.repositoryJpa.ReagentMediaFileRepository;
import com.example.sennova.infrastructure.persistence.repositoryJpa.UsageReagentRepositoryJpa;
import com.example.sennova.infrastructure.restTemplate.CloudinaryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReagentServiceImpl implements ReagentUseCase {

    private final ReagentPersistencePort reagentPersistencePort;
    private final CloudinaryService cloudinaryService;
    private final LocationUseCase locationUseCase;
    private final ReagentMediaFileRepository reagentMediaFileRepository;
    private final UsageUseCase usageUseCase;
    private final UsageReagentRepositoryJpa usageReagentRepositoryJpa;
    private final NotificationsService notificationsService;

    public ReagentServiceImpl(ReagentPersistencePort reagentPersistencePort, CloudinaryService cloudinaryService, LocationUseCase locationUseCase, ReagentMediaFileRepository reagentMediaFileRepository, UsageUseCase usageUseCase, UsageReagentRepositoryJpa usageReagentRepositoryJpa, NotificationsService notificationsService) {
        this.reagentPersistencePort = reagentPersistencePort;
        this.cloudinaryService = cloudinaryService;
        this.locationUseCase = locationUseCase;
        this.reagentMediaFileRepository = reagentMediaFileRepository;
        this.usageUseCase = usageUseCase;
        this.usageReagentRepositoryJpa = usageReagentRepositoryJpa;
        this.notificationsService = notificationsService;
    }


    @Override
    public ReagentModel save(
            @Valid ReagentModel reagentModel,
            MultipartFile multipartFile,
            @Valid Long locationId,
            @Valid Long usageId, String userAction) {


        // add the location
        LocationModel locationModel = this.locationUseCase.getById(locationId);
        reagentModel.setLocation(locationModel);

        // add the usage
        UsageModel usageModel = this.usageUseCase.getById(usageId);
        reagentModel.setUsage(usageModel);


        // see the expiration date for change the state
        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = reagentModel.getExpirationDate();

        if (expirationDate.isBefore(currentDate)) {
            reagentModel.setStateExpiration(ReagentStateCons.STATE_EXPIRED);
        } else if (expirationDate.isAfter(currentDate)) {
            reagentModel.setStateExpiration(ReagentStateCons.STATE_NOT_EXPIRED);
        } else {
            reagentModel.setStateExpiration(ReagentStateCons.STATE_NOT_EXPIRED);
        }

        // validate the state of the quantity
        if (reagentModel.getQuantity() >= 1) {
            reagentModel.setState(ReagentStateCons.WITH_STOCK);
        } else {
            reagentModel.setState(ReagentStateCons.LOW_STOCK);
        }


        try {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                String imageSaved = cloudinaryService.uploadImage(multipartFile);
                reagentModel.setImageUrl(imageSaved);
            }

            // the user id is for make one notification to say who save one reagent

            return reagentPersistencePort.save(reagentModel);

        } catch (Exception e) {
            if (reagentModel.getImageUrl() != null) {
                try {
                    cloudinaryService.deleteFileByUrl(reagentModel.getImageUrl());
                } catch (Exception ex) {
                    System.out.println("Error deleting image after failure: {}" + ex.getMessage());
                }
            }
            throw new RuntimeException("Error al guardar el reactivo: " + e.getMessage(), e);
        }


    }

    @Override
    public ReagentModel saveDirect(ReagentModel reagentModel) {
        return this.reagentPersistencePort.saveDirect(reagentModel);
    }

    @Override
    @Transactional
    public ReagentModel update(ReagentModel reagentModel, Long reagentId, MultipartFile multipartFile, Long locationId, Long usageId, String userAction) {

        ReagentModel existing = reagentPersistencePort.findById(reagentId);

        existing.setReagentName(reagentModel.getReagentName());
        existing.setBrand(reagentModel.getBrand());
        existing.setPurity(reagentModel.getPurity());
        existing.setUnits(reagentModel.getUnits());
        existing.setQuantity(reagentModel.getQuantity());
        existing.setUnitOfMeasure(reagentModel.getUnitOfMeasure());
        existing.setBatch(reagentModel.getBatch());
        existing.setExpirationDate(reagentModel.getExpirationDate());
        existing.setSenaInventoryTag(reagentModel.getSenaInventoryTag());



        LocationModel locationModel = this.locationUseCase.getById(locationId);
        existing.setLocation(locationModel);

        UsageModel usageModel = this.usageUseCase.getById(usageId);
        existing.setUsage(usageModel);

        // update state

        LocalDate currentDate = LocalDate.now();
        LocalDate expirationDate = existing.getExpirationDate();

        if (expirationDate.isBefore(currentDate)) {
            existing.setStateExpiration(ReagentStateCons.STATE_EXPIRED);
        } else {
            existing.setStateExpiration(ReagentStateCons.STATE_NOT_EXPIRED);
        }

        if (existing.getQuantity() >= 1) {
            existing.setState(ReagentStateCons.WITH_STOCK);
        } else {
            existing.setState(ReagentStateCons.LOW_STOCK);
            // send and save notification
            this.saveNotification(reagentModel);
        }

        // add image or no
        if (multipartFile != null && !multipartFile.isEmpty()) {
            // eliminar la imagen anterior

            if (existing.getImageUrl() != null) {
                try {
                    cloudinaryService.deleteFileByUrl(existing.getImageUrl());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            // subir la nueva
            String newImageUrl = cloudinaryService.uploadImage(multipartFile);
            existing.setImageUrl(newImageUrl);
        }


        return reagentPersistencePort.save(existing);
    }

    @Transactional
    public void saveNotification(ReagentModel reagentModel) {
        Notifications newNotification = new Notifications();
        newNotification.setMessage("El reactivo  " + reagentModel.getReagentName() + " esta bajo de stock.");
        newNotification.setType(TypeNotifications.NEW_EQUIPMENT);
        newNotification.setActorUser("System");
        newNotification.setImageUser(null);
        newNotification.setTags(List.of(RoleConstantsNotification.ROLE_ADMIN, RoleConstantsNotification.ROLE_SUPERADMIN, RoleConstantsNotification.ROLE_ANALYST));

        this.notificationsService.saveNotification(newNotification);
    }

    @Override
    public ReagentModel getById(@Valid Long id) {
        return this.reagentPersistencePort.findById(id);
    }

    @Override
    public ReagentsEntity getEntity(Long id) {
        return this.reagentPersistencePort.findEntityById(id);
    }

    @Override
    public List<ReagentModel> getAll() {
        return this.reagentPersistencePort.findAll();
    }

    @Override
    public Page<ReagentModel> getAll(Pageable pageable) {
        return this.reagentPersistencePort.findAll(pageable);
    }

    @Override
    public List<ReagentModel> getAllByName(@Valid String name) {
        return this.reagentPersistencePort.findAllByName(name);
    }

    @Override
    public List<ReagentModel> getAllExpired() {
        LocalDate currentDate = LocalDate.now();
        return this.reagentPersistencePort.findAllByExpirationDate(currentDate);
    }

    @Override
    public List<ReagentModel> getAllByLocation(@Valid Long locationId) {
        LocationModel locationModel = this.locationUseCase.getById(locationId);
        return this.reagentPersistencePort.findAllByLocation(locationModel);
    }

    @Override
    @Transactional
    public void deleteById(@Valid Long id) {

        // check if the reagent exist.
        ReagentModel reagentModel = this.reagentPersistencePort.findById(id);

        // delete image and files from cloudinary
        List<ReagentMediaFilesEntity> files = this.reagentMediaFileRepository.findAllByReagentEntity_ReagentsId(id);

        if (!files.isEmpty()) {
            files.forEach(file -> {
                try {
                    this.cloudinaryService.deleteFile(file.getPublicId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        if (reagentModel.getImageUrl() != null && !reagentModel.getImageUrl().isEmpty()) {
            try {
                this.cloudinaryService.deleteFileByUrl(reagentModel.getImageUrl());
            } catch (Exception e) {
                System.err.println("Error deleting main image from Cloudinary: " + e.getMessage());
            }
        }

        this.reagentPersistencePort.deleteById(id);
    }

    @Override
    @Transactional
    public boolean deleteFile(String publicId) {
        ReagentMediaFilesEntity file = this.reagentMediaFileRepository.findByPublicId(publicId);
        if (file != null) {
            file.setReagentEntity(null);
            reagentMediaFileRepository.deleteById(file.getReagentFileId());
            cloudinaryService.deleteFile(publicId);
        }

        return true;
    }

    @Override
    public List<ReagentMediaFilesEntity> getFiles(@Valid Long reagentId) {
        ReagentModel reagentModel = this.reagentPersistencePort.findById(reagentId);
        return this.reagentMediaFileRepository.findAllByReagentEntity_ReagentsId(reagentModel.getReagentsId());
    }

    @Override
    @Transactional
    public List<ReagentMediaFilesEntity> uploadFiles(@Valid Long reagentId, List<MultipartFile> multipartFiles) {

        // check if the reagent exists
        ReagentsEntity reagentsEntity = this.reagentPersistencePort.findEntityById(reagentId);

        List<ReagentMediaFilesEntity> reagentMediaFilesEntities = multipartFiles
                .stream()
                .map(file -> {

                    // save the file
                    Map<String, String> fileUpload = this.cloudinaryService.uploadFile(file);

                    // create the entity media for save
                    ReagentMediaFilesEntity reagentMediaFilesEntity = new ReagentMediaFilesEntity();
                    reagentMediaFilesEntity.setNameFile(fileUpload.get("originalFilename"));
                    reagentMediaFilesEntity.setReagentEntity(reagentsEntity);
                    reagentMediaFilesEntity.setType(fileUpload.get("contentType"));
                    reagentMediaFilesEntity.setUrl(fileUpload.get("secure_url"));
                    reagentMediaFilesEntity.setPublicId(fileUpload.get("public_id"));


                    return this.reagentMediaFileRepository.save(reagentMediaFilesEntity);

                })
                .toList();

        // cretae logic
        return reagentMediaFilesEntities;
    }

    @Override
    public ReagentModel changeQuantity(Long reagentId, Integer quantity) {
        // find the reagent
        ReagentModel reagent = this.reagentPersistencePort.findById(reagentId);


        // change the state
        reagent.setQuantity(quantity);

        if (quantity >= 1) {
            reagent.setState(ReagentStateCons.WITH_STOCK);
        } else {
            reagent.setState(ReagentStateCons.LOW_STOCK);
            this.saveNotification(reagent);
        }

        if (reagent.getQuantity() <= 0) this.saveNotification(reagent);

        return this.reagentPersistencePort.save(reagent);
    }

    @Override
    public ReagentModel changeState(Long reagentId, String state) {
        // find the reagent
        ReagentModel reagent = this.reagentPersistencePort.findById(reagentId);

        switch (state) {
            case "SIN CONTENIDO":
                reagent.setState(ReagentStateCons.LOW_STOCK);
                reagent.setQuantity(0);

                // save and send notification
                this.saveNotification(reagent);
                ;

            case "CON CONTENIDO":
                reagent.setState(ReagentStateCons.WITH_STOCK);
                ;
            default:
                reagent.setState(ReagentStateCons.NO_VALUE);

        }

        return this.reagentPersistencePort.save(reagent);
    }

    @Override
    @Transactional
    public ReagentsUsageRecords saveUsage(UsageReagentRequest usageReagentRequest) {
        // get the reagent
        ReagentModel reagentModel = this.reagentPersistencePort.findById(usageReagentRequest.reagentId());

        // validate the stock
        if (usageReagentRequest.quantity() > reagentModel.getQuantity()) {
            throw new IllegalArgumentException("Cantidad inválida: la cantidad solicitada excede el número de unidades disponibles en inventario.");
        }

        double currentQuantity = reagentModel.getQuantity();

        // update the state and stock
        double newQuantity = reagentModel.getQuantity() - usageReagentRequest.quantity();
        newQuantity = Math.round(newQuantity * 100.0) / 100.0;
        reagentModel.setQuantity(newQuantity);

        if (newQuantity >= 1) {
            reagentModel.setState(ReagentStateCons.WITH_STOCK);
        } else {
            this.saveNotification(reagentModel);
            reagentModel.setState(ReagentStateCons.LOW_STOCK);
        }

        // save the reagent again.
        ReagentModel reagentUpdate = this.reagentPersistencePort.save(reagentModel);

        ReagentsUsageRecords reagentsUsageRecord = new ReagentsUsageRecords();
        reagentsUsageRecord.setNotes(usageReagentRequest.notes());
        reagentsUsageRecord.setUsedBy(usageReagentRequest.responsibleName());
        reagentsUsageRecord.setQuantity_used(usageReagentRequest.quantity());
        reagentsUsageRecord.setPreviousQuantity(currentQuantity);
        reagentsUsageRecord.setReagent(
                this.reagentPersistencePort.findEntityById(reagentUpdate.getReagentsId())
        );


        // save the record and return that data
        return this.usageReagentRepositoryJpa.save(reagentsUsageRecord);
    }

    @Override
    public List<ReagentsUsageRecords> getUsagesByReagentId(@Valid Long reagentId) {
        return this.usageReagentRepositoryJpa.findAllByReagent_ReagentsId(reagentId);
    }

    @Override
    public ReagentSummaryStatistics getSummaryStatics() {
        return this.reagentPersistencePort.getSummaryStatics();
    }
}
