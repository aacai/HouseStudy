# backdrop 0.0.1-alpha02 编译时用的 skiko 与运行时的 0.150.1 不一致。
# 只有 ProgressiveBlur / SdfShader / ColorFilter 这些本项目没用到的类受影响，
# 用到的 blur 走 RenderEffect.createBlurEffect，不碰这些签名。
-dontwarn com.kashif_e.backdrop.**
