package com.smartgrid.plugins

import com.smartgrid.util.ServerConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS() {
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowCredentials = true
        allowNonSimpleContentTypes = true
        allowHost(ServerConfig.BIND_HOST, schemes = listOf(ServerConfig.SCHEME_HTTP, ServerConfig.SCHEME_HTTPS))
    }
}
