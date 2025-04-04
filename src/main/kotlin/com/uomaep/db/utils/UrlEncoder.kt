package com.uomaep.db.utils

import java.net.URLEncoder

fun String.encodeURL() = URLEncoder.encode(this, "UTF-8")