@file:Suppress("unused", "unused")

package ve.com.teeac.svgs.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ve.com.teeac.svgs.customers.CustomersScreen

@Composable
fun AppGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            CustomersScreen(modifier = modifier)
        }
//        composable("camera") {
//            CameraScreen(onPostUploaded = {
//                navController.navigate("home")
//            })
//        }
//        composable("post/{postId}/comments") { backStackEntry ->
//            val postId = backStackEntry.arguments?.getString("postId")!!
//            CommentScreen(postId = postId, onBack = { navController.navigateUp() })
//        }
//        composable("account") {
//            Text(text = "TODO")
//        }
//        composable(
//            route = "account/{userId}",
//            deepLinks = listOf(navDeepLink {
//                uriPattern = "instant://account/{userId}"
//            })
//        ) {
//            Text(text = "account")
//        }
    }
}

sealed class DestinationScreen(val route: String) {
    object MainScreenDest : DestinationScreen(route = "main_screen")
}
