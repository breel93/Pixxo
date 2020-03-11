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
package com.pixxo.breezil.pixxo.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.pixxo.breezil.pixxo.databinding.QuickSelectItemBinding;
import com.pixxo.breezil.pixxo.ui.callbacks.QuickSelectListener;

public class QuickSelectRecyclerListAdapter
    extends RecyclerView.Adapter<QuickSelectRecyclerListAdapter.QuickSelectViewHolder> {

  private String[] quickSelectName;
  private String[] quickSelectColor;
  private QuickSelectListener quickSelectListener;

  public QuickSelectRecyclerListAdapter(
      String[] quickSelectName,
      String[] quickSelectColor,
      QuickSelectListener quickSelectListener) {
    this.quickSelectName = quickSelectName;
    this.quickSelectColor = quickSelectColor;
    this.quickSelectListener = quickSelectListener;
  }

  @NonNull
  @Override
  public QuickSelectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
    QuickSelectItemBinding binding = QuickSelectItemBinding.inflate(layoutInflater, parent, false);
    return new QuickSelectViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(@NonNull QuickSelectViewHolder holder, int position) {
    holder.bind(quickSelectName, quickSelectColor, position, quickSelectListener);
  }

  @Override
  public int getItemCount() {
    return quickSelectName.length;
  }

  class QuickSelectViewHolder extends RecyclerView.ViewHolder {
    QuickSelectItemBinding binding;

    QuickSelectViewHolder(QuickSelectItemBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    void bind(
        String[] quickSelectName,
        String[] quickSelectColor,
        int position,
        QuickSelectListener quickSelectListener) {
      binding.quickSelectLayout.setCardBackgroundColor(
          Color.parseColor(quickSelectColor[position]));
      binding.quickSelectName.setText(quickSelectName[position]);
      itemView.setOnClickListener(v -> quickSelectListener.getString(quickSelectName[position]));
    }
  }
}
