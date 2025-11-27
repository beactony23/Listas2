package com.example.listas

import android.os.Bundle
import android.util.Log
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
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope

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
    val context = LocalContext.current
    val sesionIniciada : String? = getSessionValue(context, "sesionIniciada", "no")

    var startDestination = "lstRentas"

    if(sesionIniciada == "yesAdmin"){
        startDestination = "menuadmin"
        userRole.value = "admin"
    } else if (sesionIniciada == "yes"){
        startDestination = "menu"
    }


    NavHost(navController = navController, startDestination = startDestination) {
        composable("Login") { LoginContent(navController, modifier, userRole) }
        composable("menu") { MenuContent(navController, modifier) }
        composable("menuadmin") { MenuAdminContent(navController, modifier) }
        composable("lstTrajes") { LstTrajesContent(navController, modifier, userRole) }
        composable("frmTrajes") { FrmTrajesContent(navController, modifier, userRole) }
        composable("lstClientes") { LstClientesContent(navController, modifier, userRole) }
        composable("frmClientes") { FrmClientesContent(modifier, navController, userRole) }
        composable("lstRentas") { LstRentasContent(navController, modifier, userRole) }
        composable("lstPagos") { LstPagosContent(navController, modifier, userRole) }
        composable("lstEmpleados") { LstEmpleadosContent(navController, modifier, userRole) }
        composable("frmEmpleados") { FrmEmpleadosContent(modifier, navController, userRole) }


    }
}




fun setSessionValue(context: Context, key: String, value: String) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    val editor = sharedPrefs.edit()
    editor.putString(key, value)
    editor.apply()
    // setSessionValue(context, "sesionIniciada", "yes")
}

fun getSessionValue(context: Context, key: String, defaultValue: String): String? {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
    return sharedPrefs.getString(key, defaultValue)
    // val sesionIniciada : String = getSessionValue(context, "sesionIniciada", "no")
}

data class ModeloPagos(
    val idPagos: Int,
    val idRenta: Int,
    val descripcionRentas: String,
    val monto: Double,
    val fechaPago: String,
    val metodoPago: String,
    
)

data class ModeloClientes(
    val idCliente: Int,
    val nombre: String,
    val telefono: String,
    val correo:String)


data class OpcionRenta(val value: String, val label: String)
interface ApiService {
    @POST("berrones-acosta.php?iniciarSesion")
    @FormUrlEncoded
    suspend fun iniciarSesion(
        @Field("usuario") usuario: String,
        @Field("contrasena") contrasena: String
    ): Response<String>

    @GET("berrones-acosta.php?Pagos")
    suspend fun Pagos(): List<ModeloPagos>

    @GET("berrones-acosta.php?rentaCombo")
    suspend fun rentaCombo(): List<OpcionRenta>

    @POST("berrones-acosta.php?agregarPagos")
    @FormUrlEncoded
    suspend fun agregarPagos(
        @Field("idRenta") idRenta: Int,
        @Field("monto") monto: Double,
        @Field("fechaPago") fechaPago: String,
        @Field("metodoPago") metodoPago: String
    ): Response<String>

    @POST("berrones-acosta.php?modificarPagos")
    @FormUrlEncoded
    suspend fun modificarPagos(
        @Field("idPagos") idPagos: Int,
        @Field("idRenta") idRenta: Int,
        @Field("monto") monto: Double,
        @Field("fechaPago") fechaPago: String,
        @Field("metodoPago") metodoPago: String
    ): Response<String>
    @POST("berrones-acosta.php?eliminarPagos")
    @FormUrlEncoded
    suspend fun eliminarPagos(
        @Field("idPagos") idPagos: Int
    ): Response<String>
//José Magdaleno
    @GET("martinez-lira.php?listarClientes")
    suspend fun listarClientes(): List<ModeloClientes>
    @FormUrlEncoded
    @POST("martinez-lira.php?agregarCliente")
    suspend fun agregarCliente(
        @Field("id") nombre: String,
        @Field("telefonoCliente") telefono: String,
        @Field("correoElectronico") correo: String
    ): Response<String>
    @FormUrlEncoded
    @POST("martinez-lira.php?buscarCliente=true")
    suspend fun buscarCliente(
        @Field("idCliente") idCliente: Int
    ): ModeloClientes?
    @FormUrlEncoded
    @POST("martinez-lira.php?modificarCliente")
    suspend fun modificarCliente(
        @Field("idCliente") idCliente: Int,
        @Field("nombreCliente") nombre: String,
        @Field("telefonoCliente") telefono: String,
        @Field("correoElectronico") correo: String
    ): Response<String>
    @FormUrlEncoded
    @POST("martinez-lira.php?eliminarCliente=true")
    suspend fun eliminarCliente(
        @Field("idCliente") idCliente: Int
    ): Response<String>
// Yamil Yeray
// RENTAS
@GET("hernandez-herrera.php?Rentas")
suspend fun getRentas(): List<ModeloRenta>

    @FormUrlEncoded
    @POST("hernandez-herrera?agregarRenta")
    suspend fun agregarRenta(
        @Field("idCliente") idCliente: Int,
        @Field("idTraje") idTraje: Int,
        @Field("idEmpleado") idEmpleado: Int,
        @Field("descripcionRentas") descripcion: String,
        @Field("fechaHoraInicio") inicio: String,
        @Field("fechaHoraFin") fin: String
    ): Response<String>

    @FormUrlEncoded
    @POST("hernandez-herrera?modificarRenta")
    suspend fun modificarRenta(
        @Field("idRenta") idRenta: Int,
        @Field("idCliente") idCliente: Int,
        @Field("idTraje") idTraje: Int,
        @Field("idEmpleado") idEmpleado: Int,
        @Field("descripcionRentas") descripcion: String,
        @Field("fechaHoraInicio") inicio: String,
        @Field("fechaHoraFin") fin: String
    ): Response<String>

    @FormUrlEncoded
    @POST("hernandez-herrera?eliminarRenta")
    suspend fun eliminarRenta(
        @Field("idRenta") idRenta: Int
    ): Response<String>
}

val retrofit = Retrofit.Builder()
    .baseUrl("https://dfhash.com/temporal/practicasDAM/")
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val api = retrofit.create(ApiService::class.java)

