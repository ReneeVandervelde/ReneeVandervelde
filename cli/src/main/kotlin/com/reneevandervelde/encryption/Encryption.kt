package com.reneevandervelde.encryption

import com.reneevandervelde.addScript
import ink.ui.render.statichtml.InkUiScript

fun InkUiScript.useEncryption()
{
    addScript(resource("js/urldecrypt.js"))
    addStyle(resource("css/encryption-v1.css"))
}
