package com.example.mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.example.mobile.databinding.FragmentTambahBinding

class TambahFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentTambahBinding
    private lateinit var ref: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTambahBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseDatabase.getInstance().getReference("bencana")
        binding.btnSimpan.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        simpanData()
        val bncn = BerandaFragment()
        val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.container, TambahFragment())
        transaction.commit()
    }

    private fun simpanData() {
        val judul = binding.edtJudul.text.toString().trim()
        val bencana = binding.edtBencana.text.toString().trim()
        val kategori =  binding.edtKategori.text.toString().trim()
        val target = binding.edtTarget.text.toString().trim()
        val tingkat = binding.edtTingkat.text.toString().trim()
        val nama = binding.edtNama.text.toString().trim()
        val norek = binding.edtNorek.text.toString().trim()

        if (judul.isEmpty() or bencana.isEmpty() or kategori.isEmpty() or target.isEmpty() or
            tingkat.isEmpty() or nama.isEmpty() or norek.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "Isi data secara lengkap tidak boleh kosong",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        val bncnId = ref.push().key
        val bncna = Bencana(bncnId!!, judul, bencana, kategori, target, tingkat, nama, norek)

        bncnId?.let {
            ref.child(it).setValue(bncna).addOnCompleteListener { task ->
                if(isAdded) {
                    if (task.isSuccessful) {
                        Toast.makeText(
                            requireContext(),
                            "Data berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Gagal menambahkan data",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}