package ve.com.teeac.svgs.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import ve.com.teeac.svgs.R

val Gayathri = FontFamily(
    Font(R.font.gayathri_bold),
    Font(R.font.gayathri_regular),
    Font(R.font.gayathri_thin)
)

val Montserrat = FontFamily(
    Font(R.font.montserrat_bold),
    Font(R.font.montserrat_medium),
    Font(R.font.montserrat_regular)
)

// Set of Material typography styles to start with
val AppTypography = Typography(
    h1 = TextStyle(
        fontFamily = Gayathri,
        fontWeight = FontWeight.Thin,
        fontSize = 86.sp,
        letterSpacing = (-1.5).sp
    ),
    h2 = TextStyle(
        fontFamily = Gayathri,
        fontWeight = FontWeight.Thin,
        fontSize = 53.sp,
        letterSpacing = (-0.5).sp
    ),
    h3 = TextStyle(
        fontFamily = Gayathri,
        fontWeight = FontWeight.Normal,
        fontSize = 43.sp,
        letterSpacing = 0.sp
    ),
    h4 = TextStyle(
        fontFamily = Gayathri,
        fontWeight = FontWeight.Normal,
        fontSize = 30.sp,
        letterSpacing = 0.25.sp
    ),
    h5 = TextStyle(
        fontFamily = Gayathri,
        fontWeight = FontWeight.Normal,
        fontSize = 21.sp,
        letterSpacing = 0.sp
    ),
    h6 = TextStyle(
        fontFamily = Gayathri,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle1 = TextStyle(
        fontFamily = Gayathri,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.15.sp
    ),
    subtitle2 = TextStyle(
        fontFamily = Gayathri,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.1.sp
    ),
    body1 = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.5.sp
    ),
    body2 = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.25.sp
    ),
    button = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 1.25.sp
    ),
    caption = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        letterSpacing = 0.4.sp
    ),
    overline = TextStyle(
        fontFamily = Montserrat,
        fontWeight = FontWeight.Normal,
        fontSize = 9.sp,
        letterSpacing = 1.5.sp
    )

)
