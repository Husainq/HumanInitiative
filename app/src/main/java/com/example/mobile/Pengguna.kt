package com.example.mobile

data class Pengguna (
    val id: String,
    val uemail: String,
    val unama: String,
    val unohp: String,
    val upass: String,
) {
    constructor() : this("","","","","")
}