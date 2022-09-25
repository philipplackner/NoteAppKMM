package com.plcoding.noteappkmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform