package com.ahmedazz.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), TextWatcher{

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)


        editText_email_sign_in.addTextChangedListener(this@SignInActivity)
        editText_password_sign_in.addTextChangedListener(this@SignInActivity)


        btn_sign_in.setOnClickListener {

            val email = editText_email_sign_in.text.toString()
            val password = editText_password_sign_in.text.toString()

            if (email.isEmpty()){
                editText_email_sign_in.error = "Email Required"
                editText_email_sign_in.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editText_email_sign_in.error = "Please enter a valid email"
                editText_email_sign_in.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6){
                editText_password_sign_in.error = "6 char required"
                editText_password_sign_in.requestFocus()
                return@setOnClickListener
            }

            signIn(email, password)

        }

        btn_create_account.setOnClickListener {
            val signUpIntent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(signUpIntent)
        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser?.uid != null){
            val intentMainActivity = Intent(this@SignInActivity, MainActivity::class.java)
            startActivity(intentMainActivity)
        }
    }

    private fun signIn(email: String, password: String) {
        progress_sign_in.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {task ->

            if (task.isSuccessful){
                progress_sign_in.visibility = View.INVISIBLE
                val intentMainActivity = Intent(this@SignInActivity, MainActivity::class.java)
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentMainActivity)
            } else {
                progress_sign_in.visibility = View.INVISIBLE
                Toast.makeText(this@SignInActivity, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        btn_sign_in.isEnabled = editText_email_sign_in.text.toString().trim().isNotEmpty()
                && editText_password_sign_in.text.toString().trim().isNotEmpty()
    }


}
