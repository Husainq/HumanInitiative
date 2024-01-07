package com.example.mobile

data class Pengguna (
    val id: String,
    val email: String,
    val name: String,
    val nohp: String,
    val pass: String
) {
    constructor() : this("","","","","")
}