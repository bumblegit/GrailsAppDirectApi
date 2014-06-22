<!-- #section:basics/navbar.layout -->
<div id="navbar" class="navbar navbar-default">
<script type="text/javascript">
    try{ace.settings.check('navbar' , 'fixed')}catch(e){}
</script>

<div class="navbar-container" id="navbar-container">
<!-- #section:basics/sidebar.mobile.toggle -->
<button type="button" class="navbar-toggle menu-toggler pull-left" id="menu-toggler">
    <span class="sr-only">Toggle sidebar</span>

    <span class="icon-bar"></span>

    <span class="icon-bar"></span>

    <span class="icon-bar"></span>
</button>

<!-- /section:basics/sidebar.mobile.toggle -->
<div class="navbar-header pull-left">
    <!-- #section:basics/navbar.layout.brand -->
    <a href="#" class="navbar-brand">
        <small>
            <i class="fa fa-leaf"></i>
            GPlanningBoard
        </small>
    </a>

    <!-- /section:basics/navbar.layout.brand -->

    <!-- #section:basics/navbar.toggle -->

    <!-- /section:basics/navbar.toggle -->
</div>

<!-- #section:basics/navbar.dropdown -->
<div class="navbar-buttons navbar-header pull-right" role="navigation">
    <ul class="nav ace-nav">

        <li class="purple">
            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                <i class="ace-icon fa fa-bell icon-animated-bell"></i>
                <span class="badge badge-important">8</span>
            </a>

            <ul class="dropdown-menu-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close">
                <li class="dropdown-header">
                    <i class="ace-icon fa fa-exclamation-triangle"></i>
                    8 Notifications
                </li>

                <li>
                    <a href="#">
                        <div class="clearfix">
                            <span class="pull-left">
                                <i class="btn btn-xs no-hover btn-pink fa fa-comment"></i>
                                New Comments
                            </span>
                            <span class="pull-right badge badge-info">+12</span>
                        </div>
                    </a>
                </li>

                <li>
                    <a href="#">
                        <i class="btn btn-xs btn-primary fa fa-user"></i>
                        Bob just signed up as an editor ...
                    </a>
                </li>

                <li>
                    <a href="#">
                        <div class="clearfix">
                            <span class="pull-left">
                                <i class="btn btn-xs no-hover btn-success fa fa-shopping-cart"></i>
                                New Orders
                            </span>
                            <span class="pull-right badge badge-success">+8</span>
                        </div>
                    </a>
                </li>

                <li>
                    <a href="#">
                        <div class="clearfix">
                            <span class="pull-left">
                                <i class="btn btn-xs no-hover btn-info fa fa-twitter"></i>
                                Followers
                            </span>
                            <span class="pull-right badge badge-info">+11</span>
                        </div>
                    </a>
                </li>

                <li class="dropdown-footer">
                    <a href="#">
                        See all notifications
                        <i class="ace-icon fa fa-arrow-right"></i>
                    </a>
                </li>
            </ul>
        </li>

        <li class="purple">
            <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                <i class="ace-icon fa fa-bell icon-animated-bell"></i>
                <span class="badge badge-important">8</span>
            </a>

            <ul class="dropdown-menu-right dropdown-navbar navbar-pink dropdown-menu dropdown-caret dropdown-close">
                <li class="dropdown-header">
                    <i class="ace-icon fa fa-exclamation-triangle"></i>
                    8 Notifications
                </li>

                <li>
                    <a href="#">
                        <div class="clearfix">
                            <span class="pull-left">
                                <i class="btn btn-xs no-hover btn-pink fa fa-comment"></i>
                                New Comments
                            </span>
                            <span class="pull-right badge badge-info">+12</span>
                        </div>
                    </a>
                </li>

                <li>
                    <a href="#">
                        <i class="btn btn-xs btn-primary fa fa-user"></i>
                        Bob just signed up as an editor ...
                    </a>
                </li>

                <li>
                    <a href="#">
                        <div class="clearfix">
                            <span class="pull-left">
                                <i class="btn btn-xs no-hover btn-success fa fa-shopping-cart"></i>
                                New Orders
                            </span>
                            <span class="pull-right badge badge-success">+8</span>
                        </div>
                    </a>
                </li>

                <li>
                    <a href="#">
                        <div class="clearfix">
                            <span class="pull-left">
                                <i class="btn btn-xs no-hover btn-info fa fa-twitter"></i>
                                Followers
                            </span>
                            <span class="pull-right badge badge-info">+11</span>
                        </div>
                    </a>
                </li>

                <li class="dropdown-footer">
                    <a href="#">
                        See all notifications
                        <i class="ace-icon fa fa-arrow-right"></i>
                    </a>
                </li>
            </ul>
        </li>
        <!-- #section:basics/navbar.user_menu -->
        <li class="light-blue">
            <a data-toggle="dropdown" href="#" class="dropdown-toggle">
                <img class="nav-user-photo" src="../avatars/user.jpg" alt="Jason's Photo" />
                <span class="user-info">
                    <small>Welcome,</small>
                    Jason
                </span>

                <i class="ace-icon fa fa-caret-down"></i>
            </a>

            <ul class="user-menu dropdown-menu-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                <li>
                    <a href="#"><strong>OpenID:</strong> <br/><openid:identifier/></a>
                </li>
                <li>
                    <openid:logoutLink success="[controller:'login', action: '/index']"><strong>Logout</strong></openid:logoutLink>

                </li>
            </ul>
        </li>

        <!-- /section:basics/navbar.user_menu -->
    </ul>
