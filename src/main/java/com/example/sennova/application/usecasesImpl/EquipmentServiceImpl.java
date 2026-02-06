package com.example.sennova.application.usecasesImpl;

import com.example.sennova.application.dto.UserDtos.UserResponse;
import com.example.sennova.application.mapper.UserMapper;
import com.example.sennova.application.usecases.EquipmentUseCase;
import com.example.sennova.application.usecases.LocationUseCase;
import com.example.sennova.application.usecases.UsageUseCase;
import com.example.sennova.application.usecases.UserUseCase;
import com.example.sennova.domain.constants.EquipmentConstants;
import com.example.sennova.domain.constants.RoleConstantsNotification;
import com.example.sennova.domain.constants.TypeNotifications;
import com.example.sennova.domain.model.LocationModel;
import com.example.sennova.domain.model.EquipmentModel;
import com.example.sennova.domain.model.UsageModel;
import com.example.sennova.domain.port.EquipmentLoanPersistencePort;
import com.example.sennova.domain.port.EquipmentPersistencePort;
import com.example.sennova.domain.port.MaintenanceEquipmentPersistencePort;
import com.example.sennova.infrastructure.persistence.entities.Notifications;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentEntity;
import com.example.sennova.infrastructure.persistence.entities.inventoryEquipmentEntities.EquipmentMediaEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.EquipmentMediaRepositoryJpa;
import com.example.sennova.infrastructure.restTemplate.CloudinaryService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EquipmentServiceImpl implements EquipmentUseCase {

    private final EquipmentPersistencePort equipmentPersistencePort;
    private final LocationUseCase locationUseCase;
    private final UsageUseCase usageUseCase;
    private final CloudinaryService cloudinaryService;
    private final EquipmentMediaRepositoryJpa equipmentMediaRepositoryJpa;
    private final MaintenanceEquipmentPersistencePort maintenanceEquipmentPersistencePort;
    private final EquipmentLoanPersistencePort equipmentLoanPersistencePort;
    private final NotificationsService notificationsService;

    public EquipmentServiceImpl(EquipmentPersistencePort equipmentPersistencePort, LocationUseCase locationUseCase, UsageUseCase usageUseCase, CloudinaryService cloudinaryService, EquipmentMediaRepositoryJpa equipmentMediaRepositoryJpa, MaintenanceEquipmentPersistencePort maintenanceEquipmentPersistencePort, EquipmentLoanPersistencePort equipmentLoanPersistencePort, NotificationsService notificationsService) {
        this.equipmentPersistencePort = equipmentPersistencePort;
        this.locationUseCase = locationUseCase;
        this.usageUseCase = usageUseCase;
        this.cloudinaryService = cloudinaryService;
        this.equipmentMediaRepositoryJpa = equipmentMediaRepositoryJpa;
        this.maintenanceEquipmentPersistencePort = maintenanceEquipmentPersistencePort;
        this.equipmentLoanPersistencePort = equipmentLoanPersistencePort;
        this.notificationsService = notificationsService;
    }


    @Override
    @Transactional
    public EquipmentModel save(EquipmentModel equipmentModel, Long locationId, Long usageId, String userAction) {

        // validate the state
        String state = equipmentModel.getState();
        if (!state.equals(EquipmentConstants.STATUS_ACTIVE)
                && !state.equals(EquipmentConstants.STATUS_DECOMMISSIONED)
                && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE)) {
            throw new IllegalArgumentException("Estado de equipo inválido. Los valores permitidos son: Activo, Dado de baja, Fuera de servicio.");
        }

        // change the attribute available according the state
        equipmentModel.setAvailable(!state.equals(EquipmentConstants.STATUS_DECOMMISSIONED)
                && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE));

        // validate if the number serial is unique
        String serialNumber = equipmentModel.getSerialNumber();
        if (this.equipmentPersistencePort.existsBySerialNumber(serialNumber)) {
            throw new IllegalArgumentException("El numero serial ya existe. Debe de ser unico.");
        }

        String internalCode = equipmentModel.getInternalCode();
        if (this.equipmentPersistencePort.existsByInternalCode(internalCode)) {
            throw new IllegalArgumentException("El codigo interno del equipo: " + equipmentModel.getEquipmentName() + " debe de ser unico.");
        }


        LocationModel locationModel = this.locationUseCase.getById(locationId);
        equipmentModel.setLocation(locationModel);

        UsageModel usageModel = this.usageUseCase.getById(usageId);
        equipmentModel.setUsage(usageModel);


        // check like present
        equipmentModel.setMarkReport(false);

        EquipmentModel savedEquipment = this.equipmentPersistencePort.save(equipmentModel);


        this.saveNotification(savedEquipment, userAction);

        return savedEquipment;
    }

    @Transactional
    public void saveNotification(EquipmentModel equipmentModel, String userAction) {
        Notifications newNotification = new Notifications();
        newNotification.setMessage("Se agregó un nuevo equipo: " + equipmentModel.getEquipmentName());
        newNotification.setType(TypeNotifications.NEW_EQUIPMENT);
        newNotification.setActorUser(userAction);
        newNotification.setImageUser(null);
        newNotification.setTags(List.of(RoleConstantsNotification.ROLE_ADMIN, RoleConstantsNotification.ROLE_SUPERADMIN));

        this.notificationsService.saveNotification(newNotification);
    }


    @Override
    @Transactional
    public EquipmentModel update(@Valid EquipmentModel equipmentModel, @Valid Long id, Long locationId, Long usageId, String userAction) {

        if (equipmentModel.getEquipmentId() == null) {
            throw new IllegalArgumentException("Error en el servidor al momento de editar el equipo. Por favor intentalo mas tarde o comunica el error");
        }

        if (!equipmentModel.getEquipmentId().equals(id)) {
            throw new IllegalArgumentException("Ocurrio un error en el sistema al intentar editar el elemento: los indentificadores no coinciden");
        }

        if (!this.equipmentPersistencePort.existById(equipmentModel.getEquipmentId())) {
            throw new IllegalArgumentException("EL equipo con id: " + equipmentModel.getEquipmentId() + " no existe en la base de datos");
        }

        String state = equipmentModel.getState();
        if (!state.equals(EquipmentConstants.STATUS_ACTIVE)
                && !state.equals(EquipmentConstants.STATUS_DECOMMISSIONED)
                && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE)) {
            throw new IllegalArgumentException("Estado de equipo inválido. Los valores permitidos son: Activo, Dado de baja, Fuera de servicio.");
        }

        // change the attribute available according the state
        equipmentModel.setAvailable(!state.equals(EquipmentConstants.STATUS_DECOMMISSIONED) && !state.equals(EquipmentConstants.STATUS_OUT_OF_SERVICE));

        // obtener la entidad para editar sin cambios para comparar valores y deterkinar la logica

        EquipmentModel equipmentToEdit = this.equipmentPersistencePort.findById(id);
        equipmentModel.setCreateAt(equipmentToEdit.getCreateAt());
        equipmentToEdit.setUpdateAt(LocalDateTime.now());

        // si el numero serial es diferente, entonces buscamos que no exista en la db
        if (!equipmentModel.getSerialNumber().equals(equipmentToEdit.getSerialNumber())) {
            if (this.equipmentPersistencePort.existsBySerialNumber(equipmentModel.getSerialNumber())) {
                throw new IllegalArgumentException("El numero serial ya existe. debe de ser unico");
            }
        }

        // si el codigo interno es diferente, entonces buscamos que no exista en la db
        if (!equipmentModel.getInternalCode().equals(equipmentToEdit.getInternalCode())) {
            if (this.equipmentPersistencePort.existsByInternalCode(equipmentModel.getInternalCode())) {
                throw new IllegalArgumentException("El codigo interno del equipo: " + equipmentModel.getEquipmentName() + " Debe de ser unico");
            }
        }

        LocationModel locationModel = this.locationUseCase.getById(locationId);
        equipmentModel.setLocation(locationModel);

        UsageModel usageModel = this.usageUseCase.getById(usageId);
        equipmentModel.setUsage(usageModel);

        return this.equipmentPersistencePort.update(equipmentModel);
    }

    @Override
    public EquipmentModel getById(Long id) {
        return this.equipmentPersistencePort.findById(id);
    }

    @Override
    public List<EquipmentModel> getAllByName(String name) {
        return this.equipmentPersistencePort.findAllByName(name);
    }

    @Override
    public List<EquipmentModel> getAllByInternalCode(String internalCode) {

        return this.equipmentPersistencePort.findAllByInternalCode(internalCode);
    }

    @Override
    public List<EquipmentModel> getAllBySenaInventoryTag(String code) {
        return this.equipmentPersistencePort.findAllBySenaInventoryTag(code);
    }

    @Override
    public Page<EquipmentModel> getAll(Pageable pageable) {
        return this.equipmentPersistencePort.getAllPage(pageable);
    }

    @Override
    public List<EquipmentModel> getAll() {
        return this.equipmentPersistencePort.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {

        if (!this.equipmentPersistencePort.existById(id)) {

            throw new IllegalArgumentException("El equipo que deseas eliminar no existe.");
        }


        EquipmentModel currentEquipment = this.equipmentPersistencePort.findById(id);
        List<EquipmentMediaEntity> equipmentMediaEntityList = this.equipmentMediaRepositoryJpa.findByEquipmentId(id);

        try {
            // delete image in cloudinary
            if (currentEquipment.getImageUrl() != null) {
                cloudinaryService.deleteFileByUrl(currentEquipment.getImageUrl());
            }



            // delete files
            if (!equipmentMediaEntityList.isEmpty()) {
                for (EquipmentMediaEntity file : equipmentMediaEntityList) {
                    cloudinaryService.deleteFile(file.getPublicId());
                }
            }


            // 2. delete relationships
            this.equipmentLoanPersistencePort.deleteByEquipmentId(currentEquipment.getEquipmentId());
            this.maintenanceEquipmentPersistencePort.deleteByEquipmentId(currentEquipment.getEquipmentId());



            // 3. delete the equipment
            this.equipmentPersistencePort.delete(currentEquipment.getEquipmentId());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el equipo y sus archivos asociados", e);
        }
    }


    @Override
    public List<EquipmentModel> getByLocation(Long locationId) {
        LocationModel locationModel = this.locationUseCase.getById(locationId);
        return this.equipmentPersistencePort.findAllByLocation(locationModel);
    }

    @Override
    public List<EquipmentModel> getByUsage(Long usageId) {
        UsageModel usageModel = this.usageUseCase.getById(usageId);
        return this.equipmentPersistencePort.findAllByUsage(usageModel);
    }

    @Override
    @Transactional
    public EquipmentModel changeState(Long id, String state) {
        if (id == null || state == null) {
            throw new IllegalArgumentException("Ocurrio un error en el sistema, por favor intentalo mas tarde.");
        }
        return this.equipmentPersistencePort.changeState(id, state);

    }

    @Override
    public String changeImage(MultipartFile multipartFile, Long equipmentId) {
        try {
            EquipmentModel equipmentModel = this.getById(equipmentId);

            if (equipmentModel.getImageUrl() != null && !equipmentModel.getImageUrl().isEmpty()) {
                try {
                    Map imageDelete = this.cloudinaryService.deleteFileByUrl(equipmentModel.getImageUrl());
                } catch (Exception e) {
                    throw new RuntimeException("no se pudo eliminar al foto.");
                }
            }

            if (multipartFile.isEmpty()) {
                throw new IllegalArgumentException("no se envio un aimagen en la peticion");
            }

            String imageUrl = this.cloudinaryService.uploadImage(multipartFile);
            equipmentModel.setImageUrl(imageUrl);

            EquipmentModel equipmentModelUpdate = this.equipmentPersistencePort.save(equipmentModel);

            return equipmentModelUpdate.getImageUrl();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    @Transactional
    public List<EquipmentMediaEntity> saveFiles(List<MultipartFile> files, Long equipmentId) {
        EquipmentEntity equipment = this.equipmentPersistencePort.findEntityById(equipmentId);

        List<EquipmentMediaEntity> equipmentMediaEntities = files
                .stream()
                .map(file -> {
                    try {

                        Map<String, String> fileSaved = this.cloudinaryService.uploadFile(file);
                        EquipmentMediaEntity equipmentMediaEntity = new EquipmentMediaEntity(
                                fileSaved.get("secure_url"),
                                fileSaved.get("public_id"),
                                fileSaved.get("contentType"),
                                fileSaved.get("originalFilename"),
                                equipment
                        );

                        return this.equipmentMediaRepositoryJpa.save(equipmentMediaEntity);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }


                })
                .toList();


        return equipmentMediaEntities;
    }

    @Override
    public EquipmentEntity returnEntityById(Long id) {
        return this.equipmentPersistencePort.findEntityById(id);
    }

    @Override
    public List<EquipmentMediaEntity> getFiles(Long id) {
        return this.equipmentMediaRepositoryJpa.findByEquipmentId(id);
    }

    @Override
    public Boolean deleteFile(String public_Id) {
        this.cloudinaryService.deleteFile(public_Id);

        EquipmentMediaEntity equipmentMedia = this.equipmentMediaRepositoryJpa.findByPublicId(public_Id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro el archivo"));

        this.equipmentMediaRepositoryJpa.deleteById(equipmentMedia.getEquipmentMediaId());
        return true;
    }

    @Override
    public Map<String, Long> getSummaryStatics() {
        long count = this.equipmentPersistencePort.countTotal();
        long countAvailableTrue = this.equipmentPersistencePort.countByAvailableTrue();
        long countByMarkReportTrue = this.equipmentPersistencePort.countReported();
        long countMaintenanceMonth = this.equipmentPersistencePort.countByMaintenanceMonth();

        Map<String, Long> mapObject = new HashMap<>();
        mapObject.put("countAll", count);
        mapObject.put("countAvailableTrue", countAvailableTrue);
        mapObject.put("countReported", countByMarkReportTrue);
        mapObject.put("countMaintenanceMonth", countMaintenanceMonth);

        return mapObject;
    }

    @Override
    public List<EquipmentModel> getAllEquipmentToMaintenance() {
        LocalDate currentDate = LocalDate.now();
        return this.equipmentPersistencePort.findAllByMaintenanceDate(currentDate);
    }

    @Override
    public EquipmentModel reportEquipment(@Valid Long equipmentId) {
        EquipmentModel equipmentModel = this.equipmentPersistencePort.findById(equipmentId);
        equipmentModel.setMarkReport(true);

        return  this.equipmentPersistencePort.save(equipmentModel);
    }

    @Override
    public EquipmentModel markEquipmentAsExisting(@Valid Long equipmentId) {
        EquipmentModel equipmentModel = this.equipmentPersistencePort.findById(equipmentId);
        equipmentModel.setMarkReport(false);

        return  this.equipmentPersistencePort.save(equipmentModel);
    }

    @Override
    public List<EquipmentModel> getAllReportedEquipment() {
        return this.equipmentPersistencePort.findAllByIsPresentFalse();
    }

}
