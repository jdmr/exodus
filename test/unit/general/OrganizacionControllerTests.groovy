package general

import org.junit.*
import grails.test.mixin.*
import grails.test.mixin.domain.*
import javax.servlet.http.HttpServletResponse

@TestFor(OrganizacionController)
@TestMixin(DomainClassUnitTestMixin)
class OrganizacionControllerTests {

    @Test
    void debieraLlevarALista() {
        controller.index()
        assert "/organizacion/lista" == response.redirectedUrl
    }

    @Test
    void debieraMostrarListaDeOrganizaciones() {
        def lista = []
        for(i in 1..20) {
            lista << [codigo:"TST$i",nombre:"TEST$i",nombreCompleto:"TEST $i"]
        }
        mockDomain(Organizacion, lista)

        def model = controller.lista()

        assert model.organizaciones.size() == 10
        assert model.totalDeOrganizaciones == 20
    }

    @Test
    void debieraCrearUnaNuevaOrganizacion() {
        mockDomain(Organizacion)

        def model = controller.nueva()
        assert model.organizacion != null

        controller.crea()
        assert response.status == HttpServletResponse.SC_METHOD_NOT_ALLOWED

        response.reset()
        request.method = 'POST'
        controller.crea()

        assert model.organizacion != null
        assert view == '/organizacion/nueva'

        response.reset()

        params.codigo = 'TST-1'
        params.nombre = 'TEST-1'
        params.nombreCompleto = 'TEST 1'

        controller.crea()

        assert response.redirectedUrl == '/organizacion/ver/1'
        assert controller.flash.message != null
        assert Organizacion.count() == 1

    }

    @Test
    void debieraMostrarUnaOrganizacion() {
        mockDomain(Organizacion)

        controller.ver()

        assert flash.message != null
        assert response.redirectedUrl == '/organizacion/lista'


        def organizacion = new Organizacion()
        organizacion.codigo = 'TST-1'
        organizacion.nombre = 'TEST-1'
        organizacion.nombreCompleto = 'TEST 1'

        assert organizacion.save() != null

        params.id = organizacion.id

        def model = controller.ver()

        assert model.organizacion == organizacion
    }

    @Test
    void debieraModificarUnaOrganizacion() {
        mockDomain(Organizacion)
        
        controller.edita()

        assert flash.message != null
        assert response.redirectedUrl == '/organizacion/lista'


        def organizacion = new Organizacion()

        organizacion.codigo = 'TST-1'
        organizacion.nombre = 'TEST-1'
        organizacion.nombreCompleto = 'TEST 1'

        assert organizacion.save() != null

        params.id = organizacion.id

        def model = controller.edita()

        assert model.organizacion == organizacion

        controller.actualiza()
        assert response.status == HttpServletResponse.SC_METHOD_NOT_ALLOWED

        response.reset()
        request.method = 'POST'

        // test invalid parameters in update
        params.id = organizacion.id
        params.codigo = '7777777'

        controller.actualiza()

        assert view == "/organizacion/edita"
        assert model.organizacion != null

        organizacion.clearErrors()

        params.id = organizacion.id
        params.version = organizacion.version
        params.codigo = 'PRB-1'
        params.nombre = 'PRB-1'

        controller.actualiza()

        assert response.redirectedUrl == "/organizacion/ver/$organizacion.id"
        assert flash.message != null
    }

    @Test
    void debieraEliminarUnaOrganizacion() {
        mockDomain(Organizacion)

        controller.elimina()
        assert response.status == HttpServletResponse.SC_METHOD_NOT_ALLOWED

        response.reset()
        request.method = 'POST'
        controller.elimina()
        assert flash.message != null
        assert response.redirectedUrl == '/organizacion/lista'

        response.reset()

        def organizacion = new Organizacion()

        organizacion.codigo = 'TST-1'
        organizacion.nombre = 'TEST-1'
        organizacion.nombreCompleto = 'TEST 1'

        assert organizacion.save() != null
        assert Organizacion.count() == 1

        params.id = organizacion.id

        controller.elimina()

        assert Organizacion.count() == 0
        assert Organizacion.get(organizacion.id) == null
        assert response.redirectedUrl == '/organizacion/lista'
    }
}
