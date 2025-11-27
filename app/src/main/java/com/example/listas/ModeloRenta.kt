package com.example.listas


    data class ModeloRenta(
        val idRenta: Int,
        val idCliente: Int,
        val idTraje: Int,
        val idEmpleado: Int,
        val descripcion: String,
        val fechaInicio: String,
        val fechaFin:String
    )
