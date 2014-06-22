<%@ page import="grailsappdirect.Login" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>Login Page - Ace Admin</title>

    <meta name="description" content="User login page" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="css/bootstrap.min.css" />
    <link rel="stylesheet" href="css/font-awesome.min.css" />

    <!-- text fonts -->
    <link rel="stylesheet" href="css/ace-fonts.css" />

    <!-- ace styles -->
    <link rel="stylesheet" href="css/ace.min.css" />

    <!--[if lte IE 9]>
			<link rel="stylesheet" href="css/ace-part2.min.css" />
		<![endif]-->
    <link rel="stylesheet" href="css/ace-rtl.min.css" />

    <!--[if lte IE 9]>
		  <link rel="stylesheet" href="css/ace-ie.min.css" />
		<![endif]-->
    <link rel="stylesheet" href="css/ace.onpage-help.css" />

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->

    <!--[if lt IE 9]>
		<script src="js/html5shiv.js"></script>
		<script src="js/respond.min.js"></script>
		<![endif]-->
    <link rel="stylesheet" href="css/grails-app-direct.css" />
</head>

<body class="login-layout light-login">
<openid:hasLoginError>
    <div class="errors">
        <ul>
            <li><openid:renderLoginError /></li>
        </ul>
    </div>
</openid:hasLoginError>
<div class="main-container">
<div class="main-content">
<div class="row">
<div class="col-sm-10 col-sm-offset-1">
<div class="login-container">
<div class="center">
    <h1>
        <i class="ace-icon fa fa-leaf green"></i>
        <span class="red">Grails</span>
        <span class="white" id="id-text2">AppDirect Test</span>
    </h1>
</div>

<div class="space-6"></div>

<div class="position-relative">
    <div id="login-box" class="login-box visible widget-box no-border">
        <div class="widget-body">
            <div class="widget-main">
                <h4 class="header blue lighter bigger">
                    <i class="ace-icon fa fa-coffee green"></i>
                    Please Enter Your OpenID
                </h4>

                <div class="space-6"></div>

                <openid:form success="[controller:'login', action:'loggedIn']" error="[controller:'login', action:'notLogged']">
                    <fieldset>
                        <label class="block clearfix">
                            <span class="block input-icon input-icon-right">
                                <openid:input class="form-control" placeholder="OpenID" size="30" />
                                <i class="ace-icon fa fa-user"></i>
                            </span>
                        </label>

                        <g:if test="${errorOpenID}">
                            <div class="space"></div>
                            <h4 class="header red lighter bigger">
                                <i class="fa red"></i>
                                User Not Authorized
                            </h4>
                        </g:if>

                        <div class="space"></div>

                        <div class="clearfix">

                            <g:submitButton name="login" value="Login" class="width-35 pull-right btn btn-sm btn-primary">
                                <i class="ace-icon fa fa-key"></i>
                                <span class="bigger-110">Login</span>
                            </g:submitButton>
                        </div>

                        <div class="space-4"></div>
                    </fieldset>

                </openid:form>

            </div><!-- /.widget-main -->

            <div class="toolbar clearfix">
            </div>
        </div><!-- /.widget-body -->
    </div><!-- /.login-box -->

</div><!-- /.position-relative -->

</div>
</div><!-- /.col -->
</div><!-- /.row -->
</div><!-- /.main-content -->
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='js/jquery.min.js'>"+"<"+"/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
</script>

<!-- inline scripts related to this page -->
<script type="text/javascript">
    jQuery(function($) {
        $(document).on('click', '.toolbar a[data-target]', function(e) {
            e.preventDefault();
            var target = $(this).data('target');
            $('.widget-box.visible').removeClass('visible');//hide others
            $(target).addClass('visible');//show target
        });
    });



    //you don't need this, just used for changing background
    jQuery(function($) {
        $('#btn-login-dark').on('click', function(e) {
            $('body').attr('class', 'login-layout');
            $('#id-text2').attr('class', 'white');
            $('#id-company-text').attr('class', 'blue');

            e.preventDefault();
        });
        $('#btn-login-light').on('click', function(e) {
            $('body').attr('class', 'login-layout light-login');
            $('#id-text2').attr('class', 'grey');
            $('#id-company-text').attr('class', 'blue');

            e.preventDefault();
        });
        $('#btn-login-blur').on('click', function(e) {
            $('body').attr('class', 'login-layout blur-login');
            $('#id-text2').attr('class', 'white');
            $('#id-company-text').attr('class', 'light-blue');

            e.preventDefault();
        });

    });
</script>
</body>
</html>