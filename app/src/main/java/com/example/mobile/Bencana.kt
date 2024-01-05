package com.example.mobile

data class Bencana (
    val id: String,
    val judul: String,
    val bencana: String,
    val kategori: String,
    val target: String,
    val tingkat: String,
    val nama: String,
    val norek: String
) {
    constructor() : this("","","","","","","","")
}