package com.smartherd.aniaux.navigation

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.smartherd.aniaux.AuthViewModel.AuthViewModel
import com.smartherd.aniaux.R
import com.smartherd.aniaux.screens.Categories
import com.smartherd.aniaux.screens.Favourite
import com.smartherd.aniaux.screens.Home
import com.smartherd.aniaux.screens.Login
import com.smartherd.aniaux.screens.Profile
import com.smartherd.aniaux.screens.Screens
import com.smartherd.aniaux.screens.SignUpScreen
import com.smartherd.aniaux.ui.theme.grey


@Composable
fun MybottomAppBar(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel){
    val navigationController = rememberNavController()
    val context = LocalContext.current.applicationContext
    var isClicked by remember { mutableStateOf(false) }
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar(containerColor = grey) {
                IconButton(onClick = {
                    selected.value = Icons.Default.Home
                    navigationController.navigate(Screens.Screens.Home.screen){
                        popUpTo(0)
                    }
                },
                    modifier =Modifier.weight(1.5f)) {
                    Icon(Icons.Default.Home,
                        contentDescription = "Home",
                        Modifier.size(25.dp),
                        tint = if (selected.value == Icons.Default.Home) Color.Black   else MaterialTheme.colorScheme.primary )
                }
                //Categories
                IconButton(onClick = {
                    selected.value = Icons.Default.ShoppingCart
                    navigationController.navigate(Screens.Screens.Categories.screen){
                        popUpTo(0)
                    }
                },
                    modifier =Modifier.weight(1.5f)) {
                    Icon(
                        painter = painterResource(id = R.drawable.sort_icon),
                        contentDescription = "Sort Icon",
                        modifier = Modifier.size(24.dp).clickable { isClicked = !isClicked },

                        tint = if(isClicked) Color.Black else  MaterialTheme.colorScheme.primary
                    )

                }

                //floating btn
              /*  Box(modifier = Modifier
                    .weight(2.5f)
                    .padding(16.dp),
                    contentAlignment = Alignment.Center
                ){
                    FloatingActionButton(onClick = { Toast.makeText(context, "Floating action Clicker", Toast.LENGTH_SHORT).show() }) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = grey)
                    }

                }
*/
                IconButton(onClick = {
                    selected.value = Icons.Default.Favorite
                    navigationController.navigate(Screens.Screens.Favourite.screen){
                        popUpTo(0)
                    }
                },
                    modifier =Modifier.weight(2f)) {
                    Icon(Icons.Default.Favorite,
                        contentDescription = "Favourite",
                        Modifier.size(25.dp),
                        tint = if (selected.value == Icons.Default.Favorite) Color.Black   else MaterialTheme.colorScheme.primary )
                }

                //profile
                IconButton(onClick = {
                    selected.value = Icons.Default.AccountCircle
                    navigationController.navigate(Screens.Screens.Profile.screen){
                        popUpTo(0)
                    }
                },
                    modifier =Modifier.weight(2f)) {
                    Icon(Icons.Default.AccountCircle,
                        contentDescription = "Profile",
                        Modifier.size(25.dp),
                        tint = if (selected.value == Icons.Default.AccountCircle) Color.Black   else MaterialTheme.colorScheme.primary )
                }
            }
        }
    ) {
            paddingValues ->
        NavHost(navController = navigationController,
            startDestination = Screens.Screens.Home.screen,
            modifier = Modifier.padding(paddingValues)
        ){


            composable(Screens.Screens.Home.screen){ Home()}
            composable(Screens.Screens.Favourite.screen) { Favourite()  }
            composable(Screens.Screens.Categories.screen) { Categories()  }
            composable(Screens.Screens.Profile.screen) { Profile()  }
            //composable(Screens.Screens.Login.screen) { Login(modifier,navigationController,authViewModel)  }
           // composable(Screens.Screens.SignUpScreen.screen) { SignUpScreen(modifier,navigationController,authViewModel)  }


        }
    }
}

