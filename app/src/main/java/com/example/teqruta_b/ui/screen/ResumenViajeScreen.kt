package com.example.teqruta_b.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.teqruta_b.viewmodel.ViajeViewModel

@Composable
fun ResumenViajeScreen(
    navController: NavController,
    viewModel: ViajeViewModel
) {
    val estado by viewModel.estado.collectAsState()
    val historialActual by viewModel.historial.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Viaje en Curso",
            style = MaterialTheme.typography.headlineMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "Técnico: ${estado.tecnico}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Matrícula: ${estado.matricula}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Destino: ${estado.destino}", style = MaterialTheme.typography.bodyLarge)
                Text(text = "Kilometraje inicial: ${estado.odometroInicio}", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // Campo para ingresar el kilometraje final
        OutlinedTextField(
            value = estado.odometroFin,
            onValueChange = viewModel::onOdometroFinChange,
            label = { Text("Kilometraje Final") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado.peajes,
            onValueChange = viewModel::onPeajesChange,
            label = { Text("Gasto en Peajes ($)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        estado.errorOdometro?.let { mensajeError ->
            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Button(
            onClick = { viewModel.finalizarViaje() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Finalizar Viaje y Calcular")
        }

        // Si la distancia ya fue calculada (no es null), la mostramos
        if (estado.distanciaRecorrida != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Resumen del Traslado", style = MaterialTheme.typography.titleMedium)
                    Text("Hora de inicio: ${estado.horaInicio}")
                    Text("Hora de término: ${estado.horaFin}")
                    Text("Vehículo: ${estado.vehiculo}")
                    Text("Distancia: ${estado.distanciaRecorrida} Kms")
                    Text("Costo Total: $${estado.costoTotal}", style = MaterialTheme.typography.headlineSmall)
                }
            }

            // Botón para limpiar pantalla y realizar un nuevo registro
            Button(
                onClick = {
                    viewModel.iniciarNuevoViaje()
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Iniciar Nuevo Traslado")
            }
        }

        // Mostrar la lista con los viajes finalizados anteriormente
        if (historialActual.isNotEmpty()) {
            Text(
                text = "Historial de Viajes Realizados (${historialActual.size}):",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                historialActual.forEach { viaje ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "Destino: ${viaje.destino}", style = MaterialTheme.typography.titleSmall)
                            Text(text = "Técnico: ${viaje.tecnico} | Matrícula: ${viaje.matricula}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Horario: ${viaje.horaInicio} - ${viaje.horaFin}", style = MaterialTheme.typography.bodySmall)
                            Text(text = "Distancia: ${viaje.distanciaRecorrida} Kms | Costo: $${viaje.costoTotal}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}