</div>

<!-- /section:basics/navbar.dropdown -->
</div><!-- /.navbar-container -->
</div>

<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
<script type="text/javascript">
    try{ace.settings.check('main-container' , 'fixed')}catch(e){}
</script>

<!-- #section:basics/sidebar -->
<div id="sidebar" class="sidebar                  responsive">
<script type="text/javascript">
    try{ace.settings.check('sidebar' , 'fixed')}catch(e){}
</script>

<ul class="nav nav-list">
<li class="active">
    <a href="index.html">
        <i class="menu-icon fa fa-tachometer"></i>
        <span class="menu-text"> Dashboard </span>
    </a>

    <b class="arrow"></b>
</li>

<li class="">
    <a href="#" class="dropdown-toggle">
        <i class="menu-icon fa fa-desktop"></i>
        <span class="menu-text"> UI &amp; Elements </span>

        <b class="arrow fa fa-angle-down"></b>
    </a>

    <b class="arrow"></b>

    <ul class="submenu">
        <li class="">
            <a href="#" class="dropdown-toggle">
                <i class="menu-icon fa fa-caret-right"></i>

                Layouts
                <b class="arrow fa fa-angle-down"></b>
            </a>

            <b class="arrow"></b>

            <ul class="submenu">
                <li class="">
                    <a href="top-menu.html">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Top Menu
                    </a>

                    <b class="arrow"></b>
                </li>

                <li class="">
                    <a href="mobile-menu-1.html">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Default Mobile Menu
                    </a>

                    <b class="arrow"></b>
                </li>

                <li class="">
                    <a href="mobile-menu-2.html">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Mobile Menu 2
                    </a>

                    <b class="arrow"></b>
                </li>

                <li class="">
                    <a href="mobile-menu-3.html">
                        <i class="menu-icon fa fa-caret-right"></i>
                        Mobile Menu 3
                    </a>

                    <b class="arrow"></b>
                </li>
            </ul>
        </li>

        <li class="">
            <a href="typography.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Typography
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="elements.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Elements
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="buttons.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Buttons &amp; Icons
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="treeview.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Treeview
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="jquery-ui.html">
                <i class="menu-icon fa fa-caret-right"></i>
                jQuery UI
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="nestable-list.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Nestable Lists
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="#" class="dropdown-toggle">
                <i class="menu-icon fa fa-caret-right"></i>

                Three Level Menu
                <b class="arrow fa fa-angle-down"></b>
            </a>

            <b class="arrow"></b>

            <ul class="submenu">
                <li class="">
                    <a href="#">
                        <i class="menu-icon fa fa-leaf"></i>
                        Item #1
                    </a>

                    <b class="arrow"></b>
                </li>

                <li class="">
                    <a href="#" class="dropdown-toggle">
                        <i class="menu-icon fa fa-pencil"></i>

                        4th level
                        <b class="arrow fa fa-angle-down"></b>
                    </a>

                    <b class="arrow"></b>

                    <ul class="submenu">
                        <li class="">
                            <a href="#">
                                <i class="menu-icon fa fa-plus"></i>
                                Add Product
                            </a>

                            <b class="arrow"></b>
                        </li>

                        <li class="">
                            <a href="#">
                                <i class="menu-icon fa fa-eye"></i>
                                View Products
                            </a>

                            <b class="arrow"></b>
                        </li>
                    </ul>
                </li>
            </ul>
        </li>
    </ul>
</li>

<li class="">
    <a href="#" class="dropdown-toggle">
        <i class="menu-icon fa fa-list"></i>
        <span class="menu-text"> Tables </span>

        <b class="arrow fa fa-angle-down"></b>
    </a>

    <b class="arrow"></b>

    <ul class="submenu">
        <li class="">
            <a href="tables.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Simple &amp; Dynamic
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="jqgrid.html">
                <i class="menu-icon fa fa-caret-right"></i>
                jqGrid plugin
            </a>

            <b class="arrow"></b>
        </li>
    </ul>
