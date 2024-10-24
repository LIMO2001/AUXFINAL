package com.smartherd.aniaux.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.*
import com.smartherd.aniaux.R

@Composable
fun Home() {
    var searchQuery by remember { mutableStateOf("") } // For search bar, currently unused
    var itemsList by remember { mutableStateOf<List<GridItemsSingle>>(emptyList()) } // List to store fetched items

    // Reference to Firebase Database
    val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("animals")

    // Fetching data from Firebase
    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = mutableListOf<GridItemsSingle>()
                for (snapshot in dataSnapshot.children) {
                    // Fetch each breed's details from the snapshot
                    val desc = snapshot.child("desc").getValue(String::class.java) ?: ""
                    val price = snapshot.child("price").getValue(Int::class.java) ?: 0

                    // Map breed name to corresponding drawable resource
                    val resource = when (desc) {
                        "Ayshre" -> R.drawable.ic_ayshre
                        "Jersey" -> R.drawable.ic_jersey
                        "Fresian" -> R.drawable.ic_fres
                        "Zebu" -> R.drawable.zebu
                        "Angus" -> R.drawable.angus
                        "Simental" -> R.drawable.simental
                        else -> R.drawable.ic_ayshre // Default image for unknown breeds
                    }

                    // Add the fetched breed to the items list
                    items.add(GridItemsSingle(resource, desc, price))
                }
                itemsList = items // Update the state with fetched items
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors like permission issues or network failures
                println("Error fetching data: ${databaseError.message}")
            }
        })
    }

    // Column Layout for UI Components
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header Text for Popular Breeds
        Text(text = "POPULAR BREEDS", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

        // Grid to display fetched items from Firebase
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // Number of columns
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp) // Padding for the grid
        ) {
            items(itemsList) { imageRes ->
                GridItem(gridItem = imageRes, price = "${imageRes.price} KSH", onBidClick = {
                    // Action when bid button is clicked, e.g., navigate to bidding screen
                })
            }
        }
    }
}

// Data class to hold grid item information
data class GridItemsSingle(val resource: Int, val desc: String, val price: Int)

@Composable
fun GridItem(gridItem: GridItemsSingle, price: String, onBidClick: () -> Unit) {
    // Implement UI for each grid item, including the image, description, price, and bid button
}
