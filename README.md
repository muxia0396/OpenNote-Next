# OpenNote-Next

一款面向 Android 的本地笔记与文本文件阅读、编辑应用，使用 Kotlin 和 Jetpack Compose 构建。

![Android](https://img.shields.io/badge/Android-29%2B-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-2.2.20-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack-Compose-4285F4?logo=jetpackcompose&logoColor=white)
![License](https://img.shields.io/badge/License-GPL--3.0-blue)

## 项目说明

OpenNote-Next 基于 [OpenNote-Compose](https://github.com/YangDai2003/OpenNote-Compose) 进行二次开发，在保留笔记管理、Markdown 编辑与渲染、LaTeX、Mermaid、模板、导出、备份等能力的基础上，重点改进了本地文件导入、大型文本阅读、搜索、文件元数据和 Android 系统集成体验。

- 原项目地址：<https://github.com/YangDai2003/OpenNote-Compose>
- Android 应用 ID：`com.muxia0396.opennotenext`
- 最低支持版本：Android 10（API 29）

## OpenNote-Next 新增与改进

### 文件导入与源文件联动

- 首页右下角新增展开式操作菜单，支持新建文件、导入文件和导入文件夹。
- 支持从系统目录批量导入文件夹，并自动过滤不支持、不可读或体积异常的文件，降低大量文件导致崩溃的风险。
- 为单文件导入内容提供默认“导入文件”文件夹，方便统一归类。
- 支持在 Android 文件管理器中将 OpenNote-Next 作为 `.txt`、`.md`、`.markdown`、`.html` 等文本文件的打开方式。
- 对同一来源文件进行识别：再次从文件管理器打开时定位到既有记录，不重复导入。
- 编辑导入文件后可将内容同步写回源文件（需要系统授予对应文件的写入权限）。
- 增加桌面图标长按快捷操作：新建文件、导入文件、导入文件夹。

### 大型文本与阅读体验

- 针对大型 TXT 等文本文件引入分段加载与处理优化，减少打开长文档时的阻塞和卡顿。
- 保存每个文件的阅读位置，再次打开时自动回到上次浏览的位置。
- 优化编辑器输入链路，降低唤起输入法及长文本编辑时的卡顿。
- 文件概览中同时展示创建时间与编辑时间。

### 排序、时间与首页展示

- 新增按创建时间、编辑时间和文件首字母排序，并支持升序、降序切换。
- 排序方式持久化，不会因打开文档而恢复为默认值。
- 列表和网格卡片会根据当前排序字段显示对应的创建时间或编辑时间。
- 优先读取 Android 文件提供器暴露的创建时间，并结合可用文件元数据进行记录。
- 调整首页搜索、排序图标、文件卡片时间栏和悬浮操作按钮的布局与交互。

### 搜索能力

- 首页支持标题与正文全文搜索，并在结果中展示命中位置附近的内容片段。
- 搜索词使用荧光黄色背景高亮，点击结果可直接跳转到对应文档位置。
- 阅读模式加入文内搜索，支持上一处、下一处匹配和搜索词同步。
- 上下浏览文档后继续查找时，会以当前阅读位置为基准定位相邻匹配项。
- 点击搜索历史关键词会立即执行搜索。

### 批量管理与界面

- 长按文件进入批量管理时自动选中被长按的文件。
- 已选文件增加灰色遮罩，选择框调整到卡片右下角，并完善全选状态联动。
- 应用名称、包名、Logo、关于页面及相关界面统一为 OpenNote-Next。
- 重新设计首页新增操作、文件时间信息、侧边栏与应用图标资源。

### 云服务

- 修复设置中的云服务配置与同步操作流程，并移除测试状态提示。

## 主要功能

- 笔记与文件夹的创建、编辑、移动、删除和回收站管理
- Markdown / CommonMark / GFM 编辑与渲染
- LaTeX 数学公式和 Mermaid 图表
- 轻量编辑模式与经典编辑模式
- 文内查找、替换、目录大纲和字数统计
- 图片、音频和视频资源插入与预览
- TXT、Markdown、HTML、PDF 等格式导出
- 数据备份与恢复、应用锁、生物识别和屏幕隐私保护
- Material 3、自适应布局、桌面小组件及外接键鼠支持

## 技术栈

- Kotlin 2.2.20 / Java 17
- Jetpack Compose + Material 3
- MVVM / Clean Architecture
- Room、Hilt、KSP、DataStore、WorkManager
- Ktor、CommonMark
- Gradle Kotlin DSL

## 构建

使用 Android Studio 打开项目，等待 Gradle 同步完成后直接运行 `app` 模块；也可以在项目根目录执行：

```bash
./gradlew assembleDebug
```

Windows PowerShell：

```powershell
.\gradlew.bat assembleDebug
```

## 贡献

欢迎通过 Issue 报告问题、提出功能建议，也欢迎提交 Pull Request。提交代码前请确保相关功能可以正常构建，并尽量说明修改目的、影响范围和验证方式。

## 开源许可

本项目采用 [GNU General Public License v3.0](LICENSE) 开源。你可以在许可证允许的范围内使用、研究、修改和分发本项目；分发修改版本时须遵守 GPL-3.0 的源代码公开及同许可证授权要求。

详细条款请参阅仓库中的 [LICENSE](LICENSE) 文件。
