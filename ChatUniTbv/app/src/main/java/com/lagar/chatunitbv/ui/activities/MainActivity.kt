package com.lagar.chatunitbv.ui.activities

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.lagar.chatunitbv.R
import com.lagar.chatunitbv.databinding.ActivityMainBinding
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.widget.Button
import android.widget.ImageView
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.lagar.chatunitbv.databinding.SlidingMenuHeaderBinding
import com.lagar.chatunitbv.firebase.Operations
import com.lagar.chatunitbv.models.Group
import com.lagar.chatunitbv.models.User
import com.lagar.chatunitbv.preferences.UserSharedPreferencesRepository
import io.github.rosariopfernandes.firecoil.load
import java.net.URI


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var title: TextView
    private lateinit var user: User
    private lateinit var imageUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = UserSharedPreferencesRepository(
            this.getSharedPreferences("USER", MODE_PRIVATE)
        )
        user = sharedPreferences.read()

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        title = findViewById(R.id.toolbar_title)
        val bottomNavView: BottomNavigationView = binding.bottomNavView

        drawerLayout = findViewById(R.id.drawerLayout)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment

        val navController: NavController = navHostFragment.navController
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener(View.OnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        })
        navigationView.bringToFront()
        toggle.syncState()
        drawerLayout.addDrawerListener(toggle)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_chats, R.id.navigation_people
            )
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            title.text = when (destination.id) {
                R.id.navigation_chats -> "Chats"
                R.id.navigation_people -> "People"
                else -> " "
            }
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)

        binding.toolbarButtonLogOut.setOnClickListener {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            val preferences = this
                .getSharedPreferences("REMEMBER_USER", Context.MODE_PRIVATE)
                ?.edit()
            preferences?.putBoolean("user", false)
            preferences?.apply()

            val userSharedPreferences = this
                .getSharedPreferences("USER", Context.MODE_PRIVATE)
                ?.edit()
                ?.clear()
            userSharedPreferences?.apply()

            switchActivities()
        }

        binding.navView.getHeaderView(0).findViewById<Button>(R.id.upload_photo)
            .setOnClickListener {
                uploadImage()

            }


        Operations.db.collection("groups").document("${user.group}").get()
            .addOnSuccessListener {
                val group = it.toObject<Group>()
                setupUserData(group)
            }
    }

    private fun uploadImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {

            imageUri = data?.data!!

            val profilePicture: ImageView = findViewById(R.id.profile_image)
            val storageReference =
                FirebaseStorage.getInstance().getReference("images/users/${user.email}.jpg")
            storageReference.putFile(imageUri).addOnSuccessListener {
                profilePicture.load(imageUri) {
                    transformations(CircleCropTransformation())
                }
            }.addOnFailureListener {

            }
            profilePicture.load(imageUri) {
                transformations(CircleCropTransformation())
            }

        }
    }

    private fun setupUserData(group: Group?) {
//      Toast.makeText(this, "User logged:${user.email} ${user.name}", Toast.LENGTH_LONG).show()
        val profilePicture: ImageView = findViewById(R.id.profile_image)
        val profileName: TextView = findViewById(R.id.profile_name)
        val profileField: TextView = findViewById(R.id.profile_field)
        val profileGroup: TextView = findViewById(R.id.profile_group)

        val reference = Operations.store.getReference("images/users/${user.email}.jpg");
        val avatar = Operations.store.getReference("images/avatar.jpg")

        reference.downloadUrl.addOnSuccessListener {
            profilePicture.load(reference) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
            .addOnFailureListener {
                profilePicture.load(avatar) {
                    transformations(CircleCropTransformation())
                }
            }



        profileName.text = user.name.toString()
        profileField.text = group?.specialisation.toString()
        profileGroup.text = user.group.toString()
    }


    private var doubleBackToExitPressedOnce = false

    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            finish()
            finishAffinity()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private fun switchActivities() {
        this.finishAffinity()
        val switchActivityIntent = Intent(this, StartActivity::class.java)
        startActivity(switchActivityIntent)
    }


}