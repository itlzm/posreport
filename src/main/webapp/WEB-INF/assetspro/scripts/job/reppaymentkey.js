var baseUrl = $("#baseUrl").val();
var funcID = "R0403";
var pageIndex = 1;
var pageSzie = 15;

var myChart2;
var option1;

// Step:3 conifg ECharts's path, link to echarts.js from current page.
// Step:3 为模块加载器配置echarts的路径，从当前页面链接到echarts.js，定义所需图表路径
require.config({
	paths : {
		echarts : baseUrl + '/assets/global/plugins/echarts-2.2.7/js'
	}
});

// Step:4 require echarts and use it in the callback.
// Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
require([ 'echarts', 'echarts/chart/bar', 'echarts/chart/line',
		'echarts/chart/pie', 'echarts/chart/funnel' ], function(ec) {
	myChart2 = ec.init(document.getElementById('chart-2'));

});


// 显示查询窗口
function fnBtnClickShowQuery() {

	if ($("#QueryDiv").css("display") == 'none') {
		$("#QueryDiv").css("display", "inline");
	} else {
		$("#QueryDiv").css("display", "none");
	}

}

// 查询按钮注册
function fnListeningActQuery() {
	$(document).ready(function() {
		$('#ActQuery').click(function() {
			// 开启编辑控件可用
			fnBtnClickShowQuery();
		});
	});
}

// 执行查询并关闭查询窗口
var setPaymentTypeName = '';
function fnBtnClickSetQuery() {
	$("#QueryDiv").css("display", "none");
	pageIndex = 1;

	var paymentTypeName = $("#paymentTypeName").val();
	//获取下拉选中的值
	setPaymentTypeName = $("#setPaymentTypeName").select2("val");
	if(setPaymentTypeName == 'ALL'){
		setPaymentTypeName = '';
	}
	fnQueryData(setPaymentTypeName);

}


