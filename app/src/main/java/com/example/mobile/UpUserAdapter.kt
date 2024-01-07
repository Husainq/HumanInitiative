package com.example.mobile

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class UpUserAdapter(
    val penggunaContext: Context,
    val layoutResId: Int,
    val penggunaList: List<Pengguna>
) : ArrayAdapter<Pengguna>(penggunaContext, layoutResId, penggunaList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(penggunaContext)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val oi_email: TextView = view.findViewById(R.id.o_email)
        val oi_username: TextView = view.findViewById(R.id.o_username)
        val oi_nohpone: TextView = view.findViewById(R.id.o_nohphone)
        val oi_password: TextView = view.findViewById(R.id.o_password)
        val btnUbah: Button = view.findViewById(R.id.btnUbahUp)
        val btnCancel: Button = view.findViewById(R.id.btnCancelUp)
        val anggota = penggunaList[position]

        btnUbah.setOnClickListener {
            updateDialog(anggota)
        }
        oi_email.text = anggota.uemail
        oi_username.text = anggota.unama
        oi_nohpone.text = anggota.unohp
        oi_password.text = anggota.upass

        return view
    }

    private fun updateDialog(anggota: Pengguna) {
        val builder = AlertDialog.Builder(penggunaContext)
        builder.setTitle("Update Data")
        val inflater = LayoutInflater.from(penggunaContext)
        val view = inflater.inflate(R.layout.up_user, null)

        val edtUEmail = view.findViewById<EditText>(R.id.upUEmail)
        val edtUName = view.findViewById<EditText>(R.id.upUName)
        val edtUNoHp = view.findViewById<EditText>(R.id.upUNoHp)
        val edtUPass = view.findViewById<EditText>(R.id.upUPass)

        edtUEmail.setText(anggota.uemail)
        edtUName.setText(anggota.unama)
        edtUNoHp.setText(anggota.unohp)
        edtUPass.setText(anggota.upass)

        builder.setView(view)

        builder.setPositiveButton("Ubah") { pe, p1 ->
            val dbAnggota = FirebaseDatabase.getInstance().getReference("User")
            val email = edtUEmail.text.toString().trim()
            val nama = edtUName.text.toString().trim()
            val nohandphone = edtUNoHp.text.toString().trim()
            val password = edtUPass.text.toString().trim()

            if (email.isEmpty() or nama.isEmpty() or nohandphone.isEmpty() or password.isEmpty()) {
                Toast.makeText(
                    penggunaContext, "Isi data secara lengkap tidak boleh kosong",
                    Toast.LENGTH_SHORT
                )
                    .show()

                return@setPositiveButton
            }
            val anggota = Pengguna(anggota.id, email, nama, nohandphone, password )

            dbAnggota.child(anggota.id).setValue(anggota)
            Toast.makeText(penggunaContext, "Data berhasil di update", Toast.LENGTH_SHORT)
                .show()
        }

        builder.setNeutralButton("Batal") { po, p1 -> }

        val alert = builder.create()
        alert.show()
    }
}