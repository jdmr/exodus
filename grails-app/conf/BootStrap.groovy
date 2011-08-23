class BootStrap {

    def init = { servletContext ->
        log.info 'Inicializando aplicación'
        log.info 'Validando Roles'
        def rolAdmin = general.Rol.findByAuthority('ROLE_ADMIN')
        if (general.Rol.count() != 4) {
            if (!rolAdmin) {
                rolAdmin = new general.Rol(authority: 'ROLE_ADMIN').save(flush:true)
            }
            def rolOrg = general.Rol.findByAuthority('ROLE_ORG')
            if (!rolOrg) {
                rolOrg = new general.Rol(authority: 'ROLE_ORG').save(flush:true)
            }
            def rolEmp = general.Rol.findByAuthority('ROLE_EMP')
            if (!rolEmp) {
                rolEmp = new general.Rol(authority: 'ROLE_EMP').save(flush:true)
            }
            def rolUser = general.Rol.findByAuthority('ROLE_USER')
            if (!rolUser) {
                rolUser = new general.Rol(authority: 'ROLE_USER').save(flush:true)
            }
        }

        log.info "Validando usuarios"
        def admin = general.UsuarioRol.findByRol(rolAdmin)
        if (!admin) {
            admin = new general.Usuario(
                username:'admin'
                ,password:'admin'
                ,nombre:'David'
                ,apellido:'Mendoza'
                ,correo:'david.mendoza@um.edu.mx'
            )
            admin.save(flush:true)
            general.UsuarioRol.create(admin, rolAdmin, true)
        }

        log.info("Aplicacion inicializada")
    }
    def destroy = {
    }
}
