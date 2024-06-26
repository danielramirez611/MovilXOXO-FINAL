package com.joranprojects.pruebaregister.Resgitrar.Model


data class NUser(
    val nombre: String,
    val apellido: String,
    val correo: String,
    val password: String,
    val roleRequest: RoleRequest = RoleRequest(listOf("USER")) // Asegúrate de que RoleRequest maneje una lista de roles
)
