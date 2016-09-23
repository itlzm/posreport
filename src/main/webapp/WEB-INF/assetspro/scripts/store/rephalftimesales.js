var baseUrl = $("#baseUrl").val();
var funcID = "R0108";
var pageIndex = 1;
var pageSzie = 15;

var initdteTimeStart;
var initdteTimeEnd;

// 查询时有时间范围临时变量 规则 一个时间间隔控件对应 两个变量
var dteTimeStart; // 时间开始时间
var dteTimeEnd; // 时间结束时间

var myChart1;
var myChart2;

var option1;

// 查询按钮注册
function fnListeningActQuery() {
	$(document).ready(function() {
		$('#ActQuery').click(function() {
			// 开启编辑控件可用
			fnBtnClickShowQuery();
		});
	});
}

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
	myChart1 = ec.init(document.getElementById('chart-1'));
	option1 = {
		title : {
			text : '支付结构分析',
			subtext : '',
			x : 'center'
		},
		tooltip : {
			trigger : 'item',
			formatter : "{a} <br/>{b} : {c} ({d}%)"
		},
		legend : {
			orient : 'vertical',
			x : 'left',
			data : []
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
					type : [ 'pie', 'funnel' ]
				},
				restore : {
					show : true
				},
				saveAsImage : {
					show : true
				}
			}
		},
		calculable : false,
		series : [ {
			name : '支付方式',
			type : 'pie',
			selectedMode : 'single',
			radius : [ 0, 70 ],

			// for funnel
			x : '20%',
			width : '40%',
			funnelAlign : 'right',
			max : 1548,

			itemStyle : {
				normal : {
					label : {
						position : 'inner'
					},
					labelLine : {
						show : false
					}
				}
			},
			data : []
		}, {
			name : '支付方式',
			type : 'pie',
			radius : [ 100, 140 ],

			// for funnel
			x : '60%',
			width : '35%',
			funnelAlign : 'left',
			max : 1048,

			data : []
		} ]
	};

	/*
	 * myChart1.showLoading({ text : '正在读取数据中...', // loading话术 });
	 * myChart1.hideLoading(); myChart1.setOption(option1);
	 */ 
	myChart2 = ec.init(document.getElementById('chart-2'),"macarons");
	var option2 = {
		tooltip : {
			trigger : 'axis',
			axisPointer : {
				type : 'shadow'
			}
		},
		legend : {
			data : [ '好吃来CPOS', '微信餐厅堂食', '饿了么外卖', '百度外卖' ]
		},
		toolbox : {
			show : false,
			orient : 'vertical',
			y : 'center',
			feature : {
				mark : {
					show : true
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
			data : [ '订单金额', '折扣金额', '实付金额' ]
		} ],
		yAxis : [ {
			type : 'value',
			splitArea : {
				show : true
			},
			axisLabel : {
				formatter : '{value} 元'
			}
		} ],
		grid : {
			x2 : 40
		},
		series : [ {
			name : '好吃来CPOS',
			type : 'bar',
			// stack: '总量',
			data : [ 110710, 16606.5, 94103.5 ]
		}, {
			name : '微信餐厅堂食',
			type : 'bar',
			// stack: '总量',
			data : [ 105580, 15837, 89743 ]
		}, {
			name : '饿了么外卖',
			type : 'bar',
			// stack: '总量',
			data : [ 47062, 7059.3, 40002.7 ]
		}, {
			name : '百度外卖',
			type : 'bar',
			// stack: '总量',
			data : [ 79670, 11950.5, 67719.5 ]
		} ]
	};
	/*
	 * myChart2.showLoading({ text : '正在读取数据中...', // loading话术 });
	 * myChart2.hideLoading(); myChart2.setOption(option2);
	 */
	/*
	 * var myChart3 = ec.init(document.getElementById('chart-3')); var option3 = {
	 * tooltip : { trigger : 'axis', axisPointer : { // 坐标轴指示器，坐标轴触发有效 type :
	 * 'shadow' // 默认为直线，可选为：'line' | 'shadow' } }, legend : { data : [ '实际收入',
	 * '折扣金额', '订单金额' ] }, toolbox : { show : false, feature : { mark : { show :
	 * true }, dataView : { show : true, readOnly : false }, magicType : { show :
	 * true, type : [ 'line', 'bar' ] }, restore : { show : true }, saveAsImage : {
	 * show : true } } }, calculable : true, xAxis : [ { type : 'value',
	 * axisLabel : { formatter : '{value} 元' } } ], yAxis : [ { type :
	 * 'category', axisTick : { show : false }, data : [ '整单八五折', '满100减10',
	 * '满200减30' ] } ], series : [ { name : '实际收入', type : 'bar', itemStyle : {
	 * normal : { label : { show : true, position : 'inside' } } }, data : [
	 * 41366.95, 78221.25, 171980.5 ] }, { name : '订单金额', type : 'bar', stack :
	 * '总量', barWidth : 5, itemStyle : { normal : { label : { show : true } } },
	 * data : [ 48667, 92025, 202330 ] }, { name : '折扣金额', type : 'bar', stack :
	 * '总量', itemStyle : { normal : { label : { show : true, position : 'left' } } },
	 * data : [ -7300.05, -13803.75, -30349.5 ] } ] }; myChart3.showLoading({
	 * text : '正在读取数据中...', // loading话术 }); myChart3.hideLoading();
	 * myChart3.setOption(option3);
	 */
});

