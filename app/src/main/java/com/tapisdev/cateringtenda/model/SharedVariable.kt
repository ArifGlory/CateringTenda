package com.tapisdev.cateringtenda.model

class SharedVariable {
    //open var selectedIdPenyedia = "-"

    companion object {
        var selectedIdPenyedia: String = "-"
        var IdPenyediaCart: String = "-"
        var totalKeranjang : Int = 0
        var listCart = ArrayList<Cart>()
    }
}