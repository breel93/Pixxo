/**
 * Designed and developed by Kola Emiola
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pixxo.breezil.pixxo.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mikhaellopez.rxanimation.*
import com.pixxo.breezil.pixxo.R
import com.pixxo.breezil.pixxo.databinding.ActivitySplashScreenBinding
import com.pixxo.breezil.pixxo.ui.BaseActivity
import com.pixxo.breezil.pixxo.ui.main.MainActivity
import io.reactivex.disposables.CompositeDisposable

class SplashScreenActivity : BaseActivity() {
  private lateinit var binding: ActivitySplashScreenBinding
  private var disposable = CompositeDisposable()
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = DataBindingUtil.setContentView(this, R.layout.activity_splash_screen)
    startSplashAnimation()
  }

  private fun startSplashAnimation() {
    disposable.add(
      RxAnimation.sequentially(
        RxAnimation.together(
          binding.baseLayer.fadeOut(0L),
          binding.topLayer.fadeOut(0L),
          binding.blackDot.fadeOut(0L)
        ),
        RxAnimation.together(
          binding.baseLayer.fadeIn(1000L),
          binding.topLayer.fadeIn(1000L,null, startDelay = 1000L),
          binding.blackDot.fadeIn(1000L,null, startDelay = 2000L)
        )
      ).doOnTerminate {
        endSplashAnimate()
      }.subscribe()
    )
  }

  private fun endSplashAnimate() {
    disposable.add(
      RxAnimation.sequentially(
        RxAnimation.together(
          binding.blackDot.fadeOut(200L, startDelay = 0L),
          binding.topLayer.fadeOut(200L,startDelay = 200L),
          binding.baseLayer.fadeOut(200L,startDelay = 400L)
        )
      ).doOnTerminate {
        val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
      }.subscribe()
    )
  }
  override fun onDestroy() {
    super.onDestroy()
    disposable.clear()
  }

}