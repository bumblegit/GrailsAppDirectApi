<%@ page import="grailsappdirect.Login" %>



<div class="fieldcontain ${hasErrors(bean: loginInstance, field: 'accountIdentifier', 'error')} required">
	<label for="accountIdentifier">
		<g:message code="login.accountIdentifier.label" default="Account Identifier" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="accountIdentifier" required="" value="${loginInstance?.accountIdentifier}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: loginInstance, field: 'openId', 'error')} required">
	<label for="openId">
		<g:message code="login.openId.label" default="Open Id" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="openId" required="" value="${loginInstance?.openId}"/>

</div>

