package com.example.rccamfrontend.utils

class Address(private var ip: String, private var port: Double?) {
    // Attributes
    private val ipPat = Regex(
        "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])(\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])){3}\$"
    )

    val emptyIP = hasEmptyIP()
    val emptyPort = hasEmptyPort()
    val validIP = hasValidIP()
    val validPort = hasValidPort()

    // Returns true if the IP arg supplied was empty, else false
    private fun hasEmptyIP(): Boolean{
        if (ip == ""){
            return true
        }
        return false
    }

    // Returns true if the Port arg supplied was empty, else false
    private fun hasEmptyPort(): Boolean{
        if (port == null){
            return true
        }
        return false
    }

    // Returns true if the IP arg supplied was valid, else false
    private fun hasValidIP(): Boolean {
        if (emptyIP){
            return true
        }
        return ipPat.matches(ip)
    }

    // Returns true if the Port arg supplied was valid, else false
    private fun hasValidPort(): Boolean {
        if (emptyPort){
            return true
        }
        if (port?.let { it > 65535 } == false) {
            return true
        }
        return false
    }
}
