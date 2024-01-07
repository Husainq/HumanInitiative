package com.example.mobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mobile.databinding.FragmentBeginBinding

class BeginFragment : Fragment() {
    lateinit var binding: FragmentBeginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBeginBinding.inflate(layoutInflater)

        binding.btnRegisAwal.setOnClickListener{
            val regisFragment = RegisFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.begin,regisFragment)
            transaction.commit()
        }

        binding.btnLoginAwal.setOnClickListener{
            val loginFragment = LoginFragment()
            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.begin,loginFragment)
            transaction.commit()
        }
        return binding.root
    }

}