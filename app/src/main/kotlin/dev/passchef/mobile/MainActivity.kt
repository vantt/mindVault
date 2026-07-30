package dev.passchef.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.remember

/**
 * Minimal Compose screen proving the :app -> :shared dependency wires through
 * at both build and runtime. Real unlock/vault UI is out of scope for this cell.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val result = remember { KeystoreCrypto().ping() }
            Text(text = result)
        }
    }
}
