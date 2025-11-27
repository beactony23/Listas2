package com.example.listas

import com.google.gson.annotations.SerializedName

data class ModeloRenta(
    @SerializedName("idRenta") val idRenta: Int,
    @SerializedName("idCliente") val idCliente: Int,
    @SerializedName("idTraje") val idTraje: Int,
    @SerializedName("idEmpleado") val idEmpleado: Int,

    @SerializedName("descripcionRentas") val descripcion: String,
    @SerializedName("fechaHoraInicio") val fechaInicio: String,
    @SerializedName("fechaHoraFin") val fechaFin: String
)
