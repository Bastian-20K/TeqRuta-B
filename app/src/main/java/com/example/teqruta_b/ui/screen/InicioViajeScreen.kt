package com.example.teqruta_b.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.teqruta_b.viewmodel.ViajeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioViajeScreen(
    navController: NavController,
    viewModel: ViajeViewModel
) {
    val estado by viewModel.estado.collectAsState()

    // Variables para el menú desplegable de clínicas
    var menuExpandido by remember { mutableStateOf(false) }
    val clinicas = listOf("Hospital Regional", "Sanatorio Alemán", "Clínica Biobío", "Hospital Las Higueras", "Clínica Andes Salud")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "TeqRuta - Iniciar Traslado",
            style = MaterialTheme.typography.headlineMedium
        )

        // Nuevo Campo: Técnico
        OutlinedTextField(
            value = estado.tecnico,
            onValueChange = viewModel::onTecnicoChange,
            label = { Text("Nombre del Técnico") },
            modifier = Modifier.fillMaxWidth()
        )

        // Nuevo Campo: Matrícula
        OutlinedTextField(
            value = estado.matricula,
            onValueChange = viewModel::onMatriculaChange,
            label = { Text("Matrícula del Vehículo") },
            modifier = Modifier.fillMaxWidth()
        )

        // Catastro de Clínicas (Menú Desplegable con tu manejo de errores intacto)
        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = { menuExpandido = !menuExpandido }
        ) {
            OutlinedTextField(
                value = estado.destino,
                onValueChange = {},
                readOnly = true,
                label = { Text("Clínica u Hospital destino") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpandido) },
                isError = estado.errores.destino != null,
                supportingText = {
                    estado.errores.destino?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
                    }
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = { menuExpandido = false }
            ) {
                clinicas.forEach { seleccion ->
                    DropdownMenuItem(
                        text = { Text(seleccion) },
                        onClick = {
                            viewModel.onDestinoChange(seleccion)
                            menuExpandido = false
                        }
                    )
                }
            }
        }

        // Campo Kilometraje Inicial (Intacto)
        OutlinedTextField(
            value = estado.odometroInicio,
            onValueChange = viewModel::onOdometroInicioChange,
            label = { Text("Kilometraje Inicial") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = estado.errores.odometroInicio != null,
            supportingText = {
                estado.errores.odometroInicio?.let {
                    Text(text = it, color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Selección de Vehículo (Intacto)
        Text("Vehículo asignado:", style = MaterialTheme.typography.bodyLarge)
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            RadioButton(
                selected = estado.vehiculo == "Empresa",
                onClick = { viewModel.onVehiculoChange("Empresa") }
            )
            Text("De la Empresa")

            Spacer(modifier = Modifier.width(16.dp))

            RadioButton(
                selected = estado.vehiculo == "Particular",
                onClick = { viewModel.onVehiculoChange("Particular") }
            )
            Text("Particular")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Botón Iniciar Viaje (Intacto)
        Button(
            onClick = {
                if (viewModel.validarInicioViaje()) {
                    navController.navigate("resumen")
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar Viaje")
        }
    }
}