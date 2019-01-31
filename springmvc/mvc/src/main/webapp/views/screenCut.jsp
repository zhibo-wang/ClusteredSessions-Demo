<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/1/30
  Time: 22:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <title>测试多方位截图</title>
    <link rel="stylesheet" href="/static/script/css/dragresise.css">
    <style>
        body {
            background: #e6f0f2;
        }

        .myContainer {
            position: absolute;
            background: none;
        }

        .control button {
            margin: 0 10px;
        }
    </style>
    <script src="/static/script/js/jquery-1.7.2.js?r=version"></script>
    <script src="/static/script/js/dragresize.js?r=version"></script>
    <script src="/static/script/js/dragBox.js?r=version"></script>
</head>

<body>

<div id="dragWrap" class="part" style="position: relative; width: 700px; height: 650px; display: inline-block">
    <img id="sourceImg" src="/static/images/123.png" width="684" height="648" alt=""
         style="top: 0; left: 0; position: absolute">
    <div class="myContainer" id="selectImage" style="display: none; width: 700px; height: 650px">
        <button>选择</button>
    </div>
</div>

<div style="text-align: center; display: inline-block" class="control">
    <button id="cutImage">截取</button>
    <button id="recogniteImage">识别</button>
</div>
<div id="page-content"></div>
<script src="/static/script/js/myControl.js"></script>
</body>
</html>