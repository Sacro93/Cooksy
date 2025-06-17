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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.cooksy.data.SectionItem
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
                        painter = painterResource(id = R.drawable.elementos), // Imagen decorativa
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .alpha(0.2f) // Hacer la imagen bien transparente
                    )
                    // Avatar
                    Box(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.3f)),
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

                Spacer(modifier = Modifier.height(25.dp))

                // Card Principal "Explorar Recetas"
                CardSectionBig(
                    label = "Explorar Recetas",
                    imageRes = R.drawable.recipes,
                    route = Routes.RECIPE_LIST,
                    navController = navController
                )

                Spacer(modifier = Modifier.height(40.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    items(sections) { section ->
                        CardSectionSmall(section = section, navController = navController)
                    }
                }
            }
        }
    }
}



val sections = listOf(
    SectionItem("Virales", R.drawable.virales, Routes.VIRAL_RECIPES),
    SectionItem("Al Súper", R.drawable.supermarket, Routes.SUPERMARKET_LIST)
)

