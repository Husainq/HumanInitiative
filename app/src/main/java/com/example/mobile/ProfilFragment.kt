package com.example.mobile

import android.app.Dialog
import android.app.admin.TargetUser
import android.content.Intent
import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentTransaction
import com.example.mobile.databinding.FragmentProfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfilFragment : Fragment() {
    private lateinit var binding: FragmentProfilBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userRef: DatabaseReference
    private var isFragmentAttached = false // Variabel untuk menandai fragment terpasang

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        binding.editprofil.setOnClickListener{
            val userFragment = UserFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.container,userFragment)
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
        binding.keluar.setOnClickListener {
            logout()
        }
    }
    private fun logout() {
        firebaseAuth.signOut()
        val i = Intent(requireContext(), BeginActivity::class.java)
        startActivity(i)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isFragmentAttached = false // Set variabel bahwa fragment sudah tidak terpasang lagi
    }

    private fun displayUserInfo() {
        val user = firebaseAuth.currentUser
        val profileImageUri = user?.photoUrl
        val userId = user?.uid

        userId?.let {
            userRef.child(it).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (isFragmentAttached) { // Pastikan fragment masih terpasang sebelum menggunakan konteks
                        if (snapshot.exists()) {
                            val username = snapshot.child("username").getValue(String::class.java)
                            val nohp = snapshot.child("nohandphone").getValue(String::class.java)

                            binding.namauser.setText(username)
                            binding.nouser.setText(nohp)

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }




    }

