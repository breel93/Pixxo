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
package com.pixxo.breezil.pixxo.utils;

import static com.pixxo.breezil.pixxo.utils.Constant.ZERO;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.lang.reflect.Field;
import timber.log.Timber;

public class BottomNavigationHelper {
  private static String UNABLE_TO_GET_SHIFT = "Unable to get shift mode field";
  private static String UNABLE_TO_CHANGE_VALUE = "Unable to change value of shift mode";
  private static String SHIFTING_MODE = "mShiftingMode";

  public static void disableShiftMode(BottomNavigationView view) {
    BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(ZERO);
    try {
      Field shiftingMode = menuView.getClass().getDeclaredField(SHIFTING_MODE);
      shiftingMode.setAccessible(true);
      shiftingMode.setBoolean(menuView, false);
      shiftingMode.setAccessible(false);
      for (int i = 0; i < menuView.getChildCount(); i++) {
        BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
        //noinspection RestrictedApi
        item.setShifting(false);

        // set once again checked value, so view will be updated
        //noinspection RestrictedApi

        item.setChecked(item.getItemData().isChecked());
      }
    } catch (NoSuchFieldException e) {
      Timber.e(e, UNABLE_TO_GET_SHIFT);
    } catch (IllegalAccessException e) {
      Timber.e(e, UNABLE_TO_CHANGE_VALUE);
    }
  }
}
