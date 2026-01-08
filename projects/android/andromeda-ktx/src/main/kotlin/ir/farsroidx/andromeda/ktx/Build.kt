@file:Suppress("ObsoleteSdkInt", "AnnotateVersionCheck", "unused", "FunctionName")

package ir.farsroidx.andromeda.ktx

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.annotation.RequiresApi

/**
 * Checks if the device is running at least Android 1.0 (API 1)
 * @return true if SDK_INT >= BASE
 * @see Build.VERSION_CODES.BASE
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.BASE)
fun isAndroidBase() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE

/**
 * Checks if the device is running at least Android 1.1 (API 2)
 * @return true if SDK_INT >= BASE_1_1
 * @see Build.VERSION_CODES.BASE_1_1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.BASE_1_1)
fun isAndroidBase1_1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.BASE_1_1

/**
 * Checks if the device is running at least Android 1.5 Cupcake (API 3)
 * @return true if SDK_INT >= CUPCAKE
 * @see Build.VERSION_CODES.CUPCAKE
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.CUPCAKE)
fun isAndroidCupcake() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE

/**
 * Checks if the device is running at least Android 1.6 Donut (API 4)
 * @return true if SDK_INT >= DONUT
 * @see Build.VERSION_CODES.DONUT
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.DONUT)
fun isAndroidDonut() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT

/**
 * Checks if the device is running at least Android 2.0 Eclair (API 5)
 * @return true if SDK_INT >= ECLAIR
 * @see Build.VERSION_CODES.ECLAIR
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.ECLAIR)
fun isAndroidEclair() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR

/**
 * Checks if the device is running at least Android 2.0.1 Eclair (API 6)
 * @return true if SDK_INT >= ECLAIR_0_1
 * @see Build.VERSION_CODES.ECLAIR_0_1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.ECLAIR_0_1)
fun isAndroidEclair01() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_0_1

/**
 * Checks if the device is running at least Android 2.1 Eclair MR1 (API 7)
 * @return true if SDK_INT >= ECLAIR_MR1
 * @see Build.VERSION_CODES.ECLAIR_MR1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.ECLAIR_MR1)
fun isAndroidEclairMR1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1

/**
 * Checks if the device is running at least Android 2.2.x Froyo (API 8)
 * @return true if SDK_INT >= FROYO
 * @see Build.VERSION_CODES.FROYO
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.FROYO)
fun isAndroidFroyo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO

/**
 * Checks if the device is running at least Android 2.3.0-2.3.2 Gingerbread (API 9)
 * @return true if SDK_INT >= GINGERBREAD
 * @see Build.VERSION_CODES.GINGERBREAD
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.GINGERBREAD)
fun isAndroidGingerbread() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD

/**
 * Checks if the device is running at least Android 2.3.3-2.3.7 Gingerbread MR1 (API 10)
 * @return true if SDK_INT >= GINGERBREAD_MR1
 * @see Build.VERSION_CODES.GINGERBREAD_MR1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.GINGERBREAD_MR1)
fun isAndroidGingerbreadMR1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1

/**
 * Checks if the device is running at least Android 3.0 Honeycomb (API 11)
 * @return true if SDK_INT >= HONEYCOMB
 * @see Build.VERSION_CODES.HONEYCOMB
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.HONEYCOMB)
fun isAndroidHoneycomb() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB

/**
 * Checks if the device is running at least Android 3.1 Honeycomb MR1 (API 12)
 * @return true if SDK_INT >= HONEYCOMB_MR1
 * @see Build.VERSION_CODES.HONEYCOMB_MR1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.HONEYCOMB_MR1)
fun isAndroidHoneycombMR1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1

/**
 * Checks if the device is running at least Android 3.2.x Honeycomb MR2 (API 13)
 * @return true if SDK_INT >= HONEYCOMB_MR2
 * @see Build.VERSION_CODES.HONEYCOMB_MR2
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.HONEYCOMB_MR2)
fun isAndroidHoneycombMR2() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2

/**
 * Checks if the device is running at least Android 4.0.1-4.0.2 Ice Cream Sandwich (API 14)
 * @return true if SDK_INT >= ICE_CREAM_SANDWICH
 * @see Build.VERSION_CODES.ICE_CREAM_SANDWICH
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
fun isAndroidIceCreamSandwich() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH

/**
 * Checks if the device is running at least Android 4.0.3-4.0.4 Ice Cream Sandwich MR1 (API 15)
 * @return true if SDK_INT >= ICE_CREAM_SANDWICH_MR1
 * @see Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
fun isAndroidIceCreamSandwichMR1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1

/**
 * Checks if the device is running at least Android 4.1.x Jelly Bean (API 16)
 * @return true if SDK_INT >= JELLY_BEAN
 * @see Build.VERSION_CODES.JELLY_BEAN
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.JELLY_BEAN)
fun isAndroidJellyBean() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN

/**
 * Checks if the device is running at least Android 4.2.x Jelly Bean MR1 (API 17)
 * @return true if SDK_INT >= JELLY_BEAN_MR1
 * @see Build.VERSION_CODES.JELLY_BEAN_MR1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
fun isAndroidJellyBeanMR1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1

/**
 * Checks if the device is running at least Android 4.3.x Jelly Bean MR2 (API 18)
 * @return true if SDK_INT >= JELLY_BEAN_MR2
 * @see Build.VERSION_CODES.JELLY_BEAN_MR2
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
fun isAndroidJellyBeanMR2() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2

/**
 * Checks if the device is running at least Android 4.4.x KitKat (API 19)
 * @return true if SDK_INT >= KITKAT
 * @see Build.VERSION_CODES.KITKAT
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.KITKAT)
fun isAndroidKitKat() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

/**
 * Checks if the device is running at least Android 4.4W KitKat for Wearables (API 20)
 * @return true if SDK_INT >= KITKAT_WATCH
 * @see Build.VERSION_CODES.KITKAT_WATCH
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.KITKAT_WATCH)
fun isAndroidKitKatWatch() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH

/**
 * Checks if the device is running at least Android 5.0 Lollipop (API 21)
 * @return true if SDK_INT >= LOLLIPOP
 * @see Build.VERSION_CODES.LOLLIPOP
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP)
fun isAndroidLollipop() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

/**
 * Checks if the device is running at least Android 5.1 Lollipop MR1 (API 22)
 * @return true if SDK_INT >= LOLLIPOP_MR1
 * @see Build.VERSION_CODES.LOLLIPOP_MR1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.LOLLIPOP_MR1)
fun isAndroidLollipopMR1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1

/**
 * Checks if the device is running at least Android 6.0 Marshmallow (API 23)
 * @return true if SDK_INT >= M
 * @see Build.VERSION_CODES.M
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
fun isAndroidMarshmallow() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

/**
 * Checks if the device is running at least Android 7.0 Nougat (API 24)
 * @return true if SDK_INT >= N
 * @see Build.VERSION_CODES.N
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N)
fun isAndroidNougat() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

/**
 * Checks if the device is running at least Android 7.1 Nougat MR1 (API 25)
 * @return true if SDK_INT >= N_MR1
 * @see Build.VERSION_CODES.N_MR1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.N_MR1)
fun isAndroidNougatMR1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1

/**
 * Checks if the device is running at least Android 8.0 Oreo (API 26)
 * @return true if SDK_INT >= O
 * @see Build.VERSION_CODES.O
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
fun isAndroidOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

/**
 * Checks if the device is running at least Android 8.1 Oreo MR1 (API 27)
 * @return true if SDK_INT >= O_MR1
 * @see Build.VERSION_CODES.O_MR1
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O_MR1)
fun isAndroidOreoMR1() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1

/**
 * Checks if the device is running at least Android 9 Pie (API 28)
 * @return true if SDK_INT >= P
 * @see Build.VERSION_CODES.P
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.P)
fun isAndroidPie() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

/**
 * Checks if the device is running at least Android 10 (API 29)
 * @return true if SDK_INT >= Q
 * @see Build.VERSION_CODES.Q
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.Q)
fun isAndroid10() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

/**
 * Checks if the device is running at least Android 11 (API 30)
 * @return true if SDK_INT >= R
 * @see Build.VERSION_CODES.R
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.R)
fun isAndroid11() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

/**
 * Checks if the device is running at least Android 12 (API 31)
 * @return true if SDK_INT >= S
 * @see Build.VERSION_CODES.S
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S)
fun isAndroid12() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

/**
 * Checks if the device is running at least Android 12L (API 32)
 * @return true if SDK_INT >= S_V2
 * @see Build.VERSION_CODES.S_V2
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.S_V2)
fun isAndroid12L() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2

/**
 * Checks if the device is running at least Android 13 Tiramisu (API 33)
 * @return true if SDK_INT >= TIRAMISU
 * @see Build.VERSION_CODES.TIRAMISU
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.TIRAMISU)
fun isAndroid13() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

/**
 * Checks if the device is running at least Android 14 Upside Down Cake (API 34)
 * @return true if SDK_INT >= UPSIDE_DOWN_CAKE
 * @see Build.VERSION_CODES.UPSIDE_DOWN_CAKE
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
fun isAndroid14() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE

/**
 * Checks if the device is running at least Android 15 Vanilla Ice Cream (API 35)
 * @return true if SDK_INT >= VANILLA_ICE_CREAM
 * @see Build.VERSION_CODES.VANILLA_ICE_CREAM
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
fun isAndroid15() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM

/**
 * Checks if the device is running at least Android 16 Baklava (API 36)
 * @return true if SDK_INT >= BAKLAVA
 * @see Build.VERSION_CODES.BAKLAVA
 */
@ChecksSdkIntAtLeast(api = Build.VERSION_CODES.BAKLAVA)
fun isAndroid16() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA

private fun function() {
    if (isAndroid11()) {
        myRequiresApi()
    }
}

@RequiresApi(value = Build.VERSION_CODES.R)
private fun myRequiresApi() = Unit
