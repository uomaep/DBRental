package com.uomaep.db.configure

import org.springframework.stereotype.Component

@Component
class BlockSettings {
    companion object {
        var blockLogin: Boolean = false
        var blockRegister: Boolean = false
        var blockCreateDatabase: Boolean = false
    }
}