// 查询数据
function fnQueryData(setPaymentTypeName) {
	fnLoadingShowAndClose(true);
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/job/queryPaymentKey',
		data : {
			PaymentTypeName : setPaymentTypeName,
			PageIndex : pageIndex,
			PageSize : pageSzie
		},
		success : function(data) {
			//alert(data);
			// 第一张图形化
			var dataTXHOne = data.dataTXHOne;
			fnDataTXHOne(dataTXHOne);

			// 明细数据信息
			var listData = data.listData;
			// 分页信息;
			var pageData = data.pageData;

			// 合计行
			var listDataSumTemp = data.listDataSum;

			if (pageIndex == 1) {
				listDataSum = listDataSumTemp;
			}

			fnListTableData(listData, pageData, listDataSum);

			// alert(myChart1.getDataURL("png"));
			fnLoadingShowAndClose(false);
		},
		error : function(e) {
			fnLoadingShowAndClose(false);
		}
	});

}
//第一个报表图形化
function fnDataTXHOne(dataTXHOne) {

	if (dataTXHOne.length == 0) {
		myChart2.clear();
		myChart2.setOption(option1);
		return;
	}
	for (var i = 0; i < len; i++) {
		if (i == 0) {
			for (obj in dataTXHOne[i]) {
				strPaymentTypeName += "'" + obj + "'" + ",";
			}
		}
		break;
	}
	// 获取步骤和步骤次数
	var len = dataTXHOne.length;
	var strPaymentTypeName = "";
	var strNum = "";
	//value为json块中每个json对象，paymentTypeName和Num为json对象中每个值的key
	$.each(dataTXHOne, function(key,value) {		
		strPaymentTypeName += "'" + value.paymentTypeName + "',";
		strNum += "" + value.Num + ",";
	});
			
	if (strPaymentTypeName != "") {
		strPaymentTypeName = strPaymentTypeName.substr(0,
				strPaymentTypeName.length - 1);
	}

	if (strNum != "") {
		strNum = strNum.substr(0,
				strNum.length - 1);
	}
	
	strPaymentTypeName = "[" + strPaymentTypeName + "]";
	strNum = "[" + strNum + "]";
//	console.log(strPaymentTypeName);
//	console.log(strNum);
	var option1Data = {
		title : {
			text : '',
			subtext : ''
		},
		tooltip : {
			trigger : 'axis'
		},
		legend : {
			data : [ '数量' ]
		},
		toolbox : {
			show : false,
			feature : {
				mark : {
					show : true
				},
				dataView : {
					show : true,
					readOnly : false
				},
				magicType : {
					show : true,
					type : [ 'line', 'bar' ]
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		calculable : true,
		xAxis : [ {
			type : 'category',
			data : eval(strPaymentTypeName)
			
		} ],
		yAxis : [ {
			type : 'value'
		} ],
		series : [

		{
			name : '数量',
			type : 'bar',
			data : eval(strNum),
			markPoint : {
				data : [ {
					type : 'max',
					name : '最大值'
				}, {
					type : 'min',
					name : '最小值'
				} ]
			},
			markLine : {
				data : [ {
					type : 'average',
					name : '平均值'
				} ]
			}
		} ]
	};

	myChart2.showLoading({
		text : '正在读取数据中...', // loading话术
	});
	myChart2.hideLoading();
	myChart2.clear();
	myChart2.setOption(option1Data);

}

function fnListTableData(listData, pageData, listDataSum) {
	// 绑定明细列表信息
	//var len = listDataSum.length;
	var strHtml = "";
	var strColName = "";
	var strColNameValue = "";
	//表格标题
	//for (var i = 0; i < len; i++) {
	for (var i = 0; i < listData.length; i++) {
		if (i == 0) {
			strColName += "<thead><tr>";
			strColName += "<th scope='col' style=\"text-align: center;\">行号 </th>";

			for (obj in listData[i]) {
				//支付方式编码 支付方式名称 支付方式分类编码 支付方式分类名称 
				if (obj == "支付方式编码") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else if (obj == "支付方式名称") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else {
					strColName += "<th scope='col' style=\"text-align: center;\">"
							+ obj + "</th>";
				}
			}
			strColName += "</tr></thead>";
			break;
		}
	}
	strColNameValue += "<tbody>";
	//表格详细内容行
	for (var i = 0; i < listData.length; i++) {
		strColNameValue += "<tr>";
		strColNameValue += "<td>" + ((pageIndex - 1) * pageSzie + i + 1)
				+ "</td>"; // 明细行号列
		$.each(listData[i], function(key, value) {
			//支付方式编码 支付方式名称 支付方式分类编码 支付方式分类名称
			strColNameValue += "<td align='center'>" + value + "</td>";
		});
		strColNameValue += "</tr>";
	}

	/*二维表格底部合计栏信息
	for (var i = 0; i < listDataSum.length; i++) {
		strColNameValue += "<tr>";
		strColNameValue += "<td></td>"; // 明细行号列
		$.each(listDataSum[i], function(key, value) {

			if (isfloat(value)) {

				strColNameValue += "<td align='right'>" + value + "</td>";
			} else {
				strColNameValue += "<td>" + value + "</td>";
			}

		});
		strColNameValue += "</tr>";
	}
	*/
	strColNameValue += "</tbody>";

	strHtml += strColName;
	strHtml += strColNameValue;
	document.getElementById('listTableData').innerHTML = "";
	$('#listTableData').append(strHtml);

	var strPageHtml = "";
	strPageHtml += "<ul class=\"pagination pull-right\" id=\"dataPager\">";
	strPageHtml += "</ul>";
	document.getElementById('dataPagerDiv').innerHTML = "";
	$('#dataPagerDiv').append(strPageHtml);

	// 绑定分页信息;
	$('#dataPager').bootpag({
		paginationClass : 'pagination pagination-sm',
		next : '<i class="fa fa-angle-right"></i>',
		prev : '<i class="fa fa-angle-left"></i>',
		total : parseInt(pageData[0].totalPages),
		page : pageIndex,
		maxVisible : 6,
		leaps : false,
		firstLastUse : false,
		first : '<i class="fa fa-angle-double-left"></i>',
		last : '<i class="fa fa-angle-double-right"></i>'
	}).on('page', function(event, num) {
		pageIndex = num;
		
		
		fnQueryData(setPaymentTypeName);
	});

	$("#pagerContent").html(
			"共[" + pageData[0].totalRows + "]条记录 当前第[" + pageIndex + "]页/共["
					+ pageData[0].totalPages + "]页");
}

function fnInitForm() {
	
	// 获取所有支付方式名称，线下支付，线上支付，线下支付方式
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/paymentTypeName',
		dataType : "json",
		async : false,
		success : function(data) {
			var strPaymentTypeName = JSON.stringify(eval(data.paymentTypeName));
			strPaymentTypeName = strPaymentTypeName.substring(1, strPaymentTypeName.length);
			var strPaymentkeyTemp = '[{"id":"ALL","text":"全部支付方式"},'
					+ strPaymentTypeName;

			$("#setPaymentTypeName").select2({
				data : eval(strPaymentkeyTemp)
			});
			$("#setPaymentTypeName").select2("val", "ALL");
		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

}

// 查询重置按钮点击执行方法
function fnClickBtnReset() {
	$("#paymentTypeName").val("");
	fnInitForm();
}

var reppaymentkey = function() {

	return {
		init : function() {
			fnInitForm();
			fnGetMenuFunction(baseUrl, funcID);
			fnListeningActQuery();
			setTimeout(function() {
				fnQueryData("");
			}, 1000);
			
		}
	};

}();
