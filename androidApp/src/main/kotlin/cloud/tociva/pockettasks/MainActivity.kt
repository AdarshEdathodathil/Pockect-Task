package cloud.tociva.pockettasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import cloud.tociva.pockettasks.data.RoomTaskRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val repository = remember { RoomTaskRepository(applicationContext) }
            App(repository = repository)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
