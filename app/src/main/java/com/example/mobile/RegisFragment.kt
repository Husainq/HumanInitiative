package com.example.mobile

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisFragment : Fragment(R.layout.fragment_regis) {

    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        // Inisialisasi elemen-elemen UI
        val uemail: EditText = view.findViewById(R.id.edt_email)
        val uname: EditText = view.findViewById(R.id.edt_username)
        val unohp: EditText = view.findViewById(R.id.edt_mobilenumber)
        val usandi: EditText = view.findViewById(R.id.edt_pass)
        val btnDaftar: Button = view.findViewById(R.id.button_daftar)
        val txtLogin: TextView = view.findViewById(R.id.txt_masuk)

        // Set listener untuk tombol daftar
        btnDaftar.setOnClickListener {
            val gmail = uemail.text.toString().trim()
            val username = uname.text.toString().trim()
            val nohandphone = unohp.text.toString().trim()
            val password = usandi.text.toString().trim()

            // Validasi input sebelum pendaftaran
            if (gmail.isEmpty() || username.isEmpty() || nohandphone.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Harap isi semua kolom", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proses pendaftaran ke Firebase Authentication
            auth.createUserWithEmailAndPassword(gmail, password)
                .addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        // Pendaftaran berhasil
                        val user = auth.currentUser
                        val userId = user?.uid // atau dapatkan ID pengguna dari hasil pendaftaran

                        // Menyimpan informasi pengguna ke Realtime Database
                        val database = FirebaseDatabase.getInstance()
                        val reference = database.getReference("User") // Ganti dengan lokasi yang benar di database Anda

                        val userData = HashMap<String, Any>()
                        userData["email"] = gmail
                        userData["username"] = username
                        userData["nohandphone"] = nohandphone
                        userData["password"] = password

                        userId?.let {
                            reference.child(it).setValue(userData)
                                .addOnSuccessListener {
                                    // Data pengguna berhasil disimpan ke database
                                    Toast.makeText(requireContext(), "Pendaftaran berhasil!", Toast.LENGTH_SHORT).show()
                                    // Redirect ke halaman utama setelah pendaftaran berhasil
                                    val loginFragment = LoginFragment()
                                    val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()

                                    if (!requireActivity().isFinishing && !requireActivity().isDestroyed) {
                                        transaction.replace(R.id.begin, loginFragment)
                                        transaction.commit()
                                    }

                                }
                                .addOnFailureListener { e ->
                                    // Gagal menyimpan data pengguna ke database
                                    Toast.makeText(requireContext(), "Gagal menyimpan data pengguna: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else {
                        // Pendaftaran gagal, tampilkan pesan kesalahan
                        val exception = task.exception
                        Toast.makeText(requireContext(), "Pendaftaran gagal: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        // Listener untuk tombol daftar (redirect ke halaman login)
        txtLogin.setOnClickListener {
            val login = LoginFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.begin, login)
            transaction.commit()
        }
    }
}