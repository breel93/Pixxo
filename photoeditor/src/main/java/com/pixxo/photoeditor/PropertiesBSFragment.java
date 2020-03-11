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
package com.pixxo.photoeditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PropertiesBSFragment extends BottomSheetDialogFragment
    implements SeekBar.OnSeekBarChangeListener {

  public PropertiesBSFragment() {
    // Required empty public constructor
  }

  private Properties mProperties;

  public interface Properties {
    void onColorChanged(int colorCode);

    void onOpacityChanged(int opacity);

    void onBrushSizeChanged(int brushSize);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_bottom_properties_dialog, container, false);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    RecyclerView rvColor = view.findViewById(R.id.rvColors);
    SeekBar sbOpacity = view.findViewById(R.id.sbOpacity);
    SeekBar sbBrushSize = view.findViewById(R.id.sbSize);

    sbOpacity.setOnSeekBarChangeListener(this);
    sbBrushSize.setOnSeekBarChangeListener(this);

    LinearLayoutManager layoutManager =
        new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
    rvColor.setLayoutManager(layoutManager);
    rvColor.setHasFixedSize(true);
    ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(getActivity());
    colorPickerAdapter.setOnColorPickerClickListener(
        new ColorPickerAdapter.OnColorPickerClickListener() {
          @Override
          public void onColorPickerClickListener(int colorCode) {
            if (mProperties != null) {
              dismiss();
              mProperties.onColorChanged(colorCode);
            }
          }
        });
    rvColor.setAdapter(colorPickerAdapter);
  }

  public void setPropertiesChangeListener(Properties properties) {
    mProperties = properties;
  }

  @Override
  public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    int id = seekBar.getId();
    if (id == R.id.sbOpacity) {
      if (mProperties != null) {
        mProperties.onOpacityChanged(i);
      }
    } else if (id == R.id.sbSize) {
      if (mProperties != null) {
        mProperties.onBrushSizeChanged(i);
      }
    }
  }

  @Override
  public void onStartTrackingTouch(SeekBar seekBar) {}

  @Override
  public void onStopTrackingTouch(SeekBar seekBar) {}
}
