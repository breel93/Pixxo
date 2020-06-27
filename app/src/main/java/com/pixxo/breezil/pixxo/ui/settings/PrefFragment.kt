package com.pixxo.breezil.pixxo.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.PreferenceManager
import com.pixxo.breezil.pixxo.R

class PrefFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener{
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preference, rootKey)
    PreferenceManager.setDefaultValues(
      requireActivity(),
      R.xml.preference,
      false
    )
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
  }

  override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
    super.onSharedPreferenceChanged(sharedPreferences, key)
  }
  override fun onResume() {
    super.onResume()
    preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
  }

  override fun onPause() {
    preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    super.onPause()
  }

  override fun onStop() {
    preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    super.onStop()
  }
}