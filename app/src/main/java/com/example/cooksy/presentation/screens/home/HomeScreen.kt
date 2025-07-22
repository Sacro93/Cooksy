package com.example.cooksy.presentation.screens.home

import com.example.cooksy.R
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cooksy.data.model.recipes.SectionItem
import com.example.cooksy.presentation.components.BottomNavigationBar
import com.example.cooksy.presentation.components.CardSectionBig
import com.example.cooksy.presentation.components.CardSectionSmall
import com.example.cooksy.presentation.navigation.Routes


@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9C27B0), // Violeta
                            Color(0xFF1565C0)  // Azul
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.elementos),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.2f)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f))
                            .clickable {
                                // Ir al perfil si se desea
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.chefcita),
                            contentDescription = "User Avatar",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Cuadrícula 2x2 con 4 accesos
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                            userScrollEnabled = false
                ) {
                    items(sectionsHome) { section ->
                        CardSectionSmall(section = section, navController = navController)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
val sectionsHome = listOf(
    SectionItem("Recetas", R.drawable.recipes, Routes.CATEGORY_SELECTOR),
    SectionItem("Virales", R.drawable.virales, Routes.VIRAL_RECIPES),
    SectionItem("Al Súper", R.drawable.supermarket, Routes.SUPERMARKET_LIST),
    SectionItem("Lugares", R.drawable.mapita1, Routes.PLACE_LIST)
)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeScreenPreview() {
    // Usamos un NavController simulado para preview
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}
