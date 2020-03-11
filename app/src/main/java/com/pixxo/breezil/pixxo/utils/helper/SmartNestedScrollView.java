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

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

public class SmartNestedScrollView extends NestedScrollView {

  @Override
  protected void measureChildWithMargins(
      View child,
      int parentWidthMeasureSpec,
      int widthUsed,
      int parentHeightMeasureSpec,
      int heightUsed) {
    if (findNestedRecyclerView(child) != null) {
      MarginLayoutParams marginLayoutParams = (MarginLayoutParams) child.getLayoutParams();
      int childHeightMeasureSpec =
          MeasureSpec.makeMeasureSpec(
              marginLayoutParams.topMargin + marginLayoutParams.bottomMargin, MeasureSpec.AT_MOST);
      child.measure(parentWidthMeasureSpec, childHeightMeasureSpec);

    } else {
      super.measureChildWithMargins(
          child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }
  }

  private RecyclerView findNestedRecyclerView(View view) {
    if (view instanceof RecyclerView) {
      return (RecyclerView) view;
    } else if (view instanceof ViewGroup) {
      int index = 0;

      do {
        View child = ((ViewGroup) view).getChildAt(index);
        RecyclerView recyclerView = this.findNestedRecyclerView(child);
        if (recyclerView != null) {
          return recyclerView;
        }

        ++index;
      } while (index < ((ViewGroup) view).getChildCount());

      return null;
    } else {
      return null;
    }
  }

  public SmartNestedScrollView(@NonNull Context context) {
    super(context);
  }

  public SmartNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
  }

  public SmartNestedScrollView(
      @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }
}
