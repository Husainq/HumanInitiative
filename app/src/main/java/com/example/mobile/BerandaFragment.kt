package com.example.mobile

import android.app.Dialog
import android.app.admin.TargetUser
import android.os.Bundle
import android.os.UserHandle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mobile.databinding.FragmentBerandaBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference

class BerandaFragment : Fragment() {
    lateinit var binding: FragmentBerandaBinding
    private lateinit var bencanaList: MutableList<Bencana>
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBerandaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ref = FirebaseDatabase.getInstance().getReference("bencana")
        bencanaList = mutableListOf()

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (isAdded) { // Pastikan Fragment terpasang sebelum menggunakan requireActivity()
                    if (snapshot.exists()) {
                        bencanaList.clear()
                        for (a in snapshot.children) {
                            val bencanalam = a.getValue(Bencana::class.java)
                            bencanalam ?.let {
                                bencanaList.add(it)
                            }
                        }
                        val adapter = BencanaAdapter(
                            requireActivity(),
                            R.layout.detil_bencana,
                            bencanaList
                        )
                        binding.hasil.adapter = adapter
                        println("Output: " + bencanaList)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
    }

}

