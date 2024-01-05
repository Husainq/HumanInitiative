package com.example.mobile

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class BencanaAdapter(
    val bencanaContext: Context,
    val layoutResId: Int,
    val bencanaList: List<Bencana>
) : ArrayAdapter<Bencana>(bencanaContext, layoutResId, bencanaList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(bencanaContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val o_judul: TextView = view.findViewById(R.id.ou_judul)
        val o_bencana: TextView = view.findViewById(R.id.ou_bencana)
        val o_kategori: TextView = view.findViewById(R.id.ou_kategori)
        val o_target: TextView = view.findViewById(R.id.ou_target)
        val o_tingkat: TextView = view.findViewById(R.id.ou_tingkat)
        val o_nama: TextView = view.findViewById(R.id.ou_nama)
        val o_norek: TextView = view.findViewById(R.id.ou_norek)
        val imgEdit: ImageView = view.findViewById(R.id.icn_edit)
        val anggota = bencanaList[position]

        imgEdit.setOnClickListener {
            updateDialog(anggota)
        }
        o_judul.text = "Judul : " + anggota.judul
        o_bencana.text = "Bencana Alam : " + anggota.bencana
        o_kategori.text = "Jenis Kategori Bencana : " + anggota.kategori
        o_target.text = "Target Donasi : IDR" + anggota.target
        o_tingkat.text = "Tingkat Kerusakan :" + anggota.tingkat+"%"
        o_nama.text = "Nama Rekening Penerima : " + anggota.nama
        o_norek.text = "No Rekening Penerima : " + anggota.norek

        return view
    }

    private fun updateDialog(anggota: Bencana) {
        val builder = AlertDialog.Builder(bencanaContext)
        builder.setTitle("Update Data")
        val inflater = LayoutInflater.from(bencanaContext)
        val view = inflater.inflate(R.layout.update, null)

        val edtJudul = view.findViewById<EditText>(R.id.upJudul)
        val edtBencana = view.findViewById<EditText>(R.id.upBencana)
        val edtKategori = view.findViewById<EditText>(R.id.upKategori)
        val edtTarget = view.findViewById<EditText>(R.id.upTarget)
        val edtTingkat = view.findViewById<EditText>(R.id.upTingkat)
        val edtNama = view.findViewById<EditText>(R.id.upNama)
        val edtNorek = view.findViewById<EditText>(R.id.upNoRek)

        edtJudul.setText(anggota.judul)
        edtBencana.setText(anggota.bencana)
        edtKategori.setText(anggota.kategori)
        edtTarget.setText(anggota.target)
        edtTingkat.setText(anggota.tingkat)
        edtNama.setText(anggota.nama)
        edtNorek.setText(anggota.norek)

        builder.setView(view)

        builder.setPositiveButton("Ubah") { pe, p1 ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("bencana")
            val judul = edtJudul.text.toString().trim()
            val bencana = edtBencana.text.toString().trim()
            val kategori = edtKategori.text.toString().trim()
            val target = edtTarget.text.toString().trim()
            val tingkat = edtTingkat.text.toString().trim()
            val nama = edtNama.text.toString().trim()
            val norek = edtNorek.text.toString().trim()

            if (judul.isEmpty() or bencana.isEmpty() or kategori.isEmpty() or target.isEmpty() or
                tingkat.isEmpty() or nama.isEmpty() or norek.isEmpty()) {
                Toast.makeText(
                    bencanaContext, "Isi data secara lengkap tidak boleh kosong",
                    Toast.LENGTH_SHORT
                )
                    .show()

                return@setPositiveButton
            }
            val anggota = Bencana(anggota.id, judul, bencana, kategori, target, tingkat, nama, norek)

            dbAnggota.child(anggota.id).setValue(anggota)
            Toast.makeText(bencanaContext, "Data berhasil di update", Toast.LENGTH_SHORT)
                .show()
        }

        builder.setNeutralButton("Batal") { po, p1 -> }

        builder.setNegativeButton("Hapus") { po, p1 ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("bencana")
                .child(anggota.id)

            dbAnggota.removeValue()

            Toast.makeText(bencanaContext, "Data berhasil di hapus", Toast.LENGTH_SHORT)
                .show()
        }
        val alert = builder.create()
        alert.show()
    }
}