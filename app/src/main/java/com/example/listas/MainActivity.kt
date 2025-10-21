package com.example.listas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listas.ui.theme.ListasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                        innerPadding -> AppContent(
                    modifier = Modifier.padding(innerPadding)
                )
                }
            }
        }
    }
}

@Composable
fun AppContent(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val userRole = remember { mutableStateOf("cliente") }

    NavHost(navController = navController, startDestination = "frmReservas") {
        composable("Login") { LoginContent(navController, modifier, userRole) }
        composable("menu") { MenuContent(navController, modifier) }
        composable("menuadmin") { MenuAdminContent(navController, modifier) }
        composable("lstReservas") { LstReservasContent(navController, modifier, userRole) }
        composable("frmReservas") { frmreservasContent(modifier, navController, userRole) }
        composable("lstTrajes") { LstTrajesContent(navController, modifier, userRole) }
        composable("frmTrajes") { frmTrajesContent(navController, modifier, userRole) }
        composable("lstClientes") { LstClientesContent(navController, modifier, userRole) }
        composable("frmClientes") { frmClientesContent(modifier, navController, userRole) }
        composable("lstRentas") { LstRentasContent(navController, modifier, userRole) }
        composable("frmRentas") { frmRentasContent(navController, modifier, userRole) }
        composable("lstPagos") { LstPagosContent(navController, modifier, userRole) }
        composable("frmPagos") { frmPagosContent(modifier, navController, userRole) }
        composable("lstEmpleados") { LstEmpleadosContent(navController, modifier, userRole) }
        composable("frmEmpleados") { frmEmpleadosContent(modifier, navController, userRole) }

    }
}@Composable
fun LoginContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    val context = LocalContext.current

    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Inicio de Sesión",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Usuario:")
        TextField(
            value = usuario,
            onValueChange = { usuario = it },
            placeholder = { Text("Ingresa tu usuario") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Contraseña:")
        TextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            placeholder = { Text("Ingresa tu contraseña") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val usuarioCorrecto = "Android"
                val contrasenaCorrecta = "12345"
                val usuarioAdmin = "Admin"
                val contraseñaAdmin = "54321"

                // --- LÓGICA DE LOGIN CORREGIDA ---
                if (usuario == usuarioAdmin && contrasena == contraseñaAdmin) {
                    Toast.makeText(context, "¡Bienvenido, ${usuario}!", Toast.LENGTH_SHORT).show()
                    userRole.value = "admin" // <--- ¡AQUÍ! Se actualiza el estado
                    navController.navigate("menuadmin")
                } else if (usuario == usuarioCorrecto && contrasena == contrasenaCorrecta) {
                    Toast.makeText(context, "¡Bienvenido, ${usuario}!", Toast.LENGTH_SHORT).show()
                    userRole.value = "cliente" // <--- ¡AQUÍ! Se actualiza el estado
                    navController.navigate("menu")
                } else {
                    Toast.makeText(context, "Usuario o contraseña incorrectos", Toast.LENGTH_LONG).show()
                }
                // --- SE ELIMINÓ EL SEGUNDO IF REDUNDANTE ---
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Iniciar sesión")
        }
    }
}

@Composable
fun MenuContent(navController: NavHostController, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("Login")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Login",
                fontFamily = FontFamily.Serif,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text(
            text = "Menú",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                navController.navigate("lstReservas")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text("Reservas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstTrajes")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text("Trajes")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstRentas")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text("Rentas")
        }

    }
}

@Composable
fun MenuAdminContent(navController: NavHostController, modifier: Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                navController.navigate("Login")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Blue
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Login",
                fontFamily = FontFamily.Serif,
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Text(
            text = "Menú Admin",
            fontFamily = FontFamily.Serif,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                navController.navigate("lstReservas")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text("Reservas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstTrajes")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text("Trajes")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstClientes")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text("Clientes")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstRentas")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text("Rentas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstPagos")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text("Pagos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate("lstEmpleados")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {

            Text("Empleados")
        }
    }
}

