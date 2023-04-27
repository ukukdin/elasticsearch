<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<script src="/content/js/common/common.js" ></script>
<script>
	var gIS_PRELOADER_ACTIVATED  = false;
</script>
    <!-- Left Panel -->
    
    <aside id="left-panel" class="left-panel">
        <nav class="navbar navbar-expand-sm navbar-default">

            <div class="navbar-header">
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#main-menu" aria-controls="main-menu" aria-expanded="false" aria-label="Toggle navigation">
                    <i class="fa fa-bars"></i>
                </button>
                <a class="navbar-brand" href="./">Admin</a>
                <a class="navbar-brand hidden" href="./">M</a>
                <!-- <a class="navbar-brand hidden" href="./"><img src="images/logo2.png" alt="Logo"></a> -->
            </div>

            <div id="main-menu" class="main-menu collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <h3 class="menu-title">Monitoring</h3> 
                    <li class="menu-item-has-children dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="menu-icon fa fa-dashboard"></i>Dashboard</a>
                        <ul class="sub-menu children dropdown-menu">
                            <li><i class="fa fa-dashboard"></i><a href="/monitoring/Dashboard/dashboard">Dashboard</a></li>
                            <li><i class="fa fa-cloud"></i><a href="/monitoring/hazelcast/cacheList">Hazelcast IMDG</a></li>
                        </ul>
                    </li>

                    <li>
                        <a href="/jsptest"> <i class="menu-icon fa fa-filter"></i>탐지 룰 정보</a>
                    </li>
                    
                    <h3 class="menu-title">관리자</h3> 
                    <li class="menu-item-has-children dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><i class="menu-icon fa fa-dashboard"></i>설정</a>
                        <ul class="sub-menu children dropdown-menu">
                            <li><i class="fa fa-dashboard"></i><a href="tables-basic.html">사용자관리</a></li>
                            <li><i class="fa fa-cloud"></i><a href="/setting/userGroup/userGroupManagement">권한그룹관리</a></li>
                            <li><i class="fa fa-cloud"></i><a href="/monitoring/hazelcast/cacheList">메뉴관리</a></li>
                        </ul>
                    </li>
                    <li>
                        <a href="/scraping/codedataList/codedata_list"> <i class="menu-icon fa fa-filter"></i>코드관리</a>
                    </li>

                    
                    <h3 class="menu-title">UI template</h3><!-- /.menu-title -->
                    <li>
                        <a target="_blank" href="/content/theme/sufee-master/index.html"> <i class="menu-icon fa fa-filter"></i>Admin Template Main</a>
                    </li>
                </ul>
            </div><!-- /.navbar-collapse -->
        </nav>
    </aside>
    <!-- /#left-panel -->

    <!-- Left Panel -->
    
    <!-- Right Panel -->

    <div id="right-panel" class="right-panel">
    
    
    
    