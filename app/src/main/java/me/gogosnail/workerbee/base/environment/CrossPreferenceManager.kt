package me.gogosnail.workerbee.base.environment

import android.content.Context
import java.lang.ref.WeakReference

/** Created by max on 2019/12/15.<br/>
 */
object CrossPreferenceManager {
    private val cache = HashMap<String, WeakReference<CrossSharedPreference>>()
    fun getCrossSharedPreference(context: Context, name: String): CrossSharedPreference {
        var preference = cache.get(name)?.get()
        if (preference == null) {
            preference = CrossSharedPreference(context, name)
            cache[name] = WeakReference(preference)
        }
        return preference
    }

    fun getDefaultSharedPreference(context: Context):CrossSharedPreference {
        return getCrossSharedPreference(context, "cross_default_sp")
    }
}