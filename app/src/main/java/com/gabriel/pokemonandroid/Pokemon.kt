package com.gabriel.pokemonandroid

import android.location.Location

/**
 * Created by Gabriel on 10/27/17.
 */

class Pokemon{

    var img:    Int?=null
    var des:    String?=null
    var name:   String?=null
    var power:  Double?=null
    var location: Location?=null

    var isCatch:Boolean?=false

    constructor(img: Int, name: String, des: String, power: Double, lat: Double, long: Double){
        this.des    = des
        this.img    = img
        this.name   = name
        this.power  = power
        this.isCatch= false
        this.location= Location(name)
        this.location!!.latitude = lat
        this.location!!.longitude = long
    }
}