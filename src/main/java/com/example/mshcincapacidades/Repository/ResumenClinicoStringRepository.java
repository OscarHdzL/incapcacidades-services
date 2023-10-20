package com.example.mshcincapacidades.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;


import com.example.mshcincapacidades.Model.ResumenClinicoString;

public interface ResumenClinicoStringRepository extends MongoRepository<ResumenClinicoString, String>{
    Optional<ResumenClinicoString> findTop1ByNssAndAgregadoMedico(String cc_nss, String cc_agregado_med);


    
}
