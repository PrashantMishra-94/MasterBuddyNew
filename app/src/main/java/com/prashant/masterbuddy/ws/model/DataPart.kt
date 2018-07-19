package com.prashant.masterbuddy.ws.model

/**
 * Created by prashant.mishra on 22/05/18.
 */

class DataPart(name: String, data: ByteArray) {
    val fileName: String = name
    val content: ByteArray = data
    val type: String? = null

}
