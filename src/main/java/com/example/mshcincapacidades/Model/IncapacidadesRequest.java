package com.example.mshcincapacidades.Model;

import lombok.Data;

@Data
public class IncapacidadesRequest {
   

   Boolean desc;
   ModelIncapacidadRequest model;
   String order;
   Integer page;
   Integer pageSize;   
}

