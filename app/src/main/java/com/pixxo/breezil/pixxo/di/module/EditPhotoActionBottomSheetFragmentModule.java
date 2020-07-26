package com.pixxo.breezil.pixxo.di.module;

import com.pixxo.breezil.pixxo.ui.bottom_sheet.EditPhotoActionBottomSheetFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class EditPhotoActionBottomSheetFragmentModule {
  @ContributesAndroidInjector
  abstract EditPhotoActionBottomSheetFragment contributeEditPhotoActionBottomSheetFragment();
}