// 显示查询窗口
function fnBtnClickShowQuery() {

	if ($("#QueryDiv").css("display") == 'none') {
		$("#QueryDiv").css("display", "inline");
	} else {
		$("#QueryDiv").css("display", "none");
	}

}

// 执行查询并关闭查询窗口
function fnBtnClickSetQuery() {
	$("#QueryDiv").css("display", "none");
	pageIndex = 1;

	var BrandName = "";
	var StoreName = "";
	var ChannelName = "";

	fnQueryData(BrandName, StoreName, ChannelName);

}
var listDataSum;
// 查询数据
function fnQueryData(BrandCode, StoreCode, ChannelName) {
	fnLoadingShowAndClose(true);
	StoreCode = $("#StoreListVal").val();
	BrandCode = $("#BranCode").val();

	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/store/queryRepHalfTimeSales/',
		data : {
			TimeStart : dteTimeStart,
			TimeEnd : dteTimeEnd,
			BranCode : BrandCode,
			StoreCode : StoreCode,
			ChannelName : ChannelName,
			PageIndex : pageIndex,
			PageSize : pageSzie
		},
		success : function(data) {

			// 饼图
			var listTxData = data.listTxData;

			fnChannel1Data(listTxData, listTxData);
			fnChannel2Data(listTxData, listTxData);

			// 明细数据信息
			var listData = data.listData;
			// 分页信息;
			var pageData = data.pageData;

			var listDataSumTemp = data.listDataSum;

			if (pageIndex == 1) {
				listDataSum = listDataSumTemp;
			}

			fnListTableData(listData, pageData, listDataSum);
			fnLoadingShowAndClose(false);
		},
		error : function(e) {
			fnLoadingShowAndClose(false);
		}
	});

}

