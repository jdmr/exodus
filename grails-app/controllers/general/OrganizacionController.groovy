package general

import org.springframework.dao.DataIntegrityViolationException

class OrganizacionController {

    static allowedMethods = [crea: "POST", actualiza: "POST", elimina: "POST"]

    def index() {
        redirect(action: "lista", params: params)
    }

    def lista() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [organizaciones: Organizacion.list(params), totalDeOrganizaciones: Organizacion.count()]
    }

    def nueva() {
        [organizacion: new Organizacion(params)]
    }

    def crea() {
        def organizacion = new Organizacion(params)
        if (!organizacion.save(flush: true)) {
            render(view: "nueva", model: [organizacion: organizacion])
            return
        }

		flash.message = message(code: 'organizacion.creada', args: [organizacion.nombre])
        redirect(action: "ver", id: organizacion.id)
    }

    def ver() {
        def organizacion = Organizacion.get(params.id)
        if (!organizacion) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'organizacion.label', default: 'Organizacion'), params.id])
            redirect(action: "lista")
            return
        }

        [organizacion: organizacion]
    }

    def edita() {
        def organizacion = Organizacion.get(params.id)
        if (!organizacion) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'organizacion.label', default: 'Organizacion'), params.id])
            redirect(action: "lista")
            return
        }

        [organizacion: organizacion]
    }

    def actualiza() {
        def organizacion = Organizacion.get(params.id)
        if (!organizacion) {
            log.error("No se encontro la organizacion")
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'organizacion.label', default: 'Organizacion'), params.id])
            redirect(action: "lista")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (organizacion.version > version) {
                log.error("La version no es la misma")
                organizacion.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'organizacion.label', default: 'Organizacion')] as Object[],
                          "Another user has updated this Organizacion while you were editing")
                render(view: "edita", model: [organizacion: organizacion])
                return
            }
        }

        organizacion.properties = params

        if (!organizacion.save(flush: true)) {
            log.error("No se pudo guardar la organizacion")
            render(view: "edita", model: [organizacion: organizacion])
            return
        }

		flash.message = message(code: 'default.updated.message', args: [message(code: 'organizacion.label', default: 'Organizacion'), organizacion.id])
        redirect(action: "ver", id: organizacion.id)
    }

    def elimina() {
        def organizacion = Organizacion.get(params.id)
        if (!organizacion) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'organizacion.label', default: 'Organizacion'), params.id])
            redirect(action: "lista")
            return
        }

        try {
            organizacion.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'organizacion.label', default: 'Organizacion'), params.id])
            redirect(action: "lista")
        }
        catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'organizacion.label', default: 'Organizacion'), params.id])
            redirect(action: "ver", id: params.id)
        }
    }
}
