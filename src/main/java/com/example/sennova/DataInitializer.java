package com.example.sennova;

import com.example.sennova.infrastructure.persistence.entities.Analisys.AnalysisEntity;
import com.example.sennova.infrastructure.persistence.entities.LocationEntity;
import com.example.sennova.infrastructure.persistence.entities.RoleEntity;
import com.example.sennova.infrastructure.persistence.entities.UsageEntity;
import com.example.sennova.infrastructure.persistence.entities.UserEntity;
import com.example.sennova.infrastructure.persistence.repositoryJpa.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepositoryJpa roleRepository;
    private final UserRepositoryJpa userRepository;
    private final UsageRepositoryJpa usoRepository;
    private final LocationPersistenceJpa ubicacionRepository;
    private final ProductRepositoryJpa productoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createRoles();
        createSuperAdmin();
        createUsos();
        createUbicaciones();
        createAnalisis();

        System.out.println("Datos base verificados correctamente.");
    }

    private void createRoles() {

        createRoleIfNotExists("SUPERADMIN");
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("ANALYST");
    }

    private void createRoleIfNotExists(String roleName) {
        if (!roleRepository.existsByName(roleName)) {
            RoleEntity role = new RoleEntity();
            role.setNameRole(roleName);
            roleRepository.save(role);
        }
    }

    private void createSuperAdmin() {

        if (!userRepository.existsByEmail("softwaresennovaInfo@gmail.com")) {

            RoleEntity superRole = roleRepository.findByNameRole("SUPERADMIN")
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

            UserEntity user = new UserEntity();
            user.setName("Super Admin - edit");
            user.setEmail("softwaresennovaInfo@gmail.com");
            user.setUsername("root");
            user.setAvailable(true);
            user.setName("SUPER ADMINISTRADOR - EDITAR");
            user.setPassword(passwordEncoder.encode("root"));
            user.setRole(superRole);

            userRepository.save(user);
        }
    }

    private void createUsos() {

        List<String> usosBase = List.of(
                "Oficinas",
                "Masa",
                "Volumen",
                "Temperatura",
                "Temperatura y Humedad",
                "Potenciométrico",
                "Presión",
                "Presión y temperatura",
                "RPM",
                "Temperatura y RPM",
                "Masa y temperatura",
                "Humedad",
                "Tiempo",
                "Caudal",
                "Óptica",
                "Contador colonias",
                "Voltaje",
                "Bioluminiscencia",
                "Instrumental",
                "Dispersión luz",
                "Cómputo"
        );

        for (String nombre : usosBase) {

            if (!usoRepository.existsByUsageName(nombre)) {

                UsageEntity uso = new UsageEntity();
                uso.setUsageName(nombre);

                usoRepository.save(uso);
            }
        }
    }


    private void createUbicaciones() {

        List<String> ubicacionesBase = List.of(
                "Oficinas",
                "Microbiología",
                "Microbiología: área de siembra e incubación",
                "Microbiología: área de lavado y descarte",
                "Química húmeda",
                "ICP-OES",
                "Lavado",
                "Analítica",
                "Balanzas",
                "Bodega de reactivos",
                "Preparación de muestras",
                "HPLC",
                "UPS",
                "Estufas",
                "Zona consumibles",
                "Recepción de muestras",
                "Metrología",
                "Parte externa del laboratorio",
                "Pasillo",
                "Bodega bioreactor",
                "Cuarto aire acondicionado laboratorio",
                "Cuarto aire acondicionado microbiología",
                "Cuarto de gases"
        );

        for (String nombre : ubicacionesBase) {

            if (!ubicacionRepository.existsByLocationName(nombre)) {

                LocationEntity ubicacion = new LocationEntity();
                ubicacion.setLocationName(nombre);

                ubicacionRepository.save(ubicacion);
            }
        }
    }


    private void createAnalisis() {

        List<AnalysisEntity> analisisBase = List.of(

                new AnalysisEntity("PH", true, "equipo de prueba", "ISO 6222:1999", "producto para analisis", 300000.0, "2"),
                new AnalysisEntity("Cloro", true, "null", "32323jds iso", "", 300000.0, "3"),
                new AnalysisEntity("Acidez titulable", true, "Titulométrico", "NTC 5247:2004", null, 47011.78, "mg Ácido clorogénico/g"),
                new AnalysisEntity("Brix", true, "Refractómetro", "Escala Brix", null, 16507.98, "g sacarosa/g muestra"),
                new AnalysisEntity("Alcalinidad total", true, "Titulométrico", "Standard Methods SM-2320", null, 15008.88, "mg CaCO3/L"),
                new AnalysisEntity("Aluminio (Al)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Al/L"),
                new AnalysisEntity("Cadmio (Cd)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Cd/L"),
                new AnalysisEntity("Cafeína", true, "HPLC-DAD", "ISO 20481:2010/Modificado", null, 121506.0, "mg/100g"),
                new AnalysisEntity("Calcio (Ca)", true, "ICP-OES", "EPA 3051", null, 98029.57, "mg Ca/L"),
                new AnalysisEntity("Capacidad Antioxidante", true, "UV-VIS", "Método FRAP", null, 247430.4, "meq Trolox"),
                new AnalysisEntity("Catequina", true, "HPLC-DAD", "Método propio", "notas de prueba", 123715.2, "mg/100g"),
                new AnalysisEntity("Ceniza", true, "Gravimétrico", "NTC 4648:2022", null, 31515.82, "% Ceniza"),
                new AnalysisEntity("Cinc (Zn)", true, "ICP-OES", "EPA 3051", "", 94259.2, "mg Zn/L"),
                new AnalysisEntity("Cloro residual", true, "Fotométrico", "Standard Methods SM-4500", null, 12990.1, "mg/L"),
                new AnalysisEntity("Cloruros", true, "Método de Mohr", "Standard Methods SM-4500", null, 23564.8, "mg Cl-/L"),
                new AnalysisEntity("Cobre (Cu)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Cu/L"),
                new AnalysisEntity("Coliformes totales y Escherichia Coli", true, "Recuento en placa", "NTC 4458:2018", null, 41101.64, "UFC/mL ó g"),
                new AnalysisEntity("Colorimetría", true, "Sensorial", "Escala Pfound", null, 36662.2, "mm Pfound"),
                new AnalysisEntity("Conductividad eléctrica", true, "Potenciométrico", "Standard Methods SM-2510", null, 18009.19, "µS/cm"),
                new AnalysisEntity("Cromo (Cr)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Cr/L"),
                new AnalysisEntity("DQO", true, "Titulométrico", "Standard Methods SM-5220D", null, 61857.6, "mg oxígeno/L"),
                new AnalysisEntity("Densidad real", true, "Picnómetro", "NTC 1974:2003", null, 32165.95, "g/mL"),
                new AnalysisEntity("Dureza total", true, "Titulométrico", "Standard Methods SM-2340 C", null, 26508.3, "mg CaCO3/L"),
                new AnalysisEntity("Volátiles", true, "GC-MS", "Método propio", null, 123715.2, "%"),
                new AnalysisEntity("Epicatequina", true, "HPLC-DAD", "Método propio", null, 123715.2, "mg/100g"),
                new AnalysisEntity("HMF", true, "Espectrofotometría", "AOAC 980.23", null, 43973.6, "mEq de ácido/kg"),
                new AnalysisEntity("Hierro (Fe)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Fe/L"),
                new AnalysisEntity("Humedad (alimentos)", true, "Gravimétrico", "NTC 529:2009", null, 19510.39, "% Humedad"),
                new AnalysisEntity("Humedad (suelo)", true, "Gravimétrico", "NTC 6230:2017", null, 18006.03, "% Humedad"),
                new AnalysisEntity("Magnesio (Mg)", true, "ICP-OES", "EPA 3051", null, 98029.57, "mg Mg/L"),
                new AnalysisEntity("Manganeso (Mn)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Mn/L"),
                new AnalysisEntity("Materia seca", true, "Gravimétrico", "NTC 4888:2000", null, 16507.98, "% Humedad"),
                new AnalysisEntity("Mercurio (Hg)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Hg/L"),
                new AnalysisEntity("Molibdeno (Mo)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Mo/L"),
                new AnalysisEntity("Niquel (Ni)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Ni/L"),
                new AnalysisEntity("Perfil de ácidos orgánicos", true, "HPLC-MS/HPLC-DAD", "Método interno", null, 396198.98, "mg/100g"),
                new AnalysisEntity("Perfil de alcaloides", true, "HPLC-DAD", "ISO 20481:2010/Modificado", null, 399760.0, "mg/100g"),
                new AnalysisEntity("pH (Agua)", true, "Potenciométrico", "Standard Methods SM-4500H", null, 12005.42, "U de pH"),
                new AnalysisEntity("pH (Suelo)", true, "Potenciométrico", "NTC 5264:2023", null, 16507.98, "U de pH"),
                new AnalysisEntity("pH (Alimentos)", true, "Potenciométrico", "Método propio", null, 16507.98, "U de pH"),
                new AnalysisEntity("Plomo (Pb)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Pb/L"),
                new AnalysisEntity("Potasio (K)", true, "ICP-OES", "EPA 3051", null, 98029.57, "mg K/L"),
                new AnalysisEntity("Recuento coliformes totales", true, "Recuento en placa", "NTC 4458:2018", null, 25512.05, "UFC/cm²"),
                new AnalysisEntity("Salmonella sp.", true, "Presencia/Ausencia", "AOAC-RI 960801", null, 56047.4, "Ausencia en 25 g"),
                new AnalysisEntity("Selenio (Se)", true, "ICP-OES", "EPA 3051", null, 98029.57, "mg Se/L"),
                new AnalysisEntity("Sodio (Na)", true, "ICP-OES", "EPA 3051", null, 94259.2, "mg Na/L"),
                new AnalysisEntity("Sólidos insolubles", true, "Gravimetría", "MAFF Validated Method V22", null, 24177.06, "g"),
                new AnalysisEntity("Sólidos totales", true, "Gravimétrico", "Standard Methods SM-2540B", null, 28454.5, "mg/L"),
                new AnalysisEntity("Teobromina", true, "HPLC-DAD", "ISO 20481:2010/Modificado", null, 121506.0, "% m/m"),
                new AnalysisEntity("Teofilina", true, "HPLC-DAD", "ISO 20481:2010/Modificado", null, 121506.0, "% m/m"),
                new AnalysisEntity("Trigonelina", true, "HPLC-DAD", "ISO 20481:2010/Modificado", null, 121506.0, "% m/m"),
                new AnalysisEntity("Turbiedad", true, "Nefelométrico", "Standard Methods SM-2130", null, 15008.88, "UNT"),
                new AnalysisEntity("prueba", true, "HPLC-DAD", "Método propio", "dsdsd", 2300.0, "mg/100g")


                // Puedes seguir agregando los demás aquí
        );

        for (AnalysisEntity seed : analisisBase) {

            if (!productoRepository.existsByAnalysisName(seed.getAnalysisName())) {

                AnalysisEntity analysis = new AnalysisEntity();
                analysis.setAnalysisName(seed.getAnalysisName());
                analysis.setAvailable(seed.getAvailable());
                analysis.setPrice(seed.getPrice());
                analysis.setUnits(seed.getUnits());
                analysis.setMethod(seed.getMethod());
                analysis.setEquipment(seed.getEquipment());
                analysis.setNotes(seed.getNotes());

                productoRepository.save(analysis);
            }
        }
    }

}

