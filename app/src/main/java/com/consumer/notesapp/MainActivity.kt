package com.consumer.notesapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.consumer.notesapp.databinding.ActivityMainBinding
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph)
            .setOpenableLayout(binding.drawerLayout)
            .build()
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.navView, navController)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            if (id == R.id.action_see_secret_notes) {
                authenticateForSecret {
                    val b = Bundle().apply { putBoolean("showSecret", true) }
                    navController.navigate(R.id.FirstFragment, b)
                    binding.drawerLayout.closeDrawers()
                }
                true
            } else if (id == R.id.action_see_notes) {
                val b = Bundle().apply { putBoolean("showSecret", false) }
                navController.navigate(R.id.FirstFragment, b)
                binding.drawerLayout.closeDrawers()
                true
            } else if (id == R.id.action_new_note) {
                val b = Bundle().apply { putLong("noteId", -1); putBoolean("isSecret", false) }
                navController.navigate(R.id.SecondFragment, b)
                binding.drawerLayout.closeDrawers()
                true
            } else NavigationUI.onNavDestinationSelected(menuItem, navController)
        }

        binding.fab.setOnClickListener {
            val b = Bundle().apply { putLong("noteId", -1); putBoolean("isSecret", false) }
            navController.navigate(R.id.SecondFragment, b)
        }
    }

    private fun authenticateForSecret(onSuccess: () -> Unit) {
        val executor: Executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                runOnUiThread { onSuccess() }
            }
        }
        val prompt = BiometricPrompt(this, executor, callback)
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Authenticate to view secret notes")
        try {
            builder.setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        } catch (e: NoSuchMethodError) {
            builder.setNegativeButtonText("Cancel")
        }
        prompt.authenticate(builder.build())
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}
