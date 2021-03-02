
# Android 10 深色主题的介绍与适配

[**介绍：**](https://developer.android.google.cn/guide/topics/ui/look-and-feel/darktheme#java)
> Android 10 (API 级别 29) 及更高版本中提供深色主题背景,深色主题背景同时适用于 Android 系统界面和在设备上运行的应用。

**深色主题背景具有诸多优势：**
>- 可大幅减少耗电量（具体取决于设备的屏幕技术）
>- 为弱视以及对强光敏感的用户提高可视性。
>- 让所有人都可以在光线较暗的环境中更轻松地使用设备。

.

.

**Android 10 深色主题的适配步骤：**

.

**1. 在`res`目录下创建`values-v29`目录，并创建`styles.xml`文件**


```java
<resources>
    <!--  
		深色主题：parent="Theme.AppCompat.DayNight.NoActionBar"
		浅色主题：parent="Theme.AppCompat.Light.NoActionBar"
	-->
    <!-- android:forceDarkAllowed属性并设置为true，说明现在我们是允许系统使用Force Dark将应用强制转换成深色主题的 -->

    <style name="AppTheme" parent="Theme.AppCompat.DayNight.NoActionBar">
        <!-- Customize your theme here. -->
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorAccent">@color/colorAccent</item>
        <!--
			<item name="android:forceDarkAllowed">true</item>
		-->	
    </style>
</resources>
```

> **说明：**
> 
> 在普通情况下仍然会使用浅色主题，但是一旦用户在系统设置中开启了深色主题，项目就会自动使用相应的深色主题。


**2. 在`res`目录下创建`values-night`目录，并创建`colors.xml`文件**


```java
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="colorPrimary">#303030</color>
    <color name="colorPrimaryDark">#232323</color>
    <color name="colorAccent">#008577</color>
</resources>
```

> **说明：**
> 
> 在普通情况下，系统仍然会读取`values/colors.xml`文件中的颜色值，而一旦用户开启了深色主题，系统就会去读取`values-night/colors.xml`文件中的颜色值了


.

.

### 手动切换深色主题

通过调用`AppCompatDelegate.setDefaultNightMode()`方法可以实现浅色主题与深色主题的切换，不过在切换前需要判断当前系统是浅色主题还是深色主题，可以通过`isDarkTheme()`方法来获取系统当前的主题，然后根据返回值执行不同的代码逻辑即可。

>**setDefaultNightMode()的介绍：**
>
>> **作用：** 用于控制当前应用程序的深色模式，接收一个mode参数。
> >
>> **mode参数可供选择：**
>>- **`MODE_NIGHT_FOLLOW_SYSTEM`：** 默认模式，表示让当前应用程序跟随系统设置来决定使用浅色主题还是深色主题。 
>>- **`MODE_NIGHT_YES`：** 脱离系统设置，强制让当前应用程序使用深色主题
>>- **`MODE_NIGHT_NO`：** 脱离系统设置，强制让当前应用程序使用浅色主题
>>- **`MODE_NIGHT_AUTO_BATTERY`：** 根据手机的电池状态来决定使用浅色主题还是深色主题，如果开启了节点模式，则使用深色主题

.


**切换步骤如下：**

**1. 定义一个方法获取当前的主题样式**

```java
private fun isDarkTheme(context: Context): Boolean {
    val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return flag == Configuration.UI_MODE_NIGHT_YES
}
```

**2. 在Activity中进行主题的切换操作**

```java
//浅色与深色主题切换按钮
btn_theme.setOnClickListener {

    if (isDarkTheme(this)) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        tv_theme.text = "当前是深色主题"
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        tv_theme.text = "当前是浅色主题"
    }
}
```

> **注意：**
> 
> 当调用`setDefaultNightMode()`方法并成功切换主题时，应用程序中所有处于`started状态`的Activity都会被重新创建，不在`started状态`的Activity则会在恢复started状态时再重新创建。


.

.

### 深色主题切换时避免Activity被重新创建

可以在 `AndroidManifest.xml` 中给Activity配置`android:configChanges="uiMode"`属性来让当前Activity在系统开启与关闭深色主题避免被重新创建。

**步骤如下：**

**1. 在`AndroidManifest.xml`配置相应属性**

```java
<activity
    android:name=".MainActivity"
    android:configChanges="uiMode" >
</activity>
```

**2. 在Activity中重写`onConfigurationChanged()`方法的回调**

```java
override fun onConfigurationChanged(newConfig: Configuration) {
    val currentNightMode = newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK
    when (currentNightMode) {
        Configuration.UI_MODE_NIGHT_NO -> {
			// 夜间模式未启用，使用浅色主题
			Toast.makeText(this,"关闭深色主题",Toast.LENGTH_SHORT).show()
		} 
        Configuration.UI_MODE_NIGHT_YES -> {
			// 夜间模式启用，使用深色主题
			Toast.makeText(this,"开启深色主题",Toast.LENGTH_SHORT).show()
		} 
    }
}
```


**说明：**
> 现在当应用程序的主题发生变化时，`MainActivity`并不会重新创建，而是会触发`onConfigurationChanged()`方法的回调，你可以在回调当中手动做一些逻辑处理，这里什么也没有做只是打印一个消息，所以当前的 `MainActivity` 就好像并没有切换主题一样，界面上也不会有任何变化。


**补充：**
> 如果在创建 `compileSdkVersion >=29` 的新项目时，系统会帮我们自动适配深色主题，他会在 `res` 目录下帮我们自动创建 `values-night`目录下创建 `themes.xml` 文件。文件代码如下：


```java
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Theme.DarkThemeDemo" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
        <!-- Primary brand color. -->
        <item name="colorPrimary">@color/purple_200</item>
        <item name="colorPrimaryVariant">@color/black</item>
        <item name="colorOnPrimary">@color/black</item>
        <!-- Secondary brand color. -->
        <item name="colorSecondary">@color/teal_200</item>
        <item name="colorSecondaryVariant">@color/teal_200</item>
        <item name="colorOnSecondary">@color/black</item>
        <!-- Status bar color. -->
        <item name="android:statusBarColor" tools:targetApi="l">?attr/colorPrimaryVariant</item>
        <!-- Customize your theme here. -->
    </style>
</resources>
```

> 这样就不要我们手动去适配深色主题模式，而手动切换深色主题模式代码同上述。

.

.

**项目演示：**

![在这里插入图片描述](https://img-blog.csdnimg.cn/20210302101156201.gif#pic_center)

**项目地址**
- [**码云地址**](https://gitee.com/qu-wenbin/dark-theme-demo)
- [**Github地址**](https://github.com/Ou520/DarkThemeDemo)




