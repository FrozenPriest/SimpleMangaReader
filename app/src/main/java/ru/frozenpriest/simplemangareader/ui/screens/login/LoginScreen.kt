package ru.frozenpriest.simplemangareader.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ru.frozenpriest.simplemangareader.R
import ru.frozenpriest.simplemangareader.ui.Screen
import ru.frozenpriest.simplemangareader.ui.components.rememberState

/*
todo make it look nice
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val authSuccessful = viewModel.authSuccessful.collectAsState()
    if (authSuccessful.value) {
        navController.navigate(Screen.Library.route) {
            //popUpTo("login") { inclusive = true }
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.secondary),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(start = 16.dp, end = 16.dp),
                backgroundColor = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                elevation = 12.dp
            ) {
                var login by rememberState(viewModel.login)
                var password by rememberState("")
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = login,
                        onValueChange = { login = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        label = { Text(text = stringResource(id = R.string.login)) }
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        label = { Text(text = stringResource(id = R.string.password)) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.authIn(login, password) }) {
                        Text(text = stringResource(id = R.string.enter))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

            }
        }
    }
}