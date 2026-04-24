# WebView Shell - Android APK

一个Android WebView壳应用，支持通过配置文件加载指定网址，并具备开机自启动功能。

## 功能特性

- ✅ **配置文件加载网址** - 通过 `assets/config.txt` 配置要加载的网址
- ✅ **开机自启动** - 设备开机后自动启动应用
- ✅ **全屏显示** - 隐藏状态栏和导航栏，沉浸式体验
- ✅ **横屏模式** - 针对1920x1080分辨率优化
- ✅ **底部导航按钮** - 前进、后退、首页功能按钮
- ✅ **右键退出** - 长按/右键点击页面可退出程序
- ✅ **Android 9.1+ 兼容** - minSdk 28 (Android 9.0)

## 项目结构

```
WebViewShell/
├── app/
│   ├── src/main/
│   │   ├── java/com/example/webviewshell/
│   │   │   ├── MainActivity.java      # 主Activity，WebView加载
│   │   │   └── BootReceiver.java      # 开机广播接收器
│   │   ├── res/                       # 资源文件
│   │   └── assets/
│   │       └── config.txt             # 网址配置文件
│   └── build.gradle                   # 应用级Gradle配置
├── build.gradle                       # 项目级Gradle配置
└── settings.gradle
```

## 使用方法

### 1. 配置网址

编辑 `app/src/main/assets/config.txt` 文件，将默认网址替换为你需要加载的网址：

```
https://your-website.com
```

### 2. 构建APK

```bash
# 进入项目目录
cd WebViewShell

# 构建Release版本APK
./gradlew assembleRelease

# 或者构建Debug版本
./gradlew assembleDebug
```

构建完成后，APK文件位于：
- Release: `app/build/outputs/apk/release/app-release-unsigned.apk`
- Debug: `app/build/outputs/apk/debug/app-debug.apk`

### 3. 安装到设备

```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

## 权限说明

应用需要以下权限：

- `INTERNET` - 访问网络加载网页
- `RECEIVE_BOOT_COMPLETED` - 接收开机完成广播

## 开机自启动设置

应用已配置开机自启动功能，但某些设备可能需要额外设置：

1. **系统设置** - 在系统设置中允许应用自启动
2. **电池优化** - 将应用加入电池优化白名单
3. **权限管理** - 确保已授予"自启动"权限

## 技术规格

- **最低SDK**: 28 (Android 9.0)
- **目标SDK**: 34 (Android 14)
- **屏幕方向**: 横屏 (Landscape)
- **分辨率适配**: 1920x1080

## 使用说明

### 底部导航按钮
- **← 后退** - 返回上一页（如果存在）
- **⌂ 首页** - 返回配置文件中设置的首页
- **前进 →** - 前进到下一页（如果存在）

### 右键退出
- 在页面任意位置**长按**或**右键点击**
- 选择"退出程序"，确认后即可退出

## 注意事项

1. 修改 `config.txt` 后需要重新构建APK
2. 首次安装后可能需要手动打开一次应用以获取必要权限
3. 某些国产ROM可能会限制开机自启动功能，需要在系统设置中手动开启
