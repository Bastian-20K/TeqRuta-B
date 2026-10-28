package com.example.teqruta_b.viewmodel

import androidx.lifecycle.ViewModel
import com.example.teqruta_b.model.ViajeUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViajeViewModel : ViewModel() {

    private val _estado = MutableStateFlow(ViajeUIState())
    val estado: StateFlow<ViajeUIState> = _estado.asStateFlow()

    private val _historial = MutableStateFlow<List<ViajeUIState>>(emptyList())
    val historial = _historial.asStateFlow()

    fun onTecnicoChange(nuevoTecnico: String) {
        _estado.update { it.copy(tecnico = nuevoTecnico) }
    }

    fun onMatriculaChange(nuevaMatricula: String) {
        _estado.update { it.copy(matricula = nuevaMatricula) }
    }

    fun onDestinoChange(nuevoDestino: String) {
        _estado.update { it.copy(destino = nuevoDestino) }
    }

    fun onOdometroInicioChange(nuevoOdometro: String) {
        _estado.update { it.copy(odometroInicio = nuevoOdometro) }
    }

    fun onOdometroFinChange(nuevoOdometro: String) {
        _estado.update { it.copy(odometroFin = nuevoOdometro) }
    }

    fun onVehiculoChange(nuevoVehiculo: String) {
        _estado.update { it.copy(vehiculo = nuevoVehiculo) }
    }

    fun onPeajesChange(nuevosPeajes: String) {
        _estado.update { it.copy(peajes = nuevosPeajes) }
    }

    fun validarInicioViaje(): Boolean {
        val horaActual = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())
        _estado.update { it.copy(horaInicio = horaActual) }
        return true
    }

    fun finalizarViaje() {
        val estadoActual = _estado.value
        val inicio = estadoActual.odometroInicio.toIntOrNull() ?: 0
        val fin = estadoActual.odometroFin.toIntOrNull() ?: 0
        val costoPeajes = estadoActual.peajes.toIntOrNull() ?: 0

        if (fin < inicio) {
            _estado.update {
                it.copy(errorOdometro = "El kilometraje final debe ser mayor al inicial", distanciaRecorrida = null, costoTotal = null)
            }
        } else {
            val distancia = fin - inicio
            val tarifaPorKm = if (estadoActual.vehiculo == "Particular") 300 else 150
            val costo = (distancia * tarifaPorKm) + costoPeajes
            val horaActual = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault()).format(java.util.Date())

            _historial.update { historialActual ->
                historialActual + estadoActual.copy(distanciaRecorrida = distancia, costoTotal = costo, horaFin = horaActual)
            }

            _estado.update {
                it.copy(distanciaRecorrida = distancia, costoTotal = costo, horaFin = horaActual, errorOdometro = null)
            }
        }
    }

    fun iniciarNuevoViaje() {
        _estado.value = ViajeUIState()
    }
}