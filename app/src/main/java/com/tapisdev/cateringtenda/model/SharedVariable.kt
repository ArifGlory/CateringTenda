package com.tapisdev.cateringtenda.model

class SharedVariable {
    //open var selectedIdPenyedia = "-"

    companion object {
        var selectedIdPenyedia: String = "-"
        var IdPenyediaCart: String = "-"
        var listCart = ArrayList<Cart>()
    }
}