package com.example.gestiondebureaudechangededevises.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.example.gestiondebureaudechangededevises.R
import com.example.gestiondebureaudechangededevises.data.models.Admin

class CurrentAdmin {
    companion object {
        private var liveData: MutableLiveData<Admin?> = MutableLiveData<Admin?>()
        fun isConnected() = liveData.value != null
        fun get() = liveData.value
        fun set(user: Admin?) {
            liveData.postValue(user)
        }

        fun logout() {
            liveData.postValue(null)
            SessionCookie.prefs.delete()
        }

        fun observe(lifecycleOwner: LifecycleOwner, onChanged: OnChanged<Admin>) {
            liveData.observe(lifecycleOwner) { user ->
                onChanged.onChange(user)
            }
        }

    }
}
interface OnChanged<T> {
    fun onChange(data: T?)
}