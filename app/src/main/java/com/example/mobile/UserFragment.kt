package com.example.mobile


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mobile.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserFragment : Fragment() {
    private lateinit var binding: FragmentUserBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userRef: DatabaseReference
    private var isFragmentAttached = false // Variabel untuk menandai fragment terpasang

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener{
            val profilFragment = ProfilFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,profilFragment)
            transaction.commit()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isFragmentAttached = true // Set variabel bahwa fragment terpasang
        firebaseAuth = FirebaseAuth.getInstance()
        userRef = FirebaseDatabase.getInstance().getReference("User")

        displayUserInfo()
    }


    private fun displayUserInfo() {
        val user = firebaseAuth.currentUser
        val userId = user?.uid

        userId?.let {
            userRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFragmentAttached) { // Pastikan fragment masih terpasang sebelum menggunakan konteks
                        if (snapshot.exists()) {
                            val username = snapshot.child("username").getValue(String::class.java)
                            val nohp = snapshot.child("nohandphone").getValue(String::class.java)
                            val password = snapshot.child("password").getValue(String::class.java)
                            val email = snapshot.child("email").getValue(String::class.java)

                            binding.oUName.setText(username)
                            binding.oUNoHp.setText(nohp)
                            binding.oUPass.setText(password)
                            binding.oUEmail.setText(email)

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}