@Composable
fun LstReservasContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    data class Reserva(val idReserva: Int, val idCliente: Int, val idTraje: Int, val Fecha: String, val Estado: String)
    val reserva = remember {
        mutableStateListOf(
            Reserva(1, 1, 23, "09-10-2025" ,"Activo"),
            Reserva(2, 2, 23, "10-10-2025" ,"Activo"),
            Reserva(3, 3, 23, "10-10-2025" ,"Activo")
        )
    }
    // productos[index] = Producto(nombre, precio, existencias)
    // productos[2] = Producto("Florentinas Fresa", 20.0, 5)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Separa los botones
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para ir al Menú
            Button(
                onClick = { if (userRole.value == "admin") {
                    navController.navigate("menuadmin")
                } else {
                    navController.navigate("menu")
                }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Menú",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }

            Button(
                onClick = { navController.navigate("frmReservas") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Formulario Reservas",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                reserva.add(Reserva(4, 4, 23, "10-10-2025" ,"En cliente"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Producto Prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Id Reserva", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Id Cliente", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Id Traje", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Fecha de Pago", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Estado", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Eliminar", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

        }
        Divider()
        reserva.forEachIndexed { index, Reserva ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text(
                    "${Reserva.idReserva}", modifier = Modifier
                        .width(150.dp)
                )
                Text(
                    "${Reserva.idCliente}", modifier = Modifier
                        .width(150.dp)
                )
                Text(
                    "${Reserva.idTraje}", modifier = Modifier
                        .width(150.dp)
                )
                Text(
                    Reserva.Fecha, modifier = Modifier
                        .width(150.dp)
                )
                Text(
                    Reserva.Estado, modifier = Modifier
                        .width(150.dp)
                )

                Button(onClick = {
                    reserva.removeAt(index)
                    //Remueve en base al index del producto en la lista
                })
                {
                    Text("Eliminar")
                }

            }
        }

    }
}
@Composable
fun frmreservasContent(modifier: Modifier = Modifier, Controller: NavHostController, userRole: androidx.compose.runtime.MutableState<String>
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var idReserva by remember { mutableStateOf("") }
    var idCliente by remember { mutableStateOf("") }
    var idTraje by remember { mutableStateOf("") }
    var fechaReserva by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
        .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row {
            Button(
                onClick = { if (userRole.value == "admin") {
                    Controller.navigate("menuadmin")
                } else {
                    Controller.navigate("menu")
                }
                }, // Se navega al menú
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue),
            ) {
                Text(
                    text = "Regresar al Menú",
                    fontFamily = FontFamily.Serif,
                )

            }
            Button(
                onClick = { Controller.navigate("lstReservas") }, // Se navega a la tabla
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
            ) {
                Text(text = "Regresar a Reservas",
                    fontFamily = FontFamily.Serif, )
            }
        }

        Spacer(Modifier.height(100.dp))
        Text(
            text = "Formulario de Reservas",
            modifier = Modifier
                .padding(24.dp)
                .align(alignment = Alignment.CenterHorizontally),
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )

        if (userRole.value == "admin") {
            Text(text = "ID Reserva:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idReserva,
            onValueChange = { idReserva = it },
            placeholder = { Text("Id de la reserva") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))
        } else {
        }

        Text(text = "id Cliente:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idCliente,
            onValueChange = { idCliente = it },
            placeholder = { Text("Id del cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "id Traje:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idTraje,
            onValueChange = { idTraje = it },
            placeholder = { Text("Id del Traje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Fecha Reserva:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = fechaReserva,
            onValueChange = { fechaReserva = it },
            placeholder = { Text("Fecha de la reserva") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        if (userRole.value == "admin") {
        Text(text = "Estado:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = estado,
            onValueChange = { estado = it },
            placeholder = { Text("Estado de la reserva (Activo o Inactivo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))
        } else {

        }

        Button(
            onClick = {
                if (userRole.value == "admin") {
                Toast.makeText(context, "Id Reserva: $idReserva", Toast.LENGTH_SHORT).show()
                } else {
                }
                Toast.makeText(context, "Id Cliente: $idCliente", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Id Traje: $idTraje", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "fechaReserva: $fechaReserva", Toast.LENGTH_SHORT).show()
                if (userRole.value == "admin") {
                Toast.makeText(context, "Estado: $estado", Toast.LENGTH_SHORT).show()
                } else {
                }
            },
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            Text(text = "Enviar")
        }
    }
}

@Composable
fun LstTrajesContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    data class Trajes(val idTraje: Int, val Nombre: String, val descripcion: String)
    val trajes = remember {
        mutableStateListOf(
            Trajes(1, "Traje ébano", "Corte clásico, color negro intenso."),
            Trajes(2, "Azul Noche", "Formal y moderno, tono azul marino."),
            Trajes(3, "Blanco Perla", "Ligero y fresco, para eventos de día.")
        )
    }
    // productos[index] = Producto(nombre, precio, existencias)
    // productos[2] = Producto("Florentinas Fresa", 20.0, 5)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Separa los botones
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para ir al Menú
            Button(
                onClick = { if (userRole.value == "admin") {
                    navController.navigate("menuadmin")
                } else {
                    navController.navigate("menu")
                }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Menú",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }

            Button(
                onClick = { navController.navigate("frmTrajes") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Formulario Trajes",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                trajes.add(Trajes(4, "Gris Urbano", "Elegante, ideal para oficina."))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Producto Prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text("Id Traje", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Nombre", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Descripción", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }
        Divider()
        trajes.forEachIndexed { index, Trajes ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text("${Trajes.idTraje}", modifier = Modifier
                    .width(150.dp)
                )
                Text("${Trajes.Nombre}", modifier = Modifier
                    .width(100.dp)
                )
                Text("${Trajes.descripcion}", modifier = Modifier
                    .width(100.dp)
                )
                Button(onClick = {
                    trajes.removeAt(index)
                    //Remueve en base al index del producto en la lista
                }) {
                    Text("Eliminar")
                }
            }
        }

    }
}
@Composable
fun frmTrajesContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>
) {
    val context = LocalContext.current

    var idTraje by remember { mutableStateOf("") }
    var NombreTraje by remember { mutableStateOf("") }
    var Descripcion by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row {
            Button(
                onClick = { if (userRole.value == "admin") {
                    navController.navigate("menuadmin")
                } else {
                    navController.navigate("menu")
                }
                }, // Se navega al menú
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
            ) {
                Text(text = "Regresar al Menú")
            }
            Button(
                onClick = { navController.navigate("lstTrajes") }, // Se navega a la tabla
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
            ) {
                Text(text = "Regresar a Trajes")
            }
        }

        Spacer(Modifier.height(100.dp))
        Text(
            text = "Formulario de trajes",
            modifier = Modifier
                .padding(24.dp)
                .align(alignment = Alignment.CenterHorizontally),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )

        Text(text = "Id Traje:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idTraje,
            onValueChange = { idTraje = it },
            placeholder = { Text("Id Traje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Nombre traje:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = NombreTraje,
            onValueChange = { NombreTraje = it },
            placeholder = { Text("Nombre del Traje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Descripcion:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = Descripcion,
            onValueChange = { Descripcion = it },
            placeholder = { Text("Descripcion") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))



        Button(
            onClick = {
                Toast.makeText(context, "Id Traje: $idTraje", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Nombre Del Traje: $NombreTraje", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Descripcion: $Descripcion", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            Text(text = "Enviar")
        }
    }
}


@Composable
fun LstClientesContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    data class Cliente(
        val id_Cliente: Int,
        val Nombre: String,
        val Telefono: String,
        val Correo_electronico: String
    )

    val clientes = remember {
        mutableStateListOf(
            Cliente(1, "Juan Pérez", "5551234567", "juan@mail.com"),
            Cliente(2, "María López", "5559876543", "maria@mail.com"),
            Cliente(3, "Carlos Sánchez", "5551112222", "carlos@mail.com")
        )
    }

    var nextId by remember { mutableStateOf(clientes.size + 1) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Separa los botones
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para ir al Menú
            Button(
                onClick = { if (userRole.value == "admin") {
                    navController.navigate("menuadmin")
                } else {
                    navController.navigate("menu")
                }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Menú",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }

            Button(
                onClick = { navController.navigate("frmClientes") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Formulario Clientes",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                clientes.add(
                    Cliente(nextId, "Nuevo Cliente", "5550000000", "nuevo@mail.com")
                )
                nextId++
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Cliente Prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("ID", modifier = Modifier.width(50.dp), fontWeight = FontWeight.Bold)
            Text("Nombre", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Teléfono", modifier = Modifier.width(120.dp), fontWeight = FontWeight.Bold)
            Text("Correo", modifier = Modifier.width(180.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }

        Divider()

        clientes.forEachIndexed { index, cliente ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row(
                modifier = Modifier
                    .background(bgColor)
                    .padding(vertical = 4.dp)
            ) {
                Text("${cliente.id_Cliente}", modifier = Modifier.width(50.dp))
                Text(cliente.Nombre, modifier = Modifier.width(150.dp))
                Text(cliente.Telefono, modifier = Modifier.width(120.dp))
                Text(cliente.Correo_electronico, modifier = Modifier.width(180.dp))
                Button(onClick = { clientes.removeAt(index) }) {
                    Text("Eliminar")
                }
            }
            }
        }
}
@Composable
fun frmClientesContent(modifier: Modifier = Modifier, controller: NavHostController, userRole: androidx.compose.runtime.MutableState<String>
) {
    val context = LocalContext.current

    var idCliente by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        // Botones de navegación
        Row {
            Button(
                onClick = { if (userRole.value == "admin") {
                    controller.navigate("menuadmin")
                } else {
                    controller.navigate("menu")
                }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(text = "Regresar al Menú")
            }
            Button(
                onClick = { controller.navigate("lstClientes") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(text = "Regresar a Clientes")
            }
        }

        Spacer(Modifier.height(100.dp))

        // Título del formulario
        Text(
            text = "Formulario de Clientes",
            modifier = Modifier
                .padding(24.dp)
                .align(alignment = Alignment.CenterHorizontally),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )

        // ID Cliente
        Text(text = "ID Cliente:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idCliente,
            onValueChange = { idCliente = it },
            placeholder = { Text("ID del cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        // Nombre
        Text(text = "Nombre:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            placeholder = { Text("Nombre completo del cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        // Teléfono
        Text(text = "Teléfono:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            placeholder = { Text("Ejemplo: 5551234567") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        // Correo electrónico
        Text(text = "Correo Electrónico:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = correo,
            onValueChange = { correo = it },
            placeholder = { Text("correo@ejemplo.com") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        // Botón de envío
        Button(
            onClick = {
                Toast.makeText(context, "ID Cliente: $idCliente", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Nombre: $nombre", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Teléfono: $telefono", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Correo: $correo", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            Text(text = "Guardar Cliente")
        }
    }
}

@Composable
fun LstRentasContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    data class Rentas(val idRenta: Int, val idCliente: Int, val idTraje: Int, val Descripcion: String, val fechaHoraInicio: String, val fechaHoraFin: String)
    val rentas = remember {
        mutableStateListOf(
            Rentas(1, 3, 8, "Traje bonito azul", "12/3/04","5/6/78"),
            Rentas(2, 20, 8, "Traje bonito azul", "12/3/04","5/6/78"),
            Rentas(3, 23, 8, "Traje bonito azul", "12/3/04","5/6/78")
        )
    }
    // productos[index] = Producto(nombre, precio, existencias)
    // productos[2] = Producto("Florentinas Fresa", 20.0, 5)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Separa los botones
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para ir al Menú
            Button(
                onClick = { if (userRole.value == "admin") {
                    navController.navigate("menuadmin")
                } else {
                    navController.navigate("menu")
                }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Menú",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }

            Button(
                onClick = { navController.navigate("frmRentas") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Formulario Rentas",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                rentas.add(Rentas(4, 32, 9, "Traje bonito azul", "12/3/04", "5/6/78"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Producto Prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text("idRenta", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("idCliente", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("idTraje", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Descripción", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("FechaHoraInicio", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("FechaHoraFin", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
        }
        Divider()
        rentas.forEachIndexed { index, Rentas ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text("${Rentas.idRenta}", modifier = Modifier
                    .width(150.dp)
                )
                Text("${Rentas.idCliente}", modifier = Modifier
                    .width(150.dp)
                )
                Text("${Rentas.idTraje}", modifier = Modifier
                    .width(150.dp)
                )
                Text(Rentas.Descripcion, modifier = Modifier
                    .width(150.dp)
                )
                Text(Rentas.fechaHoraInicio, modifier = Modifier
                    .width(150.dp)
                )
                Text(Rentas.fechaHoraFin, modifier = Modifier
                    .width(150.dp)
                )
                Button(onClick = {
                    rentas.removeAt(index)
                    //Remueve en base al index del producto en la lista
                }) {
                    Text("Eliminar")
                }
            }
        }

    }
}
@Composable
fun frmRentasContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var idRenta by remember { mutableStateOf("") }
    var idCliente by remember { mutableStateOf("") }
    var idTraje by remember { mutableStateOf("") }
    var idEmpleado by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaHoraInicio by remember { mutableStateOf("") }
    var fechaHoraFin by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row {
            Button(
                onClick = { if (userRole.value == "admin") {
                    navController.navigate("menuadmin")
                } else {
                    navController.navigate("menu")
                }
                }, // Se navega al menú
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
            ) {
                Text(text = "Regresar al Menú")
            }
            Button(
                onClick = { navController.navigate("lstRentas") }, // Se navega a la tabla
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
            ) {
                Text(text = "Regresar a Rentas")
            }
        }

        Spacer(Modifier.height(100.dp))
        Text(
            text = "Formulario de Rentas",
            modifier = Modifier
                .padding(24.dp)
                .align(alignment = Alignment.CenterHorizontally),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )

        Text(text = "Id Renta:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idRenta,
            onValueChange = { idRenta = it },
            placeholder = { Text("Id Renta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Id Cliente:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idCliente,
            onValueChange = { idCliente = it },
            placeholder = { Text("Id Cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Id Traje:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idTraje,
            onValueChange = { idTraje = it },
            placeholder = { Text("Id Traje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        if (userRole.value == "admin") {
        Text(text = "Id Empleado:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idEmpleado,
            onValueChange = { idEmpleado = it },
            placeholder = { Text("Id Empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))
        } else {

        }

        Text(text = "descripcion:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            placeholder = { Text("Descripción del traje:") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Fecha de reserva:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = fechaHoraInicio,
            onValueChange = { fechaHoraInicio = it },
            placeholder = { Text("Fecha de la reserva:") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Fecha de entrega:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = fechaHoraFin,
            onValueChange = { fechaHoraFin = it },
            placeholder = { Text("Fecha de entrega:") },
            modifier = Modifier.fillMaxWidth()
        )


        Button(
            onClick = {
                Toast.makeText(context, "Id Renta: $idRenta", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Id Cliente: $idCliente", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Id Traje: $idTraje", Toast.LENGTH_SHORT).show()
                if (userRole.value == "admin") {
                Toast.makeText(context, "Id Empleado: $idEmpleado", Toast.LENGTH_SHORT).show()
                } else {
                }
                Toast.makeText(context, "Descripción: $descripcion", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Hora de inicio: $fechaHoraInicio", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Hora de fin: $fechaHoraFin", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            Text(text = "Enviar")
        }
    }
}

@Composable
fun LstPagosContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    data class Pagos(val idPago: Int, val idRenta: Int, val monto: Double, val Fecha: String, val metodoPago: String)
    val pagos = remember {
        mutableStateListOf(
            Pagos(1, 1, 2000.0, "09-10-2025" ,"Efectivo"),
            Pagos(2, 2, 1800.0, "10-10-2025" ,"Tarjeta"),
            Pagos(3, 3, 1500.0, "10-10-2025" ,"Efectivo")
        )
    }
    // productos[index] = Producto(nombre, precio, existencias)
    // productos[2] = Producto("Florentinas Fresa", 20.0, 5)
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Separa los botones
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para ir al Menú
            Button(
                onClick = { if (userRole.value == "admin") {
                    navController.navigate("menuadmin")
                } else {
                    navController.navigate("menu")
                }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Menú",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }

            Button(
                onClick = { navController.navigate("frmPagos") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Formulario Pagos",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                pagos.add(Pagos(4, 4, 1700.0, "10-10-2025" ,"Tarjeta"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Producto Prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Id Pago", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Id Renta", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Monto", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Fecha de Pago", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Metodo de Pago", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

            Text("Eliminar", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)

        }
        Divider()
        pagos.forEachIndexed { index, Pagos ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text(
                    "${Pagos.idPago}", modifier = Modifier
                        .width(150.dp)
                )
                Text(
                    "${Pagos.idRenta}", modifier = Modifier
                        .width(150.dp)
                )
                Text(
                    "$${Pagos.monto}", modifier = Modifier
                        .width(150.dp)
                )
                Text(
                    "${Pagos.Fecha}", modifier = Modifier
                        .width(150.dp)
                )
                Text(
                    "${Pagos.metodoPago}", modifier = Modifier
                        .width(150.dp)
                )

                Button(onClick = {
                    pagos.removeAt(index)
                    //Remueve en base al index del producto en la lista
                })
                {
                    Text("Eliminar")
                }

            }
        }

    }
}
@Composable
fun frmPagosContent(modifier: Modifier = Modifier, controller: NavHostController, userRole: androidx.compose.runtime.MutableState<String>
) {
    val context = LocalContext.current

    var idPago by remember { mutableStateOf("") }
    var idRenta by remember { mutableStateOf("") }
    var Monto by remember { mutableStateOf("") }
    var FechaPago by remember { mutableStateOf("") }
    var MetodoPago by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row {
            Button(
                onClick = { if (userRole.value == "admin") {
                    controller.navigate("menuadmin")
                } else {
                    controller.navigate("menu")
                }
                }, // Se navega al menú
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
            ) {
                Text(text = "Regresar al Menú")
            }
            Button(
                onClick = { controller.navigate("lstPagos") }, // Se navega a la tabla
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
            ) {
                Text(text = "Regresar a Pagos")
            }
        }

        Spacer(Modifier.height(100.dp))
        Text(
            text = "Formulario de Pagos",
            modifier = Modifier
                .padding(24.dp)
                .align(alignment = Alignment.CenterHorizontally),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )

        Text(text = "Id Pago:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idPago,
            onValueChange = { idPago = it },
            placeholder = { Text("Id Pago") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Id Renta:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idRenta,
            onValueChange = { idRenta = it },
            placeholder = { Text("Id Renta") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Monto:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = Monto,
            onValueChange = { Monto = it },
            placeholder = { Text("Monto final") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Fecha de Pago:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = FechaPago,
            onValueChange = { FechaPago = it },
            placeholder = { Text("Fecha del pago") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Metodo de pago:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = MetodoPago,
            onValueChange = { MetodoPago = it },
            placeholder = { Text("Metodo de pago:") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                Toast.makeText(context, "Id Pago: $idPago", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Id Renta: $idRenta", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Monto: $Monto", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Fecha de pago: $FechaPago", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Metodo de pago: $MetodoPago", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            Text(text = "Enviar")
        }
    }
}

@Composable
fun LstEmpleadosContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    data class Empleado(val id: Int, val nombre: String, val puesto: String, val telefono: String)

    val empleados = remember {
        mutableStateListOf(
            Empleado(1, "Juan Pérez", "Gerente", "555-1234"),
            Empleado(2, "Ana Gómez", "Cajera", "555-5678"),
            Empleado(3, "Luis Díaz", "Repartidor", "555-9101")
        )
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .horizontalScroll(scrollState)
            .padding(8.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Separa los botones
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón para ir al Menú
            Button(
                onClick = { if (userRole.value == "admin") {
                    navController.navigate("menuadmin")
                } else {
                    navController.navigate("menu")
                }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Menú",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }

            Button(
                onClick = { navController.navigate("frmEmpleados") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(
                    "Formulario Empleados",
                    fontFamily = FontFamily.Serif,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val nuevoId = (empleados.maxOfOrNull { it.id } ?: 0) + 1
                empleados.add(Empleado(nuevoId, "Nuevo Empleado", "Nuevo Puesto", "555-0000"))
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Agregar Empleado Prueba",
                style = TextStyle(textDecoration = TextDecoration.Underline),
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row {
            Text("ID", modifier = Modifier.width(60.dp), fontWeight = FontWeight.Bold)
            Text("Nombre", modifier = Modifier.width(150.dp), fontWeight = FontWeight.Bold)
            Text("Puesto", modifier = Modifier.width(120.dp), fontWeight = FontWeight.Bold)
            Text("Teléfono", modifier = Modifier.width(120.dp), fontWeight = FontWeight.Bold)
            Text("Eliminar", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
        }

        Divider()


        empleados.forEachIndexed { index, empleado ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row(
                modifier = Modifier
                    .background(bgColor)
                    .fillMaxWidth()
            ) {
                Text("${empleado.id}", modifier = Modifier.width(60.dp))
                Text(empleado.nombre, modifier = Modifier.width(150.dp))
                Text(empleado.puesto, modifier = Modifier.width(120.dp))
                Text(empleado.telefono, modifier = Modifier.width(120.dp))
                Button(onClick = { empleados.removeAt(index) }) {
                    Text("Eliminar")
                }
            }
        }
    }
}
@Composable
fun frmEmpleadosContent(modifier: Modifier = Modifier, controller: NavHostController, userRole: androidx.compose.runtime.MutableState<String>
) {
    val context = LocalContext.current

    var idEmpleado by remember { mutableStateOf("") }
    var nombreEmpleado by remember { mutableStateOf("") }
    var puesto by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {

        Row {
            Button(
                onClick = { if (userRole.value == "admin") {
                    controller.navigate("menuadmin")
                } else {
                    controller.navigate("menu")
                }
                }, // Se navega al menú
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(text = "Regresar al Menú")
            }
            Button(
                onClick = { controller.navigate("lstEmpleados") }, // Se navega a la tabla
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Blue
                )
            ) {
                Text(text = "Regresar a Empleados")
            }
        }

        Spacer(Modifier.height(100.dp))
        Text(
            text = "Formulario de Empleados",
            modifier = Modifier
                .padding(24.dp)
                .align(alignment = Alignment.CenterHorizontally),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp
        )

        Text(text = "Id Empleado:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = idEmpleado,
            onValueChange = { idEmpleado = it },
            placeholder = { Text("Id del empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Nombre del Empleado:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = nombreEmpleado,
            onValueChange = { nombreEmpleado = it },
            placeholder = { Text("Nombre del empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Puesto:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = puesto,
            onValueChange = { puesto = it },
            placeholder = { Text("Puesto del empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Teléfono:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = telefono,
            onValueChange = { telefono = it },
            placeholder = { Text("Teléfono del empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = {
                Toast.makeText(context, "Id Empleado: $idEmpleado", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Nombre: $nombreEmpleado", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Puesto: $puesto", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Teléfono: $telefono", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            Text(text = "Enviar")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    ListasTheme {
        AppContent()
    }
}