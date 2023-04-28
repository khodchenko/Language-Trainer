package com.example.language_trainer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    // Объявляем переменные класса
    private var mGoogleSignInAccount: GoogleSignInAccount? = null
    private var bottomNavigationView: BottomNavigationView? = null
    private var selectedFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Находим BottomNavigationView по id
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)

        // Добавляем слушатель для BottomNavigationView
        bottomNavigationView?.setOnNavigationItemSelectedListener { item ->
            // Обрабатываем выбор фрагмента
            when (item.itemId) {
                FRAGMENT_TRANSLATE -> selectedFragment = TranslateFragment()
                FRAGMENT_DICTIONARY -> selectedFragment = DictionaryFragment()
                FRAGMENT_LEARNING -> selectedFragment = LearningFragment()
            }

            // Заменяем текущий фрагмент на выбранный
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment!!).commit()
            true
        }

        // По умолчанию выбираем TranslateFragment
        selectedFragment = TranslateFragment()
        supportFragmentManager.beginTransaction().replace(
            R.id.fragment_container,
            selectedFragment as TranslateFragment
        ).commit()
    }

    // Обработчик результата входа в систему
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    // Обрабатываем результат входа в систему
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            mGoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            val displayName = mGoogleSignInAccount?.displayName
            // TODO: выполнить действия для зарегистрированного пользователя
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.e(TAG, "onConnectionFailed: $connectionResult")
    }

    companion object {
        private val FRAGMENT_TRANSLATE = R.id.menu_translate
        private val FRAGMENT_DICTIONARY = R.id.menu_dictionary
        private val FRAGMENT_LEARNING = R.id.menu_learning
        private const val TAG = "MainActivity"
        private const val RC_SIGN_IN = 9001
    }
}