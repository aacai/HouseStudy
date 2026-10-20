package zhiqiu.hs

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform