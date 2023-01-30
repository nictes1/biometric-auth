package com.example.biometricauth

import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricPrompt
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.biometricauth.ui.theme.BiometricAuthTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BiometricAuthTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Auth()
                }
            }
        }
        setupAuth()

    }

    private var canAuthenticate = false
    private lateinit var promptInfo: androidx.biometric.BiometricPrompt.PromptInfo

    private fun setupAuth(){
        if (androidx.biometric.BiometricManager.from(this).canAuthenticate(
                androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL) == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS){
            canAuthenticate = true

            promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticacion Biometrica")
                .setSubtitle("Autenticate usando el sensor biometrico de tu dispositivo")
                .setAllowedAuthenticators(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG or androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .build()
        }
    }

    private fun authenticate(auth: (auth: Boolean) ->Unit){
        if (canAuthenticate){
            androidx.biometric.BiometricPrompt(this, ContextCompat.getMainExecutor(this),
                object: androidx.biometric.BiometricPrompt.AuthenticationCallback(){
                    override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)

                        auth(true)
                    }

                }).authenticate(promptInfo)
        }else{
            auth(true)
        }
    }

    @Composable
    fun Auth() {
        var auth by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .background(if (auth) Color.Green else Color.Red)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(if (auth) "Estas autenticado" else "Necesitas autenticarte", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                if(auth){
                    auth = false
                }else{
                authenticate {
                    auth = it }
            }}) {
                Text(if (auth) "Cerrar" else "Autenticarme")
            }
        }
    }
}

@Composable
fun Auth() {
    var auth by remember { mutableStateOf(false) }

    Column(
       modifier = Modifier
           .background(if (auth) Color.Green else Color.Red)
           .fillMaxSize(),
       horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center
    ) {
        Text(if (auth) "Estas autenticado" else "Necesitas autenticarte", fontSize = 22.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /*TODO*/ }) {
            Text(if (auth) "Cerrar" else "Autenticarme")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BiometricAuthTheme {
        Auth()
    }
}