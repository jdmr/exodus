<%@ page import="general.Organizacion" %>



<div class="fieldcontain ${hasErrors(bean: organizacionInstance, field: 'codigo', 'error')} required">
	<label for="codigo">
		<g:message code="organizacion.codigo.label" default="Codigo" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="codigo" maxlength="6" required="" value="${organizacionInstance?.codigo}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: organizacionInstance, field: 'nombre', 'error')} required">
	<label for="nombre">
		<g:message code="organizacion.nombre.label" default="Nombre" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nombre" maxlength="64" required="" value="${organizacionInstance?.nombre}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: organizacionInstance, field: 'nombreCompleto', 'error')} required">
	<label for="nombreCompleto">
		<g:message code="organizacion.nombreCompleto.label" default="Nombre Completo" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="nombreCompleto" maxlength="128" required="" value="${organizacionInstance?.nombreCompleto}"/>
</div>

