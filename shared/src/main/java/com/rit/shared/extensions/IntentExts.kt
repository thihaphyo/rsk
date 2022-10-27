package com.rit.shared.extensions

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.fragment.app.Fragment

/**
 * Created by Chan Myae Aung on 4/20/21.
 */

fun Fragment.goToSetting() {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri = Uri.fromParts("package", requireActivity().packageName, null)
    intent.data = uri
    startActivity(intent)
}
