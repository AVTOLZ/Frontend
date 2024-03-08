package icons.appicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import icons.AppIcons

public val AppIcons.Printer: ImageVector
    get() {
        if (_printer != null) {
            return _printer!!
        }
        _printer = Builder(name = "Printer", defaultWidth = 24.0.dp, defaultHeight = 24.0.dp,
                viewportWidth = 24.0f, viewportHeight = 24.0f).apply {
            path(fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                    strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                    pathFillType = NonZero) {
                moveTo(18.0f, 3.0f)
                horizontalLineTo(6.0f)
                verticalLineTo(7.0f)
                horizontalLineTo(18.0f)
                moveTo(19.0f, 12.0f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 18.0f, 11.0f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 19.0f, 10.0f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 20.0f, 11.0f)
                arcTo(1.0f, 1.0f, 0.0f, false, true, 19.0f, 12.0f)
                moveTo(16.0f, 19.0f)
                horizontalLineTo(8.0f)
                verticalLineTo(14.0f)
                horizontalLineTo(16.0f)
                moveTo(19.0f, 8.0f)
                horizontalLineTo(5.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, false, 2.0f, 11.0f)
                verticalLineTo(17.0f)
                horizontalLineTo(6.0f)
                verticalLineTo(21.0f)
                horizontalLineTo(18.0f)
                verticalLineTo(17.0f)
                horizontalLineTo(22.0f)
                verticalLineTo(11.0f)
                arcTo(3.0f, 3.0f, 0.0f, false, false, 19.0f, 8.0f)
                close()
            }
        }
        .build()
        return _printer!!
    }

private var _printer: ImageVector? = null