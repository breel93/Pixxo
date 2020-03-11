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
import androidx.appcompat.widget.AppCompatImageView;

public class CustomImageView extends AppCompatImageView {
  private double aspectRatio;

  public CustomImageView(Context context) {
    super(context);
  }

  public CustomImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public CustomImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    this.aspectRatio = -1.0;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    if (this.aspectRatio != -1.0) {
      int width = this.getMeasuredWidth();
      int height = (int) ((double) width * this.aspectRatio);
      if (height != this.getMeasuredHeight()) {
        this.setMeasuredDimension(width, height);
      }
    }
  }

  public double getAspectRatio() {
    return aspectRatio;
  }

  public void setAspectRatio(double aspectRatio) {
    this.aspectRatio = aspectRatio;
  }

  // private float mAspectRatio = 1.5f;
  //
  // public DynamicHeightNetworkImageView(Context context) {
  //    super(context);
  // }
  //
  // public DynamicHeightNetworkImageView(Context context, AttributeSet attrs) {
  //    super(context, attrs);
  // }
  //
  // public DynamicHeightNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
  //    super(context, attrs, defStyle);
  // }
  //
  // public void setAspectRatio(float aspectRatio) {
  //    mAspectRatio = aspectRatio;
  //    requestLayout();
  // }
  //
  // @Override
  // protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
  //    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  //    int measuredWidth = getMeasuredWidth();
  //    setMeasuredDimension(measuredWidth, (int) (measuredWidth / mAspectRatio));
  // }
}
