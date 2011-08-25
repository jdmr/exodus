package general

class Organizacion implements Serializable {
    String codigo
    String nombre
    String nombreCompleto

    static constraints = {
        codigo         blank: false, maxSize:   6, unique: true
        nombre         blank: false, maxSize:  64, unique: true
        nombreCompleto blank: false, maxSize: 128, unique: true
    }

    static mapping = {
        table 'organizaciones'
    }

    String toString() {
        return nombre
    }
        
}
