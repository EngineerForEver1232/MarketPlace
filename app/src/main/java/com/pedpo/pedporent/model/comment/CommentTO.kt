package com.pedpo.pedporent.model.comment

class CommentTO {

    var description: String? = null;
    var registerDate: String? = null;
    var ownerFirstName: String? = null;
    var ownerLastName: String? = null;
    var image: String? = null;

    fun sum(): String? {
        return if (ownerFirstName == null && ownerLastName == null)
            ""
        else
            "${if (ownerFirstName == null) "" else ownerFirstName}" +
                    " ${if (ownerLastName == null) "" else ownerLastName}"
    }

}