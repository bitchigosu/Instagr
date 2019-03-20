package com.example.instagr.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.example.instagr.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener

class LoginActivity : AppCompatActivity(), KeyboardVisibilityEventListener, View.OnClickListener {

    private val TAG = "LoginActivity"
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        Log.d(TAG, "onCreate: ")

        coordinateBtnAndInputs(login_button, email_login_input, password_login_input)
        login_button.setOnClickListener(this)
        create_account_text.setOnClickListener(this)
        mAuth = FirebaseAuth.getInstance()
        KeyboardVisibilityEvent.setEventListener(this, this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.login_button -> {
                val email = email_login_input.text.toString()
                val password = password_login_input.text.toString()
                if (validate(email, password)) {
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                    }
                } else {
                    showToast(getString(R.string.please_enter_email_and_passwrod))
                }
            }
            R.id.create_account_text -> {
                startActivity(Intent(this, RegisterActivity::class.java))
            }
        }


    }

    override fun onVisibilityChanged(isKeyboardOpen: Boolean) {
        if (isKeyboardOpen) {
            scroll_view.scrollTo(0, scroll_view.bottom)
            create_account_text.visibility = View.GONE
        } else {
            scroll_view.scrollTo(0, scroll_view.top)
            create_account_text.visibility = View.VISIBLE
        }
    }

    private fun validate(email: String, password: String) =
        email.isNotEmpty() && password.isNotEmpty()

}
