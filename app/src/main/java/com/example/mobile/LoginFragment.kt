package com.example.mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        // Inisialisasi elemen-elemen UI
        val uemail: EditText = view.findViewById(R.id.edt_email)
        val usandi: EditText = view.findViewById(R.id.edt_pass)
        val btnMasuk: Button = view.findViewById(R.id.button_login)
        val txtDaftar: TextView = view.findViewById(R.id.txt_daftar)

        // Set listener untuk tombol masuk
        btnMasuk.setOnClickListener {
            val gmail = uemail.text.toString().trim()
            val password = usandi.text.toString().trim()

            // Validasi input sebelum masuk
            if (gmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // login process
            auth.signInWithEmailAndPassword(gmail, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val userId = user?.uid

                        val database = FirebaseDatabase.getInstance()
                        val reference = database.getReference("User")

                        userId?.let {
                            reference.child(it).get().addOnSuccessListener { snapshot ->
                                val intent = Intent(requireContext(), MainActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish() // close current activity
                            }.addOnFailureListener { e ->
                                // login failed
                                Toast.makeText(requireContext(),
                                    "Login gagal: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // login failed
                        val exception = task.exception
                        if (exception is FirebaseAuthInvalidUserException
                            || exception is FirebaseAuthInvalidCredentialsException) {
                            // wrong email or password
                            Toast.makeText(requireContext(),
                                "Email atau password salah", Toast.LENGTH_SHORT).show()
                        } else {
                            // other failure
                            Toast.makeText(requireContext(),
                                "Login gagal: ${exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }


        // Listener untuk tombol daftar (redirect ke halaman daftar)
        txtDaftar.setOnClickListener {
            val daftar = RegisFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.begin, daftar)
            transaction.commit()
        }
    }
}