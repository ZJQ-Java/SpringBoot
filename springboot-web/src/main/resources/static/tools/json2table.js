/**
 var jsonArray = [
 { "name": "abc", "age": 50 },
 { "age": "25", "hobby": "swimming" },
 { "name": "xyz", "hobby": "programming" }
 ];
 */

// Builds the HTML Table out of jsonArray.
function buildHtmlTable(selector, jsonArray) {
    if (Array.isArray(jsonArray)){
        return _buildHtmlTable(selector, jsonArray)
    }
    if (typeof jsonArray == "object") {
        var table$ = $('<table class="showtable" border="1" bordercolor="#808080" cellpadding="2" cellspacing="0" style="font-size: 10pt; border-collapse:collapse; border:none" width="100%"/>')
        var row$ = $('<tr/>');
        for (var item in jsonArray) {
            row$.append($('<td/>').html(item));
            if (typeof jsonArray[item] == "object") {
                var aTd = $('<td/>');
                buildHtmlTable(aTd, jsonArray[item])
                row$.append(aTd)
            } else {
                row$.append($('<td/>').html(jsonArray[item]));
            }
        }
        table$.append(row$);
        return selector.append(table$);
    }

}

function _buildHtmlTable(selector, jsonArray) {
    var table$ = $('<table class="showtable" border="1" bordercolor="#808080" cellpadding="2" cellspacing="0" style="font-size: 10pt; border-collapse:collapse; border:none" width="100%"/>')
    if (typeof jsonArray[0] == "object") {
    	var columns = addAllColumnHeaders(jsonArray, table$);
        for (var i = 0; i < jsonArray.length; i++) {
            var row$ = $('<tr/>');
            for (var colIndex = 0; colIndex < columns.length; colIndex++) {
                var cellValue = jsonArray[i][columns[colIndex]];
                if (typeof cellValue == "object") {
                    var aTd = $('<td/>');
                    buildHtmlTable(aTd, [cellValue])
                    row$.append(aTd);
                } else {
                    if (cellValue == null) {
                        cellValue = "";
                    }
                    row$.append($('<td/>').html(cellValue));
                }

            }
            table$.append(row$);
        }
    } else {
    	var row$ = $('<tr/>');
    	for (var i = 0; i < jsonArray.length; i++) {
    		var cellValue = jsonArray[i];
    		if (cellValue == null) {
                cellValue = "";
            }
            row$.append($('<td/>').html(cellValue));
    	}
    	table$.append(row$);
    }
    selector.append(table$);
}

// Adds a header row to the table and returns the set of columns.
// Need to do union of keys from all records as some records may not contain
// all records.
function addAllColumnHeaders(jsonArray, selector) {
    var columnSet = [];
    var headerTr$ = $('<tr/>');


    for (var i = 0; i < jsonArray.length; i++) {
        var rowHash = jsonArray[i];
        for (var key in rowHash) {
            if ($.inArray(key, columnSet) == -1) {
                columnSet.push(key);
                headerTr$.append($('<th/>').html(key));
            }
        }
    }
    selector.append(headerTr$);
    return columnSet;
}