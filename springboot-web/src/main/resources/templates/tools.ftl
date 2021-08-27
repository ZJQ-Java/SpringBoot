<!doctype html>
<html>
<head>
    <meta charset='utf8'>
    <title>User Center Test Suites</title>
    <style>
        body {
            font-family: Verdana, Arial, "Microsoft YaHei", 微软雅黑, "MicrosoftJhengHei", 华文细黑, STHeiti, MingLiu, monospace;
            font-size: 12px;
        }

        table.dataintable {
            margin-top: 10px;
            border-collapse: collapse;
            border: 1px solid #aaa;
            max-width: 90%;
        }

        table.dataintable th {
            vertical-align: baseline;
            padding: 5px 5px 5px 6px;
            background-color: #d5d5d5;
            border: 1px solid #aaa;
            text-align: left;
        }

        table.dataintable td {
            vertical-align: text-top;
            padding: 6px 5px 6px 6px;
            background-color: #efefef;
            border: 1px solid #aaa;
        }

        table.dataintable td a {
            padding: 0px;
        }

        table.dataintable pre {
            width: auto;
            margin: 0;
            padding: 0;
            border: 0;
            background-color: transparent;
        }

        .dataintable > tbody > tr:hover > td,
        .table-hover > tbody > tr:hover > th {
            background-color: #CBCBCB;
        }

        .line-through {
            text-decoration: line-through
        }

        a {
            text-decoration: none;
            color: blue;
        }

        h2 {
            color: #555;
        }

        h3, h4, h5, h6 {
            color: #777;
        }

        h2, h3, h4 {
            width: 97%;
            display: inline-block;
            border-bottom: solid 1px #ccc;
        }

        form li, form ul {
            list-style: none;
            margin: 0;
            padding: 0;
            white-space: pre-wrap;
            line-height: 0px;
        }

        form ul li i {
            font-size: 10px;
        }

        form li label {
            text-align: right;
            width: 200px;
            display: inline-block;
            padding: 0;
            margin: 0;
        }

        form li input {
            width: 20em;
            margin-left: 1em;
        }

        form li.caseList {
            float: left;
            width: 100%
        }

        form li.case label {
            text-align: right;
            width: 70px;
            display: inline-block;
            padding: 0;
            margin: 0;
        }

        .tools {
            width: 30px;
            height: 30px;
            float: left;
            cursor: pointer;
        }

        form li textarea {
            width: 65%;
            margin-left: 1em;
        }

        form button {
            margin-left: 5em;
            width: 10em;
        }

        form span {
            color: black;
            padding-right: 0.5em;
        }

        form .result {
            margin-left: 0em;
            border: solid 1px #ccc;
            display: none;
            width: auto;
            max-width: 97%;
            white-space: pre-wrap;
        }

        form .url {
            display: inline-block;
            margin-left: 1em;
        }

        .module-list {
            display: inline-block;
            width: 20em;
        }

        .ml li a {
            color: blue;
        }

        .method_list a {
            padding: 1em;
        }

        .method_listTop {
            position: fixed;
            right: 10px;
            top: 10px;
            border: solid #ccc 1px;
            padding: 1em;
        }

        .method_listDown {
            position: fixed;
            right: 10px;
            bottom: 10px;
            border: solid #ccc 1px;
            padding: 1em;
        }

        .pad {
            width: 100%;
        }

        .leftPad {;
            float: left;
        }

        .rightPad {
            width: 70%;
            float: left;
        }

        .set_show_case {
            position: fixed;
            top: 10px;
            right: 1em;
            border: solid 1px #ccc;
            padding: 1em;
        }

        .red {
            color: red;
        }

        .yellow {
            color: #acac00;
        }

        .blue {
            color: #0000ff;
            font-weight: bold;
        }
    </style>
    <script src='/static/tools/jquery-1.8.3.js'></script>
    <script src='/static/tools/base64.js'></script>
    <script src='/static/tools/json2table.js'></script>
    <script src='/static/tools/index.js'></script>
</head>
<body>


<table class="dataintable method_list">
    <tbody>
    <tr>
        <th colspan="2">TOOLS</th>
    </tr>
    <#--    #foreach ($function in $functionList)-->
    <#list functionList as function>
        <tr>
            <td><a href="#${function.name}"><strong class="">${function.name}</strong></a></td>
            <td><a href="#${function.name}">${function.desc}</a></td>
        </tr>

    </#list>

    <#--    #end-->
    </tbody>
</table>


<#--#foreach ($function in $functionList)-->
<#list functionList as function>

    <a name="${function.name}"></a>
    <h3 class='red'>${function.name} </h3>
    <ul style="margin: 0; padding: 0 35px; color: red;">
        <li>${function.desc} URL: /inner/tools/${function.name}</li>
    </ul>
    <form action="/inner/tools/${function.name}" id="${function.name}" method='post' target='_blank'><h4>参数列表:</h4>
        <input type="hidden" name='key' value='${accessKey}'/>
        <ul>
            <#list function.params as param>
                <li class=''><label
                            class=''>
                        <#if param.isMust()>
                            <span class="red">*</span>
                        </#if>
                        (${param.type})${param.name}:
                    </label><input
                            type=text name='${param.name}' value=''
                            <#if param.isMust()>
                                required="required"
                            </#if>

                    >&nbsp;<i class=''> ${param.desc}</i></li>
            </#list >
        </ul>
        <button type='submit' id='openCurrent'>Submit</button>
        <button type='reset'
                onclick="$('#${function.name} .result').text('');$('#${function.name} .showtable').text('');">
            Clean
        </button>
        <br>
        <h4>返回结果</h4>
        <div class="showtable"></div>
        <div class='result'>返回结果</div>
    </form>
</#list>

</body>
</html>
