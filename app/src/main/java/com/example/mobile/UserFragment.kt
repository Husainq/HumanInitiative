package com.example.mobile

import android.app.Dialog
import android.app.admin.TargetUser
import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserFragment : Fragment() {
    lateinit var binding: FragmentUserBinding
    private lateinit var penggunaList: MutableList<Pengguna>
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("User")
        penggunaList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) { // Pastikan Fragment terpasang sebelum menggunakan requireActivity()
                    if (snapshot.exists()) {
                        penggunaList.clear()
                        for (a in snapshot.children) {
                            val penggunaakun = a.getValue(Pengguna::class.java)
                            penggunaakun ?.let {
                                penggunaList.add(it)
                            }
                        }
                        val adapter = UpUserAdapter(
                            requireActivity(),
                            R.layout.detil_user,
                            penggunaList
                        )
                        binding.infouser.adapter = adapter
                        println("Output: " + penggunaList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }

}

