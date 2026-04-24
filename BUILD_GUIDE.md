# WebView Shell APK 构建指南

## 更新内容

### 新增功能
- ✅ **鼠标右键退出** - 在WebView页面右键点击，选择"退出程序"即可退出
- ✅ 确认对话框防止误操作

## 快速构建方案

### 方案一：本地构建（推荐）

#### 1. 安装Android Studio
下载地址：https://developer.android.com/studio

#### 2. 导入项目
```bash
# 打开Android Studio
# File -> Open -> 选择 WebViewShell 文件夹
```

#### 3. 修改配置（可选）
编辑 `app/src/main/assets/config.txt`，填入你的目标网址：
```
https://your-website.com
```

#### 4. 构建APK
- Build -> Build Bundle(s) / APK(s) -> Build APK(s)
- 或使用命令行：
```bash
./gradlew assembleDebug
```

#### 5. 获取APK
构建完成后APK位于：`app/build/outputs/apk/debug/app-debug.apk`

---

### 方案二：命令行构建（无需Android Studio）

#### 1. 安装Android SDK命令行工具

**MacOS:**
```bash
# 使用Homebrew安装
brew install android-commandlinetools

# 设置环境变量
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin:$ANDROID_HOME/platform-tools
```

**Linux:**
```bash
# 下载命令行工具
wget https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip
unzip commandlinetools-linux-11076708_latest.zip -d ~/android-sdk/

# 设置环境变量
export ANDROID_HOME=$HOME/android-sdk
export PATH=$PATH:$ANDROID_HOME/cmdline-tools/bin:$ANDROID_HOME/platform-tools
```

#### 2. 安装必要组件
```bash
sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
```

#### 3. 构建APK
```bash
cd WebViewShell
./gradlew assembleDebug
```

---

### 方案三：GitHub Actions自动构建

创建 `.github/workflows/build.yml`：

```yaml
name: Build APK

on: [push]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'temurin'
        
    - name: Setup Android SDK
      uses: android-actions/setup-android@v2
      
    - name: Build APK
      run: ./gradlew assembleDebug
      
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

---

## 项目文件清单

```
WebViewShell/
├── app/
│   ├── build.gradle
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   └── config.txt          # 网址配置文件
│       ├── java/com/example/webviewshell/
│       │   ├── MainActivity.java   # 主界面（含右键菜单）
│       │   └── BootReceiver.java   # 开机自启动
│       └── res/
├── build.gradle
├── settings.gradle
├── gradle.properties
├── local.properties
├── gradlew                         # Gradle Wrapper
├── gradle/wrapper/
│   ├── gradle-wrapper.jar
│   └── gradle-wrapper.properties
└── README.md
```

## 功能说明

### 右键退出
- 在WebView页面任意位置**长按**或**右键点击**
- 弹出菜单选择"退出程序"
- 确认对话框点击"确定"后退出

### 开机自启动
- 应用已配置开机自启动权限
- 部分设备需在系统设置中手动开启自启动权限

### 分辨率适配
- 强制横屏模式
- 全屏显示，隐藏状态栏和导航栏
- 针对1920x1080分辨率优化

## 常见问题

**Q: 构建失败提示找不到SDK**
A: 确保已设置 `ANDROID_HOME` 环境变量，并安装了platform-tools和build-tools

**Q: 开机自启动不生效**
A: 检查系统设置中的应用自启动权限，部分国产ROM需要手动开启

**Q: 网页加载失败**
A: 检查config.txt中的网址是否正确，确保包含http://或https://
