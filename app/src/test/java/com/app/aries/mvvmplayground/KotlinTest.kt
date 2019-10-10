package com.app.aries.mvvmplayground

import org.junit.Test

class KotlinTest {

    var a = mutableListOf("a1","a2","a3")
    var b = mutableListOf("b1","b2","b3")

    @Test
    fun mutableListTest() {
        a.forEach{
            println(it)
        }
        println("")

        a.addAll(0,b)
        a.forEach{
            println(it)
        }
        println("")

        b.add("b4")
        a.forEach{
            println(it)
        }
    }
}