package com.uomaep.db.utils

import jakarta.servlet.http.HttpSession

class CsrfUtil {
    companion object {
        fun isValid(token: String?, session: HttpSession, denyNewSession: Boolean = false): Boolean {
            if(denyNewSession && session.isNew) return false
            return session.getAttribute("csrf") == (token ?: "")
        }
    }
}