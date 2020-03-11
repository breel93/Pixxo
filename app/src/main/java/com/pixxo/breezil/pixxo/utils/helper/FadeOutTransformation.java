/**
 * Designed and developed by Kola Emiola
 *
 * <p>Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * <p>http://www.apache.org/licenses/LICENSE-2.0
 *
 * <p>Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pixxo.breezil.pixxo.utils.helper;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

public class FadeOutTransformation implements ViewPager.PageTransformer {
  @Override
  public void transformPage(@NonNull View page, float position) {

    if (position > -1.0F && position < 1.0F) {
      if (position == 0.0F) {
        page.setTranslationX((float) page.getWidth() * position);
        page.setAlpha(1.0F);
      } else {
        page.setTranslationX((float) page.getWidth() * -position);
        float var4 = 1.0F;
        float var5 = Math.abs(position);
        page.setAlpha(var4 - var5);
      }
    } else {
      page.setTranslationX((float) page.getWidth() * position);
      page.setAlpha(0.0F);
    }
  }
}