</li>

<li class="">
    <a href="#" class="dropdown-toggle">
        <i class="menu-icon fa fa-pencil-square-o"></i>
        <span class="menu-text"> Forms </span>

        <b class="arrow fa fa-angle-down"></b>
    </a>

    <b class="arrow"></b>

    <ul class="submenu">
        <li class="">
            <a href="form-elements.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Form Elements
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="form-wizard.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Wizard &amp; Validation
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="wysiwyg.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Wysiwyg &amp; Markdown
            </a>

            <b class="arrow"></b>
        </li>

        <li class="">
            <a href="dropzone.html">
                <i class="menu-icon fa fa-caret-right"></i>
                Dropzone File Upload
            </a>

            <b class="arrow"></b>
        </li>
    </ul>
</li>

<li class="">
    <a href="widgets.html">
        <i class="menu-icon fa fa-list-alt"></i>
        <span class="menu-text"> Widgets </span>
    </a>

    <b class="arrow"></b>
</li>

<li class="">
    <a href="calendar.html">
        <i class="menu-icon fa fa-calendar"></i>

        <span class="menu-text">
            Calendar

            <!-- #section:basics/sidebar.layout.badge -->
            <span class="badge badge-transparent tooltip-error" title="2 Important Events">
                <i class="ace-icon fa fa-exclamation-triangle red bigger-130"></i>
            </span>

            <!-- /section:basics/sidebar.layout.badge -->
        </span>
    </a>

    <b class="arrow"></b>
</li>

</ul><!-- /.nav-list -->

<!-- #section:basics/sidebar.layout.minimize -->
<div class="sidebar-toggle sidebar-collapse" id="sidebar-collapse">
    <i class="ace-icon fa fa-angle-double-left" data-icon1="ace-icon fa fa-angle-double-left" data-icon2="ace-icon fa fa-angle-double-right"></i>
</div>

<!-- /section:basics/sidebar.layout.minimize -->
<script type="text/javascript">
    try{ace.settings.check('sidebar' , 'collapsed')}catch(e){}
</script>
</div>

<!-- /section:basics/sidebar -->
<div class="main-content">
    <!-- #section:basics/content.breadcrumbs -->
    <div class="breadcrumbs" id="breadcrumbs">
        <script type="text/javascript">
            try{ace.settings.check('breadcrumbs' , 'fixed')}catch(e){}
        </script>

        <ul class="breadcrumb">
            <li>
                <i class="ace-icon fa fa-home home-icon"></i>
                <a href="#">Home</a>
            </li>
            <li class="active">Dashboard</li>
        </ul><!-- /.breadcrumb -->

    <!-- /section:basics/content.searchbox -->
    </div>

    <!-- /section:basics/content.breadcrumbs -->
    <div class="page-content">

        <!-- /section:settings.box -->
        <div class="page-header">
            <h1>
                Dashboard
                <small>
                    <i class="ace-icon fa fa-angle-double-right"></i>
                    overview &amp; stats
                </small>
            </h1>
        </div><!-- /.page-header -->

        <div class="row">
            <div class="col-xs-12">
                <!-- PAGE CONTENT BEGINS -->
                Contenido!
                <!-- PAGE CONTENT ENDS -->
            </div><!-- /.col -->
        </div><!-- /.row -->
    </div><!-- /.page-content -->
</div><!-- /.main-content -->

<div class="footer">
    <div class="footer-inner">
        <!-- #section:basics/footer -->
        <div class="footer-content">
            <span class="bigger-120">
                <span class="blue bolder">Ace</span>
                Application &copy; 2013-2014
            </span>

            &nbsp; &nbsp;
            <span class="action-buttons">
                <a href="#">
                    <i class="ace-icon fa fa-twitter-square light-blue bigger-150"></i>
                </a>

                <a href="#">
                    <i class="ace-icon fa fa-facebook-square text-primary bigger-150"></i>
                </a>

                <a href="#">
                    <i class="ace-icon fa fa-rss-square orange bigger-150"></i>
                </a>
            </span>
        </div>

        <!-- /section:basics/footer -->
    </div>
</div>

<a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
    <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
</a>
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='../js/jquery.min.js'>"+"<"+"/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
 window.jQuery || document.write("<script src='../js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='../js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
</script>
<script src="../js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->

<!--[if lte IE 8]>
		  <script src="../js/excanvas.min.js"></script>
		<![endif]-->
<script src="../js/jquery-ui.custom.min.js"></script>

<!-- ace scripts -->
<script src="../js/ace-elements.min.js"></script>
<script src="../js/ace.min.js"></script>