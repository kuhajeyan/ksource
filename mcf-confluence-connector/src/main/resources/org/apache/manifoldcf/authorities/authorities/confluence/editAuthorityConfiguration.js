<script type="text/javascript">
<!--
function checkConfig()
{
	
	if (editconnection.confport.value != "" && !isInteger(editconnection.confport.value))
	{
	    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfPortMustBeAnInteger'))");
	    editconnection.confport.focus();
	    return false;
	}
	
	if (editconnection.confhost.value != "" && editconnection.confhost.value.indexOf("/") != -1)
	{
	    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfHostMustNotIncludeSlash'))");
	    editconnection.confhost.focus();
	    return false;
	}
	
	
	
	/*
	  if (editconnection.clientid.value == "") {
	  alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfClientIdShouldNotBeEmpty'))");
	  editconnection.clientid.focus(); return false; }
	  
	  if (editconnection.clientsecret.value == "") {
	  alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfClientSecretShouldNotBeEmpty'))");
	  editconnection.clientsecret.focus(); return false; }
	 */
	
	return true;
}


function checkConfigForSave()
{
	  if (editconnection.confhost.value == "")
	  {
	    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfHostMustNotBeNull'))");
	    SelectTab("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.Server'))");
	    editconnection.confhost.focus();
	    return false;
	  }
	  
	  if (editconnection.confport.value == "")
	  {
	    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfPortMustNotBeNull'))");
	    SelectTab("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.Server'))");
	    editconnection.confhost.focus();
	    return false;
	  }
	  
	  if (editconnection.confhost.value != "" && editconnection.confhost.value.indexOf("/") != -1)
	  {
	    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfHostMustNotIncludeSlash'))");
	    SelectTab("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.Server'))");
	    editconnection.confhost.focus();
	    return false;
	  }

	  if (editconnection.confport.value != "" && !isInteger(editconnection.confport.value))
	  {
	    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfPortMustBeAnInteger'))");
	    SelectTab("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.Server'))");
	    editconnection.confport.focus();
	    return false;
	  }
	  
	  if (editconnection.confsoapapipath.value == "")
	  {
	    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfApiPathShouldNotBeEmpty'))");
	    SelectTab("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.Server'))");
	    editconnection.confsoapapipath.focus();
	    return false;
	  }
	  
	  if (editconnection.confsoapapipath.value.indexOf("/") != 0)
	  {
	    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfApiPathShouldStartWithSlash'))");
	    SelectTab("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.Server'))");
	    editconnection.confsoapapipath.focus();
	    return false;
	  }
	  
	  
	  
	  
	  	if (editconnection.clientid.value == "")
		{
		    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfClientIdShouldNotBeEmpty'))");
		    editconnection.clientid.focus();
		    return false;
		}
		
		if (editconnection.clientsecret.value == "")
		{
		    alert("$Encoder.bodyJavascriptEscape($ResourceBundle.getString('ConfAuthorityConnector.ConfClientSecretShouldNotBeEmpty'))");
		    editconnection.clientsecret.focus();
		    return false;
		}
		
		return true;
	
}
// -->
</script>