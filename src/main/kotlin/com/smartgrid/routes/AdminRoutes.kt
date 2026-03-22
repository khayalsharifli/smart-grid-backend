package com.smartgrid.routes

import com.smartgrid.domain.dto.*
import com.smartgrid.domain.model.UserRole
import com.smartgrid.domain.service.AdminService
import com.smartgrid.plugins.userRole
import com.smartgrid.util.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.adminRoutes(adminService: AdminService) {
    authenticate(JwtConstants.AUTH_NAME) {
        route(Routes.ADMIN) {
            get(Routes.USERS) {
                val principal = call.principal<JWTPrincipal>()!!
                if (principal.userRole() != UserRole.ADMIN.name) {
                    return@get call.respond(HttpStatusCode.Forbidden, ApiResponse<Nothing>(success = false, error = ErrorMessages.ADMIN_ACCESS_REQUIRED))
                }
                val users = adminService.getAllUsers()
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = users))
            }

            put(Routes.USER_ROLE) {
                val principal = call.principal<JWTPrincipal>()!!
                if (principal.userRole() != UserRole.ADMIN.name) {
                    return@put call.respond(HttpStatusCode.Forbidden, ApiResponse<Nothing>(success = false, error = ErrorMessages.ADMIN_ACCESS_REQUIRED))
                }
                val id = call.parameters[Params.ID]
                    ?: return@put call.respond(HttpStatusCode.BadRequest, ApiResponse<Nothing>(success = false, error = ErrorMessages.MISSING_ID))
                val request = call.receive<ChangeRoleRequest>()
                val user = adminService.changeUserRole(id, request)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = user))
            }

            put(Routes.USER_BLOCK) {
                val principal = call.principal<JWTPrincipal>()!!
                if (principal.userRole() != UserRole.ADMIN.name) {
                    return@put call.respond(HttpStatusCode.Forbidden, ApiResponse<Nothing>(success = false, error = ErrorMessages.ADMIN_ACCESS_REQUIRED))
                }
                val id = call.parameters[Params.ID]
                    ?: return@put call.respond(HttpStatusCode.BadRequest, ApiResponse<Nothing>(success = false, error = ErrorMessages.MISSING_ID))
                val request = call.receive<BlockUserRequest>()
                val user = adminService.blockUser(id, request)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = user))
            }

            get(Routes.GRID_HEALTH) {
                val principal = call.principal<JWTPrincipal>()!!
                val role = principal.userRole()
                if (role != UserRole.ADMIN.name && role != UserRole.GRID_OPERATOR.name) {
                    return@get call.respond(HttpStatusCode.Forbidden, ApiResponse<Nothing>(success = false, error = ErrorMessages.ADMIN_OR_OPERATOR_REQUIRED))
                }
                val health = adminService.getGridHealth()
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = health))
            }

            post(Routes.GRID_EMERGENCY) {
                val principal = call.principal<JWTPrincipal>()!!
                if (principal.userRole() != UserRole.ADMIN.name) {
                    return@post call.respond(HttpStatusCode.Forbidden, ApiResponse<Nothing>(success = false, error = ErrorMessages.ADMIN_ACCESS_REQUIRED))
                }
                adminService.emergencyShutdown()
                call.respond(HttpStatusCode.OK, ApiResponse<String>(success = true, message = SuccessMessages.EMERGENCY_SHUTDOWN_INITIATED))
            }

            get(Routes.CONTRACTS) {
                val principal = call.principal<JWTPrincipal>()!!
                if (principal.userRole() != UserRole.ADMIN.name) {
                    return@get call.respond(HttpStatusCode.Forbidden, ApiResponse<Nothing>(success = false, error = ErrorMessages.ADMIN_ACCESS_REQUIRED))
                }
                val contracts = adminService.getContracts()
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = contracts))
            }

            post(Routes.CONTRACTS_DEPLOY) {
                val principal = call.principal<JWTPrincipal>()!!
                if (principal.userRole() != UserRole.ADMIN.name) {
                    return@post call.respond(HttpStatusCode.Forbidden, ApiResponse<Nothing>(success = false, error = ErrorMessages.ADMIN_ACCESS_REQUIRED))
                }
                val request = call.receive<DeployContractRequest>()
                val contract = adminService.deployContract(request)
                call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = contract))
            }
        }
    }
}
