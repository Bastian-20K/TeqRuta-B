package com.example.teqruta_b.model

data class ViajeUIState(
    val tecnico: String = "",
    val matricula: String = "",
    val tecnicoId: String = "",
    val vehiculoId: String = "",
    val destino: String = "",
    val vehiculo: String = "Empresa",
    val peajes: String = "",
    val horaInicio: String = "",
    val horaFin: String = "",
    val odometroInicio: String = "",
    val odometroFin: String = "",
    val errorOdometro: String? = null,
    val distanciaRecorrida: Int? = null,
    val costoTotal: Int? = null,
    val errores: ViajeErrores = ViajeErrores()
)
