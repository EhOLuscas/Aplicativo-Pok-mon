package com.example.testeapi.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FiltroTipos(
    items: List<String>,
    selectedItem: String,
    onItemSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
       modifier = Modifier
           .fillMaxWidth(0.7f)
           .padding(16.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            )
        ) {
            Text(selectedItem.ifEmpty { "Filtrar por tipo" })
        }

        DropdownMenu(
            modifier = Modifier
                .height(500.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { type ->
                DropdownMenuItem(
                    text = { Text(text = type.replaceFirstChar { it.uppercase() }) },
                    onClick = {
                        onItemSelect(type.replaceFirstChar { it.uppercase() })
                        expanded = false
                    }
                )
            }
        }
    }
}
