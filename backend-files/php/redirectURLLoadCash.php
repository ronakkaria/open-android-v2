<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Redirect URL</title>
<script type="text/javascript">
	function postResponse(data) {
		document.write(data);
		  CitrusResponse.loadWalletResponse(data);
	}
		var url = window.location.href;
		var index = url.indexOf("#");
		if(index != -1){
			var queryString = url.substring(index + 1);				
			postResponse("#"+queryString);
		}			
</script>
</head>
<body>
</body>
</html>
