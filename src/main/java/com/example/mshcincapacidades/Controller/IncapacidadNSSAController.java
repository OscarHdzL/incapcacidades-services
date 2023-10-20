package com.example.mshcincapacidades.Controller;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mshcincapacidades.Model.DelegacionRequest;
import com.example.mshcincapacidades.Model.DiagnosticoRequest;
import com.example.mshcincapacidades.Model.IncapacidadesRequest;
import com.example.mshcincapacidades.Model.ModelDelegacionRequest;
import com.example.mshcincapacidades.Model.ModelDiagnosticoRequest;
import com.example.mshcincapacidades.Model.ModelUnidadesRequest;
import com.example.mshcincapacidades.Model.UnidadRequest;
import com.example.mshcincapacidades.Model.DTO.IncapacidadDTO;
import com.example.mshcincapacidades.Service.IncapacidadNSSAService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/mshc-incapacidades/v1")
@RequiredArgsConstructor
public class IncapacidadNSSAController {

    //@Autowired
    private final IncapacidadNSSAService incapacidadNSSAService;
    

   /*  @GetMapping("/incapacidades/{folio}")
    public Optional<IncapacidadNSSA> findIncapacidadByFolio(@PathVariable String folio) {
        return incapacidadNSSAService.findIncapacidadByFolio(folio);
    } */

    @GetMapping("/incapacidades/{folio}/{nss}/{agregado_medico}")
    public IncapacidadDTO findIncapacidadByFolioNSSAgregado(@PathVariable String folio, @PathVariable String nss, @PathVariable String agregado_medico) {
        return incapacidadNSSAService.findIncapacidadByFolioNSSAgregado(folio, nss, agregado_medico);
    }

    /* TEST UNION */
    /* @GetMapping("/incapacidades/{nss}/{agregado_medico}")
    public List<IncapacidadNSSA> findAllIncapacidadesByNSS_AgregadoMedico(@PathVariable String nss, @PathVariable String agregado_medico) {
        return incapacidadNSSAService.findNSS_AgregadoMedico(nss, agregado_medico);
    } */

        /* TEST UNION */
    @PostMapping("/incapacidades/diagnosticos")
    public ResponseEntity<Object> findDiagnosticosByNssAgregado(@RequestBody DiagnosticoRequest request) throws ParseException {

        var a = incapacidadNSSAService.findDiagnosticos(request);

        return new ResponseEntity<>(a, HttpStatus.OK);
    }


    @PostMapping("/incapacidades/unidades")
    public ResponseEntity<Object> findUnidades(@RequestBody UnidadRequest request) throws ParseException {

        var a = incapacidadNSSAService.findUnidades(request);

        return new ResponseEntity<>(a, HttpStatus.OK);
    }

    @PostMapping("/incapacidades/delegaciones")
    public ResponseEntity<Object> findDelegaciones(@RequestBody DelegacionRequest request) throws ParseException {

        var a = incapacidadNSSAService.findDelegaciones(request);

        return new ResponseEntity<>(a, HttpStatus.OK);
    }



        @PostMapping("/incapacidades")
    public ResponseEntity<Object> findAllIncapacidadesByNSS_AgregadoMedico(@RequestBody IncapacidadesRequest request) throws ParseException {

        var a = incapacidadNSSAService.findNSS_AgregadoMedico(request);

        return new ResponseEntity<>(a, HttpStatus.OK);
    }


    @GetMapping("/notas/{idNota}")
    public List<Object> findNotasTest(@PathVariable String idNota) {
        
        List<Object> lista = new ArrayList<Object>(0);

        
        Nota nota1=new Nota(1,"Paciente masculino.........", "Alza termica", "Tratamiento especialiazado");
        Nota nota2=new Nota(2,"Paciente masculino.........", "Alza termica", "Tratamiento especialiazado");
        Nota nota3=new Nota(3,"Paciente masculino.........", "Alza termica", "Tratamiento especialiazado");
        lista.add(nota1);
        lista.add(nota2);
        lista.add(nota3);

        return lista;
        
    }


    public record Nota (Integer idNota, String resumen, String pronostico, String planEstudio) {}

    



/* 
    @GetMapping("/resumen/{nss}/{agregado}")
    public Optional<ResumenClinicoString> findIncapacidadByFolio(@PathVariable String nss, @PathVariable String agregado) {
        return incapacidadNSSAService.findTop1ByCC_NSSAndCC_AGREGADO_MED(nss, agregado);
    }

    @GetMapping("/listResumen/{nss}/{agregado}")
    public List<ConsultorioTurnoDTO> findNSS_AgregadoMedico(@PathVariable String nss, @PathVariable String agregado) {
        return incapacidadNSSAService.findNSS_AgregadoMedico(nss, agregado);
    } */


    /* @GetMapping("/incapacidades/{folio}")
    public Optional<List<IncapacidadNSSA>> findIncapacidadByFolio(@PathVariable String folio) {
        return incapacidadNSSAService.findIncapacidadByFolio(folio);
    } */

}
