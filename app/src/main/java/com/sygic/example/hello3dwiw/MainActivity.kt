package com.sygic.example.hello3dwiw

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.compose.AndroidFragment
import androidx.lifecycle.lifecycleScope
import com.sygic.aura.ResourceManager
import com.sygic.aura.ResourceManager.OnResultListener
import com.sygic.aura.utils.PermissionsUtils
import com.sygic.sdk.api.ApiNavigation.navigateToAddress
import com.sygic.sdk.api.exception.GeneralException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var fgm: SygicNaviFragment? = null
    private var uiInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val permissions = PermissionsUtils.getAllPermissions(this)
            .filter { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_DENIED }
        if (permissions.isEmpty()) {
            checkSygicResources()
        } else {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 1111);
        }
    }

    private fun checkSygicResources() {
        val resourceManager = ResourceManager(this, null)
        if (resourceManager.shouldUpdateResources()) {
            Toast.makeText(this, "Please wait while Sygic resources are being updated", Toast.LENGTH_LONG).show()
            resourceManager.updateResources(object : OnResultListener {
                override fun onError(code: Int, message: String) {
                    Toast.makeText(this@MainActivity, "Failed to update resources: $message", Toast.LENGTH_LONG).show()
                    finish()
                }

                override fun onSuccess() {
                    initUI()
                }
            })
        } else {
            initUI()
        }
    }

    private fun initUI() {
        if (uiInitialized)
            return

        uiInitialized = true

        initWithCompose()
//        initWithView()

    }

    private fun initWithCompose() {
        setContent {
            val addressValue = remember { mutableStateOf("") }

            MaterialTheme {
                Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 8.dp, 16.dp, 8.dp),
                        label = { Text("Address") },
                        value = addressValue.value,
                        onValueChange = { addressValue.value = it }
                    )
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp, 0.dp, 16.dp, 8.dp),
                        onClick = {
                            lifecycleScope.launch(Dispatchers.IO) {
                                try {
                                    navigateToAddress(addressValue.value, false, 0, 5000)
                                } catch (e: Exception) {
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(this@MainActivity, e.message.toString(), Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    ) {
                        Text("Navigate to Address")
                    }
                    AndroidFragment<SygicNaviFragment>(modifier = Modifier.fillMaxSize()) { fgm = it }
                }
            }
        }
    }

    private fun initWithView() {
        setContentView(R.layout.activity_main)
        fgm = SygicNaviFragment()

        supportFragmentManager.beginTransaction().replace(R.id.sygicmap, fgm!!).commitAllowingStateLoss()
        findViewById<Button>(R.id.btnNavigate).setOnClickListener {
            object : Thread() {
                override fun run() {
                    try {
                        val address = findViewById<EditText>(R.id.editAddress).text.toString()
                        navigateToAddress(address, false, 0, 5000)
                    } catch (e: GeneralException) {
                        e.printStackTrace()
                    }
                }
            }.start()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById<LinearLayout>(R.id.root)) { v, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
                        or WindowInsetsCompat.Type.displayCutout()
            )
            v.updatePadding(
                left = bars.left,
                top = bars.top,
                right = bars.right,
                bottom = bars.bottom,
            )
            WindowInsetsCompat.CONSUMED
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (res in grantResults) {
            if (res != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You have to allow all permissions", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }

        // all permissions are granted
        checkSygicResources()
    }

    override fun onCreateDialog(id: Int): Dialog {
        return fgm?.onCreateDialog(id) ?: return super.onCreateDialog(id)
    }

    override fun onPrepareDialog(id: Int, dialog: Dialog) {
        super.onPrepareDialog(id, dialog)
        fgm?.onPrepareDialog(id, dialog)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        fgm!!.onNewIntent(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fgm?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return fgm?.onKeyDown(keyCode, event) ?: return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        return fgm?.onKeyUp(keyCode, event) ?: return super.onKeyUp(keyCode, event)
    }
}
