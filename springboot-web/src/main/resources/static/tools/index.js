$(function () {
    function parse_json(str) {
        try {
            return JSON.stringify(JSON.parse(str), null, '        ');
        } catch (e) {
            return str;
        }
    }
    output_want = '';
    $("form").submit(function () {
        var form = $(this);
        var inputs = form.serializeArray();
        output_want = '';//params['output_want'];

        $.ajax({
            url: form.attr('action'),
            type: 'GET',
            data: inputs
        }).done(function (data, status, xhr) {

            var headers = xhr.getAllResponseHeaders();
            var html = 'HTTP/1.1 ' + xhr.status + ' ' + xhr.statusText + "\r\n"
                + headers + "\r\n\r\n" + parse_json(xhr.responseText);
            output = xhr.responseText;

            $success = "";
            if (output_want.length != 0) {
                outputWantArr = output_want.split(',');
                for (var i in outputWantArr) {
                    if (output.indexOf(outputWantArr[i]) == -1) {
                        $success =outputWantArr[i];
                        break;
                    }
                }
            }
            if ($success .length===0) {
                form.find(".result").css('background-color', 'white');
                form.find(".result").html(html).show();
                if (form.find(".showtable")[0]) {
                    form.find(".showtable table").remove();
                    buildHtmlTable(form.find(".showtable"), JSON.parse(xhr.responseText).data);
                }
            } else {
                form.find(".result").css('background-color', 'red');
                html ="Result Miss: "+$success+"\n\n"+html;
                form.find(".result").html(html).show();
            }

        }).fail(function (xhr, status, error) {
            var headers = xhr.getAllResponseHeaders();
            var html = 'HTTP/1.1 ' + xhr.status + ' ' + xhr.statusText + "\r\n"
                + headers + "\r\n\r\n" + parse_json(xhr.responseText);
            form.find(".result").html(html).show();
        });
        form.find(".result").html("加载中...");
        return false;
    });
});
