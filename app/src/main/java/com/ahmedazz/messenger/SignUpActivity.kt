package com.ahmedazz.messenger

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.ahmedazz.messenger.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), TextWatcher {


    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val currentUserDocRef: DocumentReference
    get() = firestoreInstance.document("users/${mAuth.currentUser?.uid.toString()}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        editText_name_sign_up.addTextChangedListener(this@SignUpActivity)
        editText_email_sign_up.addTextChangedListener(
            this@SignUpActivity
        )
        editText_password_sign_up.addTextChangedListener(this@SignUpActivity)


        btn_sign_up.setOnClickListener {

            val name = editText_name_sign_up.text.toString().trim()
            val email = editText_email_sign_up.text.toString().trim()
            val password = editText_password_sign_up.text.toString().trim()

            if (name.isEmpty()){
                editText_name_sign_up.error = "Name Required"
                editText_name_sign_up.requestFocus()
                return@setOnClickListener
            }

            if (email.isEmpty()){
                editText_email_sign_up.error = "Email Required"
                editText_email_sign_up.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editText_email_sign_up.error = "Please enter a valid email"
                editText_email_sign_up.requestFocus()
                return@setOnClickListener
            }

            if (password.length < 6){
                editText_password_sign_up.error = "6 char required"
                editText_password_sign_up.requestFocus()
                return@setOnClickListener
            }

            createNewAccount(name, email, password)
        }


    }

    private fun createNewAccount(name: String, email: String, password: String) {
        progressBar_sign_up.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->

            val newUser = User(name, "")

            currentUserDocRef.set(newUser)

            if (task.isSuccessful){
                progressBar_sign_up.visibility = View.INVISIBLE
                val intentMainActivity = Intent(this@SignUpActivity, MainActivity::class.java)
                intentMainActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intentMainActivity)
            } else {
                progressBar_sign_up.visibility = View.INVISIBLE
                Toast.makeText(this@SignUpActivity, task.exception?.message, Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun afterTextChanged(s: Editable?) {
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        btn_sign_up.isEnabled = editText_name_sign_up.text.trim().isNotEmpty()
                && editText_email_sign_up.text.trim().isNotEmpty()
                && editText_password_sign_up.text.trim().isNotEmpty()
    }

}
