package com.example.rccamfrontend.utils

class Address(private var ip: String, private var port: Int?) {
    // Attributes
    private val ipPat = Regex(
        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}\$"
    )

    // Returns true if the IP arg supplied was empty, else false
    fun hasEmptyIP(): Boolean{
        if (ip == ""){
            return true
        }
        return false
    }

    // Returns true if the Port arg supplied was empty, else false
    fun hasEmptyPort(): Boolean{
        if (port == null){
            return true
        }
        return false
    }

    // Returns true if the IP arg supplied was valid, else false
    fun hasValidIP(): Boolean {
        return ipPat.matches(ip)
    }

    // Returns true if the Port arg supplied was valid, else false
    fun hasValidPort(): Boolean {
        if (port?.let { it > 65536 } == false) {
            return true
        }
        return false
    }
}