// 第一个报表图形化
function fnChannel1Data(channel1, channel2) {

	if (channel1.length == 0) {
		myChart1.clear();
		myChart1.setOption(option1);
		return;
	}

	var channelAry = "[";
	var channelAryData = "[";
	for (var i = 0; i < channel1.length; i++) {
		if (i != channel1.length - 1) {
			channelAry += "'" + channel1[i].bizHourTime + "',"
			channelAryData += "{value:" + channel1[i].tot_SubNetAmountPrice
					+ ",name:'" + channel1[i].bizHourTime + "'},";
		} else {

			channelAry += "'" + channel1[i].bizHourTime + "'"

			channelAryData += "{value:" + channel1[i].tot_SubNetAmountPrice
					+ ",name:'" + channel1[i].bizHourTime + "'}";
		}
	}
	channelAry += "]";
	channelAryData += "]"

	var channelAryNum = "[";
	for (var i = 0; i < channel1.length; i++) {
		if (i != channel1.length - 1) {

			channelAryNum += "{value:" + channel1[i].tot_SubOrderCount
					+ ",name:'" + channel1[i].bizHourTime + "'},";
		} else {

			channelAryNum += "{value:" + channel1[i].tot_SubOrderCount
					+ ",name:'" + channel1[i].bizHourTime + "'}";
		}
	}
	channelAryNum += "]";



	var option2 = {
		tooltip : {
			trigger : 'axis'
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
					type : [  'line','bar']
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
		legend : {
			data : [ '实际收入','订单量'  ]
		},
		xAxis : [ {
			type : 'category',
			data : eval(channelAry)
		} ],
		yAxis : [ {
			type : 'value',
			name : '实际收入',
			axisLabel : {
				formatter : '{value} 元'
			}
		}, {
			type : 'value',
			name : '订单量',
			axisLabel : {
				formatter : '{value} 单'
			}
		} ],
		series : [ {
			name : '实际收入',
			type : 'bar',
			
			data : eval(channelAryData)
		}, {
			name : '订单量',
			type : 'line',
			yAxisIndex : 1,
			data : eval(channelAryNum)
		} ]
	};

	myChart1.showLoading({
		text : '正在读取数据中...', // loading话术
	});
	myChart1.hideLoading();
	myChart1.clear();
	myChart1.setOption(option2);

}

// 第二个报表图形化
function fnChannel2Data(channel1, channel2) {
	if (channel2.length == 0) {
		myChart2.clear();
		myChart2.setOption(option1);
		return;
	}

	var channelAry = "[";
	var channelAryCountData = "[";
	var channelPriceData = "[";
	for (var i = 0; i < channel2.length; i++) {
		if (i != channel2.length - 1) {
			channelAry += "'" + channel2[i].bizHourTime + "',"
			channelAryCountData += "'" + channel2[i].tot_SubOrderCount + "',"
			channelPriceData += "'" + channel2[i].TA + "',"

		} else {

			channelAry += "'" + channel2[i].bizHourTime + "'"

			channelAryCountData += "'" + channel2[i].tot_SubOrderCount + "'"
			channelPriceData += "'" + channel2[i].TA + "'"
		}
	}
	channelAry += "]";
	channelAryCountData += "]"
	channelPriceData += "]"

	
	var option2 = {
			tooltip : {
				trigger : 'axis'
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
						type : [  'line','bar']
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
			legend : {
				data : [ '时段客单价','时段单量'  ]
			},
			xAxis : [ {
				type : 'category',
				data : eval(channelAry)
			} ],
			yAxis : [ {
				type : 'value',
				name : '时段客单价',
				axisLabel : {
					formatter : '{value} 元'
				}
			}, {
				type : 'value',
				name : '时段单量',
				axisLabel : {
					formatter : '{value} 单'
				}
			} ],
			series : [ {
				name : '时段客单价',
				type : 'bar',
				
				data : eval(channelPriceData)
			}, {
				name : '时段单量',
				type : 'line',
				yAxisIndex : 1,
				data : eval(channelAryCountData)
			} ]
		};
	
	
	myChart2.showLoading({
		text : '正在读取数据中...', // loading话术
	});
	myChart2.hideLoading();
	myChart2.clear();
	myChart2.setOption(option2);
}

function fnListTableData(listData, pageData, listDataSum) {
	// 绑定明细列表信息
	var len = listDataSum.length;
	var strHtml = "";
	var strColName = "";
	var strColNameValue = "";
	for (var i = 0; i < len; i++) {
		if (i == 0) {
			strColName += "<thead><tr>";
			strColName += "<th scope='col' style=\"text-align: center;\">行号 </th>";
			for (obj in listDataSum[i]) {
				if (obj == "营业日期") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else if (obj == "门店") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 80px;padding-right: 80px;'>"
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
	for (var i = 0; i < listData.length; i++) {
		strColNameValue += "<tr>";
		strColNameValue += "<td>" + ((pageIndex - 1) * pageSzie + i + 1)
				+ "</td>"; // 明细行号列
		$.each(listData[i], function(key, value) {
			if (isfloat(value)) {

				strColNameValue += "<td align='right'>" + value + "</td>";
			} else {
				strColNameValue += "<td>" + value + "</td>";
			}
		});
		strColNameValue += "</tr>";
	}

	for (var i = 0; i < listDataSum.length; i++) {
		strColNameValue += "<tr>";
		strColNameValue += "<td></td>"; // 合计行行号列
		$.each(listDataSum[i], function(key, value) {
			if (isfloat(value)) {

				strColNameValue += "<td align='right'>" + value + "</td>";
			} else {
				strColNameValue += "<td>" + value + "</td>";
			}
		});
		strColNameValue += "</tr>";
	}

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
		var BrandName = "";
		var StoreName = "";
		var ChannelName = "";

		fnQueryData(BrandName, StoreName, ChannelName);
	});

	$("#pagerContent").html(
			"共[" + pageData[0].totalRows + "]条记录 当前第[" + pageIndex + "]页/共["
					+ pageData[0].totalPages + "]页");
}

// 查询重置按钮点击执行方法
function fnClickBtnReset() {
	/*
	 * dteTimeStart = ""; // 时间开始时间 dteTimeEnd = ""; // 时间结束时间
	 */

	dteTimeStart = initdteTimeStart;
	dteTimeEnd = initdteTimeEnd;

	$('#defaultrange').daterangepicker(
			{
				opens : (Metronic.isRTL() ? 'left' : 'right'),
				format : 'MM/DD/YYYY',
				separator : ' to ',
				startDate : moment().subtract('days', 7),
				endDate : moment(),
				minDate : '01/01/2012',
				maxDate : '12/31/2099',
				locale : {
					applyLabel : '确认',
					cancelLabel : '取消',
					fromLabel : '从',
					toLabel : '至',
					customRangeLabel : '自定义范围',
					daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
					monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月',
							'八月', '九月', '十月', '十一月', '十二月' ],
					firstDay : 1
				}
			},
			function(start, end) {
				$('#defaultrange input').val(
						start.format('YYYY-MM-DD') + ' ~ '
								+ end.format('YYYY-MM-DD'));
				dteTimeStart = start.format('YYYY-MM-DD');
				dteTimeEnd = end.format('YYYY-MM-DD');
			});
	$('#defaultrange input').val(
			moment().subtract('days', 7).format('YYYY-MM-DD') + ' ~ '
					+ moment().format('YYYY-MM-DD'));

	// 渠道
	$("#setChannelName").select2("val", "");

	$("#setStoreType").val("ALL");

	fnInitForm();

	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);
	$("#BranCode").val("");
}

var aryStoreList = "";
function fnInitForm() {

	var strStoreType = $("#setStoreType").val();

	// 获取授权的所有门店信息
	$.ajax({
		type : "get",
		url : baseUrl + '/common/dict/brand/getStoreList?StoreType='
				+ strStoreType,
		dataType : "json",
		async : false,
		success : function(data) {

			var strStore = "";
			var storeName = data.storeName;
			for (var i = 0; i < storeName.length; i++) {

				var ISLAST = storeName[i].ISLAST;
				var ID = storeName[i].ID;
				if (ISLAST == "Y") {
					strStore += ID + "|";
				}
			}

			if (strStore != "") {
				strStore = strStore.substr(0, strStore.length - 1);
			}
			$("#StoreListVal").val(strStore);
			aryStoreList = strStore;

		},
		error : function(errMsg, errTextType, errorThrown) {

		}
	});

}

function fnStoreTree() {
	var asyncSettings = {
		type : 'iframe',
		closeable : false,
		padding : false,
		cache : false,
		url : baseUrl + '/queryStoreList'
	};
	$('#setStoreList').webuiPopover('destroy').webuiPopover(
			$.extend({}, asyncSettings));
}

fnStoreTree();

function fnGetQueryStoreTree(strBranCode, strBranName, treeText,
		strStoreTreeText, treeVal) {
	$("#BranCode").val(strBranCode);
	$("#setBrandName").val(strBranName);
	$("#setStoreListText").val(strStoreTreeText);
	$("#StoreListVal").val(treeVal);
}

function fnGetSelectStoreTree() {
	return $("#StoreListVal").val();
}

// 获取选择的门店类型
function fnGetSelectStoreType() {
	return $("#setStoreType").val();
}

/*
 * 导出Excel文件
 */
function fnExportExcel() {

	var BrandName = "";
	var StoreCode = $("#StoreListVal").val();
	var ChannelName = "";
	var Paymentkey = $("#setPaymentkey").select2("val");
	if (Paymentkey == "ALL") {
		Paymentkey = "";
	}
	var queryText = encodeURI(encodeURI("查询日期:+" + dteTimeStart + "~"
			+ dteTimeEnd + " \n门店类型:"
			+ $("#setStoreType").find("option:selected").text() + " \n品牌范围:"
			+ $("#setBrandName").val() + " \n门店范围:"
			+ $("#setStoreListText").val()));

	var rPam = "?TimeStart=" + dteTimeStart + "&TimeEnd=" + dteTimeEnd
			+ "&BrandName=" + BrandName + "&StoreCode=" + StoreCode
			+ "&ChannelName=" + ChannelName + "&queryText=" + queryText
			+ "&BranCode=" + $("#BranCode").val();
	var exportIframe = document.getElementById("exportIframe");
	exportIframe.src = baseUrl + '/store/queryRepHalfTimeSalesExportExcel/' + rPam;

}

/*
 * 设置门店类型值改变事件
 */
function fnSetStoreTypeChange() {
	fnInitForm();
	$("#setBrandName").val("全部品牌");
	$("#setStoreListText").val("全部门店");
	$("#StoreListVal").val(aryStoreList);
	$("#BranCode").val("");
}

var rephalftimesales = function() {

	var handleDatePickers = function() {
		if (!jQuery().daterangepicker) {
			return;
		}
		$('#defaultrange').daterangepicker(
				{
					opens : (Metronic.isRTL() ? 'left' : 'right'),
					format : 'MM/DD/YYYY',
					separator : ' to ',
					startDate : moment().subtract('days', 7),
					endDate : moment(),
					minDate : '01/01/2012',
					maxDate : '12/31/2099',
					locale : {
						applyLabel : '确认',
						cancelLabel : '取消',
						fromLabel : '从',
						toLabel : '至',
						customRangeLabel : '自定义范围',
						daysOfWeek : [ '日', '一', '二', '三', '四', '五', '六' ],
						monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月',
								'七月', '八月', '九月', '十月', '十一月', '十二月' ],
						firstDay : 1
					}
				},
				function(start, end) {
					$('#defaultrange input').val(
							start.format('YYYY-MM-DD') + ' ~ '
									+ end.format('YYYY-MM-DD'));
					dteTimeStart = start.format('YYYY-MM-DD');
					dteTimeEnd = end.format('YYYY-MM-DD');
				});

		$('#defaultrange input').val(
				moment().subtract('days', 7).format('YYYY-MM-DD') + ' ~ '
						+ moment().format('YYYY-MM-DD'));

		dteTimeStart = moment().subtract('days', 7).format('YYYY-MM-DD');
		dteTimeEnd = moment().format('YYYY-MM-DD');

	}

	return {

		init : function() {
			handleDatePickers();
			fnSetStoreType();

			fnGetMenuFunction(baseUrl, funcID);
			fnListeningActQuery();
			fnInitForm();
			

			setTimeout(function() {
				fnQueryData("", "", "");
			}, 1000);

			$("#setBrandName").attr("disabled", "disabled");
			$("#setStoreListText").attr("disabled", "disabled");
			$("#setBrandName").val("全部品牌");
			$("#setStoreListText").val("全部门店");

			initdteTimeStart = dteTimeStart;
			initdteTimeEnd = dteTimeEnd;

		}
	};

}();