@Composable
fun LoginContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    val context = LocalContext.current

    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
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
                scope.launch {
                    try {
                        val respuesta : Response<String> = api.iniciarSesion(usuario, contrasena)
                        if (respuesta.body() == "correcto") {
                        Toast.makeText(context, "Inicio de sesión con éxito.", Toast.LENGTH_SHORT).show()
                            if (usuario == "Admin") {
                                userRole.value = "admin"
                                navController.navigate("menuadmin")
                                setSessionValue(context, "sesionIniciada", "yesAdmin")
                            } else {
                                userRole.value = "cliente"
                                navController.navigate("menu")
                                setSessionValue(context, "sesionIniciada", "yes")
                            }

                        } else {
                        Toast.makeText(context, "Inicio de sesión fallido.", Toast.LENGTH_SHORT).show()
                        }

                    }
                    catch (e: Exception) {
                        Log.e("API", "Error al agregar registro: ${e.message}")
                    }
                }
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
                navController.navigate("visitante")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            ),
            modifier = Modifier
                .size(width = 150.dp, height = 40.dp)
                .align(Alignment.CenterHorizontally)
        ) {


            Text("Visitante")
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

            Text("RentasYami")
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
fun LstTrajesContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    data class Trajes(val idTraje: Int, val nombre: String, val descripcion: String)
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
        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        trajes.forEachIndexed { index, Trajes ->
            val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White

            Row (
                modifier = Modifier
                    .background(bgColor)
            ) {
                Text("${Trajes.idTraje}", modifier = Modifier
                    .width(150.dp)
                )
                Text(
                    Trajes.nombre, modifier = Modifier
                    .width(100.dp)
                )
                Text(
                    Trajes.descripcion, modifier = Modifier
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
fun FrmTrajesContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>
) {
    val context = LocalContext.current

    var idTraje by remember { mutableStateOf("") }
    var nombreTraje by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }


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
            if (userRole.value == "admin") {
            Button(
                onClick = { navController.navigate("lstTrajes") }, // Se navega a la tabla
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color.Blue)
            ) {
                Text(text = "Regresar a Trajes")
            }
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
            value = nombreTraje,
            onValueChange = { nombreTraje = it },
            placeholder = { Text("Nombre del Traje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))

        Text(text = "Descripcion:", modifier = Modifier.align(alignment = Alignment.Start))
        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            placeholder = { Text("Descripcion") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(40.dp))



        Button(
            onClick = {
                Toast.makeText(context, "Id Traje: $idTraje", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Nombre Del Traje: $nombreTraje", Toast.LENGTH_SHORT).show()
                Toast.makeText(context, "Descripcion: $descripcion", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            Text(text = "Enviar")
        }
    }
}


@Composable
fun LstClientesContent(navController: NavHostController, modifier: Modifier, userRole: MutableState<String>) {
    var clientes by remember { mutableStateOf<List<ModeloClientes>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        cargarListaClientes(scope, context) { clientes = it }
    }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Lista de Clientes", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { navController.navigate("frmClientes") }) {
            Text("Agregar/Modificar Cliente")
        }
        Spacer(Modifier.height(16.dp))

        Column(Modifier.verticalScroll(rememberScrollState())) {
            clientes.forEach { cliente ->
                Text("${cliente.idCliente} - ${cliente.nombre} (${cliente.correo})", Modifier.padding(vertical = 4.dp))
            }
        }
    }
}

@Composable
fun FrmClientesContent(navController: NavHostController, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Campos de estado para los datos del cliente
    var idClienteStr by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }

    // Función para rellenar los campos al buscar
    val fillFields: (ModeloClientes) -> Unit = { cliente ->
        nombre = cliente.nombre
        telefono = cliente.telefono
        correo = cliente.correo
    }

    Column(modifier = modifier.verticalScroll(rememberScrollState()).padding(16.dp)) {
        Text("Formulario de Clientes", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))

        // Campo para ID (usado en Buscar, Modificar, Eliminar)
        TextField(value = idClienteStr, onValueChange = { idClienteStr = it }, label = { Text("ID Cliente (Buscar/Modificar/Eliminar)") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))

        // Campos para Nombre, Teléfono y Correo
        TextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre Completo") }, modifier = Modifier.fillMaxWidth())
        TextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        TextField(value = correo, onValueChange = { correo = it }, label = { Text("Correo Electrónico") }, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { scope.launch { agregarNuevoCliente(context, nombre, telefono, correo) } }) {
                Text("Agregar")
            }

            Button(onClick = {
                val id = idClienteStr.toIntOrNull()
                if (id != null) scope.launch { buscarClientePorID(context, id, fillFields) }
                else Toast.makeText(context, "ID inválido para buscar", Toast.LENGTH_SHORT).show()
            }) {
                Text("Buscar")
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = {
                val id = idClienteStr.toIntOrNull()
                if (id != null) scope.launch { modificarClienteExistente(context, id, nombre, telefono, correo) }
                else Toast.makeText(context, "ID es obligatorio para modificar", Toast.LENGTH_SHORT).show()
            }) {
                Text("Modificar")
            }

            Button(onClick = {
                val id = idClienteStr.toIntOrNull()
                if (id != null) scope.launch { eliminarClientePorID(context, id) }
                else Toast.makeText(context, "ID es obligatorio para eliminar", Toast.LENGTH_SHORT).show()
            }) {
                Text("Eliminar")
            }
            }
        }
}
@Composable
fun FrmClientesContent(modifier: Modifier = Modifier, controller: NavHostController, userRole: androidx.compose.runtime.MutableState<String>
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LstRentasContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Lista de rentas
    val rentas = remember { mutableStateListOf<ModeloRenta>() }

    // Variables del formulario
    var idRenta by remember { mutableStateOf(0) }
    var idCliente by remember { mutableStateOf("") }
    var idTraje by remember { mutableStateOf("") }
    var idEmpleado by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }

    // Cargar lista al inicio
    LaunchedEffect(Unit) {
        try {
            val respuesta = api.getRentas()
            rentas.clear()
            rentas.addAll(respuesta)
        } catch (e: Exception) {
            Log.e("API", "Error al cargar Rentas: ${e.message}")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        // Botón de menú
        Button(onClick = {
            if (userRole.value == "admin") navController.navigate("menuadmin")
            else navController.navigate("menu")
        }) {
            Text("Menú")
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = if (idRenta == 0) "Agregar Renta" else "Modificar Renta",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // FORMULARIO -------------------------

        TextField(
            value = idCliente,
            onValueChange = { idCliente = it },
            label = { Text("ID Cliente") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = idTraje,
            onValueChange = { idTraje = it },
            label = { Text("ID Traje") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = idEmpleado,
            onValueChange = { idEmpleado = it },
            label = { Text("ID Empleado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = fechaInicio,
            onValueChange = { fechaInicio = it },
            label = { Text("Fecha Inicio") },
            placeholder = { Text("DD/MM/AAAA") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        TextField(
            value = fechaFin,
            onValueChange = { fechaFin = it },
            label = { Text("Fecha Fin") },
            placeholder = { Text("DD/MM/AAAA") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        // BOTÓN AÑADIR / MODIFICAR
        Button(
            onClick = {
                scope.launch {
                    try {
                        if (idRenta == 0) {
                            // INSERTAR
                            val respuesta = api.agregarRenta(
                                idCliente.toInt(),
                                idTraje.toInt(),
                                idEmpleado.toInt(),
                                descripcion,
                                fechaInicio,
                                fechaFin
                            )

                            val newId = respuesta.body()?.toIntOrNull() ?: 0
                            if (newId > 0) {
                                Toast.makeText(context, "Renta agregada.", Toast.LENGTH_SHORT)
                                    .show()
                                rentas.add(
                                    ModeloRenta(
                                        idRenta = newId,
                                        idCliente = idCliente.toInt(),
                                        idTraje = idTraje.toInt(),
                                        idEmpleado = idEmpleado.toInt(),
                                        descripcion = descripcion,
                                        fechaInicio = fechaInicio,
                                        fechaFin = fechaFin
                                    )
                                )

                            }

                        } else {
                            // MODIFICAR
                            val respuesta = api.modificarRenta(
                                idRenta,
                                idCliente.toInt(),
                                idTraje.toInt(),
                                idEmpleado.toInt(),
                                descripcion,
                                fechaInicio,
                                fechaFin
                            )

                            if (respuesta.body() == "correcto") {
                                Toast.makeText(context, "Renta modificada.", Toast.LENGTH_SHORT)
                                    .show()

                                val index = rentas.indexOfFirst { it.idRenta == idRenta }
                                if (index >= 0) {
                                    rentas[index] = ModeloRenta(
                                        idRenta = idRenta,
                                        idCliente = idCliente.toInt(),
                                        idTraje = idTraje.toInt(),
                                        idEmpleado = idEmpleado.toInt(),
                                        descripcion = descripcion,
                                        fechaInicio = fechaInicio,
                                        fechaFin = fechaFin
                                    )
                                }
                            }
                        }

                        // Limpiar formulario
                        idRenta = 0
                        idCliente = ""
                        idTraje = ""
                        idEmpleado = ""
                        descripcion = ""
                        fechaInicio = ""
                        fechaFin = ""

                    } catch (e: Exception) {
                        Log.e("API", "Error al guardar renta: ${e.message}")
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(if (idRenta == 0) "Añadir" else "Modificar")
        }

        Spacer(Modifier.height(32.dp))

        // TABLA --------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                Text("ID", Modifier.width(60.dp), fontWeight = FontWeight.Bold)
                Text("Cliente", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                Text("Traje", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                Text("Empleado", Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                Text("Descripción", Modifier.width(150.dp), fontWeight = FontWeight.Bold)
                Text("Inicio", Modifier.width(120.dp), fontWeight = FontWeight.Bold)
                Text("Fin", Modifier.width(120.dp), fontWeight = FontWeight.Bold)
                Text("Acciones", Modifier.width(180.dp), fontWeight = FontWeight.Bold)
            }

            HorizontalDivider()

            // FILAS
            rentas.forEachIndexed { index, renta ->
                val color = if (index % 2 == 0) Color(0xFFF4F4F4) else Color.White

                Row(
                    modifier = Modifier
                        .background(color)
                        .horizontalScroll(rememberScrollState())
                        .padding(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${renta.idRenta}", Modifier.width(60.dp))
                    Text(renta.idCliente.toString())
                    Text(renta.idTraje.toString())
                    Text(renta.idEmpleado.toString())

                    Text(renta.descripcion)
                    Text(renta.fechaInicio)
                    Text(renta.fechaFin)


                    // BOTONES
                    Row(Modifier.width(180.dp)) {

                        // EDITAR
                        Button(onClick = {
                            idRenta = renta.idRenta
                            idCliente = renta.idCliente.toString()
                            idTraje = renta.idTraje.toString()
                            idEmpleado = renta.idEmpleado.toString()
                            descripcion = renta.descripcion
                            fechaInicio = renta.fechaInicio
                            fechaFin = renta.fechaFin

                        }) { Text("Editar") }

                        Spacer(Modifier.width(8.dp))

                        // ELIMINAR
                        Button(onClick = {
                            scope.launch {
                                try {
                                    val r = api.eliminarRenta(renta.idRenta)
                                    if (r.body() == "correcto") {
                                        rentas.removeAt(index)
                                        Toast.makeText(context, "Renta eliminada.", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("API", "ERROR eliminar: ${e.message}")
                                }
                            }
                        }) { Text("Eliminar") }

                    }
                }
            }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LstPagosContent(navController: NavHostController, modifier: Modifier, userRole: androidx.compose.runtime.MutableState<String>) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val pagos = remember { mutableStateListOf<ModeloPagos>() }

    // Variables del formulario
    var idPagos by remember { mutableStateOf(0) }
    var idRenta by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var fechaPago by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("") }

    val rentaOpciones = remember {
        mutableStateListOf<OpcionRenta>(
            OpcionRenta("", "Selecciona una opción"),
            //OpcionCategoria("1", "Galletas"),
            //OpcionCategoria("2", "Refrescos")
        )
    }
// ...
    var rentaExpandido by remember { mutableStateOf(value = false) }
    var renta by remember { mutableStateOf(value = rentaOpciones.first()) }

    // Cargar lista al iniciar
    LaunchedEffect(Unit) {
        try {
            val respuesta = api.Pagos()
            pagos.clear()
            pagos.addAll(respuesta)
            val respuesta2 = api.rentaCombo()
            rentaOpciones.clear()
            rentaOpciones.addAll(respuesta2)
        } catch (e: Exception) {
            Log.e("API", "Error al cargar Pagos: ${e.message}")
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Botones de navegación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                if (userRole.value == "admin") navController.navigate("menuadmin")
                else navController.navigate("menu")
            }) {
                Text("Menú")
            }

        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = if (idPagos == 0) "Agregar Pago" else "Modificar Pago",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(16.dp))

        // Campos del formulario
        Text("Id Pagos:")
        TextField(
            value = if (idPagos == 0) "" else idPagos.toString(),
            onValueChange = {
                idPagos = it.toIntOrNull() ?: 0  // Convierte a Int, si falla usa 0
            },
            placeholder = { Text("Id Pagos") },
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Text(text = "Descripción renta:")

        ExposedDropdownMenuBox(
            expanded = rentaExpandido,
            onExpandedChange = { rentaExpandido = !rentaExpandido }
        ) {
            TextField(
                value = renta.label,
                onValueChange = { /* No usado aquí ya que es de solo lectura */ },
                readOnly = true,
                label = { Text(text = "Selecciona una opción") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = rentaExpandido)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = rentaExpandido,
                onDismissRequest = { rentaExpandido = false }
            ) {
                rentaOpciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = { Text(opcion.label) },
                        onClick = {
                            renta = opcion
                            idRenta = opcion.value
                            rentaExpandido = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Monto:")
        TextField(
            value = monto,
            onValueChange = { monto = it },
            placeholder = { Text("Monto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Text("Fecha de Pago:")
        TextField(
            value = fechaPago,
            onValueChange = { fechaPago = it },
            placeholder = { Text("Día-Mes-Año") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        Text("Método de pago:")
        TextField(
            value = metodoPago,
            onValueChange = { metodoPago = it },
            placeholder = { Text("Método de pago") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(16.dp))

        // Botón agregar/modificar
        Button(
            onClick = {
                scope.launch {
                    try {
                        if (idPagos == 0) {
                            // Insertar
                            val respuesta = api.agregarPagos(
                                idRenta.toInt(),
                                monto.toDouble(),
                                fechaPago,
                                metodoPago
                            )
                            val nuevoId = respuesta.body()?.toIntOrNull() ?: 0
                            if (nuevoId > 0) {
                                Toast.makeText(context, "Pago agregado.", Toast.LENGTH_SHORT).show()
                                pagos.add(
                                    ModeloPagos(
                                        idPagos = nuevoId,
                                        idRenta = idRenta.toInt(),
                                        descripcionRentas = renta.label,
                                        monto = monto.toDouble(),
                                        fechaPago = fechaPago,
                                        metodoPago = metodoPago
                                    )
                                )
                            } else {
                                Toast.makeText(context, "Error al agregar.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        } else {

                            // Editar/modificar
                            val respuesta = api.modificarPagos(
                                idPagos.toInt(),
                                idRenta.toInt(),
                                monto.toDouble(),
                                fechaPago.toString(),
                                metodoPago.toString()
                            )
                            if (respuesta.body() == "correcto") {
                                Toast.makeText(context, "Pago modificado.", Toast.LENGTH_SHORT)
                                    .show()

                                // Refrescar lista o actualizar localmente:
                                val index = pagos.indexOfFirst { it.idPagos == idPagos }
                                if (index >= 0) {
                                    pagos[index] = ModeloPagos(
                                        idPagos = idPagos,
                                        idRenta = idRenta.toInt(),
                                        descripcionRentas = renta.label,
                                        monto = monto.toDouble(),
                                        fechaPago = fechaPago,
                                        metodoPago = metodoPago
                                    )
                                }
                            } else {
                                Toast.makeText(context, " Error al modificar.", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                        // Limpiar formulario después
                        idPagos = 0
                        idRenta = ""
                        monto = ""
                        fechaPago = ""
                        metodoPago = ""
                    } catch (e: Exception) {
                        Log.e("API", "Error al guardar pago: ${e.message}")
                    }
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = if (idPagos == 0) "Añadir" else "Modificar")
        }

        Spacer(Modifier.height(32.dp))

        // Lista de pagos
        // Scroll vertical para toda la tabla
        val verticalScroll = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(verticalScroll) // solo vertical aquí
        ) {
            // Fila de encabezados con scroll horizontal
            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {
                Text("Id Pago", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                Text("Descripción Renta", modifier = Modifier.width(80.dp), fontWeight = FontWeight.Bold)
                Text("Monto", modifier = Modifier.width(100.dp), fontWeight = FontWeight.Bold)
                Text("Fecha", modifier = Modifier.width(120.dp), fontWeight = FontWeight.Bold)
                Text("Método", modifier = Modifier.width(120.dp), fontWeight = FontWeight.Bold)
                Text("Acciones", modifier = Modifier.width(160.dp), fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

            // Filas de pagos
            pagos.forEachIndexed { index, pago ->
                val bgColor = if (index % 2 == 0) Color(0xFFF5F5F5) else Color.White
                Row(
                    modifier = Modifier
                        .background(bgColor)
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()) // solo horizontal para la fila
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${pago.idPagos}", modifier = Modifier.width(80.dp))
                    Text("${pago.descripcionRentas}", modifier = Modifier.width(80.dp))
                    Text("$${pago.monto}", modifier = Modifier.width(100.dp))
                    Text(pago.fechaPago, modifier = Modifier.width(120.dp))
                    Text(pago.metodoPago, modifier = Modifier.width(120.dp))

                    Row(modifier = Modifier.width(160.dp)) {
                        Button(onClick = {
                            // Editar: cargar los datos al formulario
                            idPagos = pago.idPagos
                            idRenta = pago.idRenta.toString()
                            monto = pago.monto.toString()
                            fechaPago = pago.fechaPago
                            metodoPago = pago.metodoPago
                        }) {
                            Text("Editar")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            scope.launch {
                                try {
                                    val respuesta = api.eliminarPagos(pago.idPagos)
                                    if (respuesta.body() == "correcto") {
                                        Toast.makeText(
                                            context,
                                            "Pago eliminado.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        pagos.removeAt(index)
                                    } else {
                                        Toast.makeText(
                                            context,
                                            "Error al eliminar.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    Log.e("API", "Error al eliminar pago: ${e.message}")
                                }
                            }
                        }) {
                            Text("Eliminar")
                        }
                    }
                }
            }
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

        HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)


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
fun FrmEmpleadosContent(modifier: Modifier = Modifier, controller: NavHostController, userRole: androidx.compose.runtime.MutableState<String>) {
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
@OptIn(ExperimentalMaterial3Api::class)

private suspend fun cargarListaClientes(scope: CoroutineScope, context: Context, onLoaded: (List<ModeloClientes>) -> Unit) {
    try {
        val response = api.listarClientes()
        onLoaded(response)
    } catch (e: Exception) {
        Toast.makeText(context, "Error al cargar la lista de clientes: ${e.message}", Toast.LENGTH_SHORT).show()
        Log.e("API_CLIENTES", "Error al listar: ${e.message}")
    }
}

private suspend fun agregarNuevoCliente(context: Context, nombre: String, telefono: String, correo: String) {
    try {
        val respuesta = api.agregarCliente(nombre, telefono, correo)
        if (respuesta.body() == "correcto") {
            Toast.makeText(context, "Cliente agregado con éxito", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error al agregar: ${respuesta.body()}", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Log.e("API_CLIENTES", "Error al agregar: ${e.message}")
        Toast.makeText(context, "Error de conexión/servidor: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

private suspend fun buscarClientePorID(context: Context, idCliente: Int, onFound: (ModeloClientes) -> Unit) {
    try {
        val cliente = api.buscarCliente(idCliente)
        if (cliente != null) {
            onFound(cliente)
            Toast.makeText(context, "Cliente encontrado", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Cliente no encontrado (ID: $idCliente)", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Log.e("API_CLIENTES", "Error al buscar: ${e.message}")
        Toast.makeText(context, "Error de conexión/servidor: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

private suspend fun modificarClienteExistente(context: Context, idCliente: Int, nombre: String, telefono: String, correo: String) {
    try {
        val respuesta = api.modificarCliente(idCliente, nombre, telefono, correo)
        if (respuesta.body() == "correcto") {
            Toast.makeText(context, "Cliente modificado con éxito", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error al modificar: ${respuesta.body()}", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Log.e("API_CLIENTES", "Error al modificar: ${e.message}")
        Toast.makeText(context, "Error de conexión/servidor: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

private suspend fun eliminarClientePorID(context: Context, idCliente: Int) {
    try {
        val respuesta = api.eliminarCliente(idCliente)
        if (respuesta.body() == "correcto") {
            Toast.makeText(context, "Cliente $idCliente eliminado con éxito", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Error al eliminar: ${respuesta.body()}", Toast.LENGTH_LONG).show()
        }
    } catch (e: Exception) {
        Log.e("API_CLIENTES", "Error al eliminar: ${e.message}")
        Toast.makeText(context, "Error de conexión/servidor: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

private fun Unit.body(){

}
@Preview(showBackground = true)
@Composable
fun AppContentPreview() {
    ListasTheme {
        AppContent()
    }
}