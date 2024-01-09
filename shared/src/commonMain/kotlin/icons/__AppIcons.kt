package icons

import androidx.compose.ui.graphics.vector.ImageVector
import icons.appicons.Icon
import icons.appicons.Magister
import icons.appicons.Visibility
import icons.appicons.VisiblityOff
import kotlin.collections.List as ____KtList

public object AppIcons

private var __AllAssets: ____KtList<ImageVector>? = null

public val AppIcons.AllAssets: ____KtList<ImageVector>
  get() {
    if (__AllAssets != null) {
      return __AllAssets!!
    }
    __AllAssets= listOf(Icon, Magister, Visibility, VisiblityOff)
    return __AllAssets!!
  }
