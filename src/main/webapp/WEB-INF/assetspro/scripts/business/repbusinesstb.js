var baseUrl = $("#baseUrl").val();
var funcID = "R0302";
var pageIndex = 1;
var pageSzie = 15;

var initdteTimeStart;
var initdteTimeEnd;

var initdteTBTimeStart;
var initdteTBTimeEnd;


// 查询时有时间范围临时变量 规则 一个时间间隔控件对应 两个变量
var dteTimeStart; // 时间开始时间
var dteTimeEnd; // 时间结束时间


var dteTBTimeStart; // 时间开始时间
var dteTBTimeEnd; // 时间结束时间

var myChart1;
var myChart2;
var myChart3;

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
	myChart2 = ec.init(document.getElementById('chart-2'));
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
	myChart3 = ec.init(document.getElementById('chart-3'));
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
	var favourableName = "";

	fnQueryData(BrandName, StoreName, ChannelName, favourableName);

}

var listDataSum;

// 查询数据
function fnQueryData(BrandCode, StoreCode, ChannelName, favourableName) {

	BrandCode = $("#BranCode").val();
	StoreCode = $("#StoreListVal").val();
	navAry = Array();
	navAry[0] = "ALLClick$查询结果$";

	var strHtml = "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick(0)\">"
			+ "查询结果" + "</a>>>";
	document.getElementById('DataNav').innerHTML = "";
	$('#DataNav').append(strHtml);

	fnLoadingShowAndClose(true);
	$.ajax({
		type : "POST",
		dataType : "json",
		url : baseUrl + '/business/queryRepBusinessTB/',
		data : {
			TimeStart : dteTimeStart,
			TimeEnd : dteTimeEnd,
			BrandCode : BrandCode,
			BrandName : BrandCode,
			StoreCode : StoreCode,
			ChannelName : ChannelName,
			PageIndex : pageIndex,
			PageSize : pageSzie,
			TBTimeStart : dteTBTimeStart,
			TBTimeEnd : dteTBTimeEnd
		},
		success : function(data) {
			/*
			 * // 支付方式结构分析 var storePay = data.storePay; var storePayType =
			 * data.storePayType; fnPayModeData(storePay, storePayType);
			 * fnPayModeSubSamountData(storePay, storePayType);
			 */

		

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
			fnLoadingShowAndClose(false);
		},
		error : function(e) {
			fnLoadingShowAndClose(false);
		}
	});

}

// 第一个报表图形化
function fnPayModeData(BTNData, ZZTData) {

	if (BTNData.length == 0) {
		myChart1.clear();
		myChart1.setOption(option1);
		return;
	}

	var payNameAry = "[";
	var payNameAryData = "[";
	for (var i = 0; i < BTNData.length; i++) {
		if (i != BTNData.length - 1) {
			payNameAry += "'" + BTNData[i].orderTypeDesc + "',"
			payNameAryData += "{value:" + BTNData[i].OrderCount + ",name:'"
					+ BTNData[i].orderTypeDesc + "'},";
		} else {

			payNameAry += "'" + BTNData[i].orderTypeDesc + "'"

			payNameAryData += "{value:" + BTNData[i].OrderCount + ",name:'"
					+ BTNData[i].orderTypeDesc + "'}";
		}
	}
	payNameAry += "]";
	payNameAryData += "]"

	var ChannlNameAry = "[";
	var payTypeData = "[";
	for (var i = 0; i < ZZTData.length; i++) {
		if (i != ZZTData.length - 1) {
			ChannlNameAry += "'" + ZZTData[i].channelName + "',"
			payTypeData += "{value:" + ZZTData[i].OrderCount + ",name:'"
					+ ZZTData[i].channelName + "'},";
		} else {
			ChannlNameAry += "'" + ZZTData[i].channelName + "'"
			payTypeData += "{value:" + ZZTData[i].OrderCount + ",name:'"
					+ ZZTData[i].channelName + "'}";
		}
	}
	ChannlNameAry += "]";
	payTypeData += "]";

	var option1Data = {
		title : {
			text : '销售渠道优惠单量分析',
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
			data : eval(ChannlNameAry)
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
			name : '售卖方式',
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
			data : eval(payNameAryData)
		}, {
			name : '销售渠道',
			type : 'pie',
			radius : [ 100, 140 ],

			// for funnel
			x : '60%',
			width : '35%',
			funnelAlign : 'left',
			max : 1048,

			data : eval(payTypeData)
		} ]
	};

	myChart1.showLoading({
		text : '正在读取数据中...', // loading话术
	});
	myChart1.hideLoading();
	myChart1.clear();
	myChart1.setOption(option1Data);

}

// 第二个报表图形化
function fnPayModeSubSamountData(ZZTData) {
	if (ZZTData.length == 0) {
		myChart2.clear();
		myChart2.setOption(option1);
		return;
	}
	var payNameAry = "[";
	var payNameAryData = "[";
	for (var i = 0; i < ZZTData.length; i++) {
		if (i != ZZTData.length - 1) {
			payNameAry += "'" + ZZTData[i].channelName + "',"
			payNameAryData += "{data:[" + ZZTData[i].sOrderTotalAmount + ","
					+ ZZTData[i].sDiscount + "," + ZZTData[i].sNetAmountPrice
					+ "],type : 'bar',name:'" + ZZTData[i].channelName + "'},";
		} else {

			payNameAry += "'" + ZZTData[i].channelName + "'"

			payNameAryData += "{data:[" + ZZTData[i].sOrderTotalAmount + ","
					+ ZZTData[i].sDiscount + "," + ZZTData[i].sNetAmountPrice
					+ "],type : 'bar',name:'" + ZZTData[i].channelName + "'}";
		}
	}
	payNameAry += "]";
	payNameAryData += "]"

	var option2 = {
		tooltip : {
			trigger : 'axis',
			axisPointer : {
				type : 'shadow'
			}
		},
		legend : {
			data : eval(payNameAry)
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
			data : [ '订单金额', '折扣金额', '实际收入' ]
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
		series : eval(payNameAryData)
	};
	myChart2.showLoading({
		text : '正在读取数据中...', // loading话术
	});
	myChart2.hideLoading();
	myChart2.clear();
	myChart2.setOption(option2);
}

// 第三个报表图形化
function fnFavourableName(DBTData) {

	if (DBTData.length == 0) {
		myChart3.clear();
		myChart3.setOption(option1);
		return;
	}

	var payNameAry = "[";
	var payNameAryData1 = "[";
	var payNameAryData2 = "[";
	var payNameAryData3 = "[";
	for (var i = 0; i < DBTData.length; i++) {
		if (i != DBTData.length - 1) {
			payNameAry += "'" + DBTData[i].favourableName + "',";
			payNameAryData1 += "'" + DBTData[i].sNetAmountPrice + "',";
			payNameAryData2 += "'" + DBTData[i].sDiscount + "',";
			payNameAryData3 += "'" + DBTData[i].sOrderTotalAmount + "',";
		} else {

			payNameAry += "'" + DBTData[i].favourableName + "'";
			payNameAryData1 += "'" + DBTData[i].sNetAmountPrice + "'";
			payNameAryData2 += "'" + DBTData[i].sDiscount + "'";
			payNameAryData3 += "'" + DBTData[i].sOrderTotalAmount + "'";

		}
	}
	payNameAry += "]";
	payNameAryData1 += "]";
	payNameAryData2 += "]";
	payNameAryData3 += "]";

	var option3 = {
		tooltip : {
			trigger : 'axis',
			axisPointer : { // 坐标轴指示器，坐标轴触发有效
				type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
			}
		},
		legend : {
			data : [ '实际收入', '折扣金额', '订单金额' ]
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
			type : 'value',
			axisLabel : {
				formatter : '{value} 元'
			}
		} ],
		yAxis : [ {
			type : 'category',
			axisTick : {
				show : false
			},
			data : eval(payNameAry)
		} ],
		series : [ {
			name : '实际收入',
			type : 'bar',
			itemStyle : {
				normal : {
					label : {
						show : true,
						position : 'inside'
					}
				}
			},
			data : eval(payNameAryData1)
		}, {
			name : '订单金额',
			type : 'bar',
			stack : '总量',
			barWidth : 5,
			itemStyle : {
				normal : {
					label : {
						show : true
					}
				}
			},
			data : eval(payNameAryData3)
		}, {
			name : '折扣金额',
			type : 'bar',
			stack : '总量',
			itemStyle : {
				normal : {
					label : {
						show : true,
						position : 'left'
					}
				}
			},
			data : eval(payNameAryData2)
		} ]
	};
	myChart3.showLoading({
		text : '正在读取数据中...', // loading话术
	});
	myChart3.hideLoading();
	myChart3.clear();
	myChart3.setOption(option3);

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

				if (obj == "日期") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else if (obj == "品牌" || obj == "区域" || obj == "门店") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 80px;padding-right: 80px;'>"
							+ obj + "</th>";
				} else if (obj == "省份" || obj == "城市" || obj == "区县"
						|| obj == "所属品牌") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 30px;padding-right: 30px;'>"
							+ obj + "</th>";
				} else if (obj == "所属区域") {
					strColName += "<th scope='col' style='text-align: center;padding-left: 20px;padding-right: 20px;'>"
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
		$
				.each(
						listData[i],
						function(key, value) {

							if (isfloat(value)) {

								strColNameValue += "<td align='right'>" + value
										+ "</td>";
							} else {

								if (key == "品牌") {

									if (value != "") {
										var valueAry = value.split("|=|");

										strColNameValue += "<td><a href='JavaScript:void(0)' onclick=\"JavaScript:fnEdtBrandClick('"
												+ value
												+ "')\">"
												+ valueAry[1]
												+ "</a></td>";
									} else {
										strColNameValue += "<td>" + value
												+ "</td>";
									}
								} else if (key == "区域") {

									if (value != "") {
										var valueAry = value.split("|=|");

										strColNameValue += "<td><a href='JavaScript:void(0)' onclick=\"JavaScript:fnEdtAreaClick('"
												+ value
												+ "')\">"
												+ valueAry[3]
												+ "</a></td>";
									} else {
										strColNameValue += "<td>" + value
												+ "</td>";
									}
								} else {
									strColNameValue += "<td>" + value + "</td>";
								}

							}

						});
		strColNameValue += "</tr>";
	}

	for (var i = 0; i < listDataSum.length; i++) {
		strColNameValue += "<tr>";
		strColNameValue += "<td></td>"; // 明细行号列
		$.each(listDataSum[i], function(key, value) {
			if (isfloat(value)) {
				strColNameValue += "<td align='right'>" + value + "</td>";
			} else {
				strColNameValue += "<td >" + value + "</td>";
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
		var favourableName = "";
		fnQueryData(BrandName, StoreName, ChannelName, favourableName);
	});

	$("#pagerContent").html(
			"共[" + pageData[0].totalRows + "]条记录 当前第[" + pageIndex + "]页/共["
					+ pageData[0].totalPages + "]页");
}

var navAry = Array();

// 第一级别品牌点击
function fnEdtBrandClick(strPrm) {

	navID = 1;
	var strPrmAry = strPrm.split("|=|");

	var brandCode = strPrmAry[0];
	var brandName = strPrmAry[1];
	var areaCode = strPrmAry[2];

	/*
	 * var strHtml = "<a href='JavaScript:void(0)'
	 * onclick=\"JavaScript:fnEdtBrandClick('" + strPrm + "')\">" + brandName + "</a>>>";
	 * 
	 */

	/*
	 * var BrandName = $("#setBrandName").select2("val"); var strHtml = ""; if
	 * (BrandName != "") { navAry = Array(); navAry[0] = "BrandClick$" +
	 * brandName + "$" + strPrm; strHtml = "<a href='JavaScript:void(0)'
	 * onclick=\"JavaScript:fnNavDataClick(0)\">" + brandName + "</a>>>";
	 * document.getElementById('DataNav').innerHTML = "";
	 * $('#DataNav').append(strHtml); } else {
	 */
	var navAryL = navAry.length;
	navAry[navAryL] = "BrandClick$" + brandName + "$" + strPrm;
	var strHtml = "";
	for (var i = 0; i < navAry.length; i++) {
		var alltemp = navAry[i];
		var showName = alltemp.split("$")[1];
		strHtml += "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick("
				+ (i) + ")\">" + showName + "</a>>>";
		// }

		document.getElementById('DataNav').innerHTML = "";
		$('#DataNav').append(strHtml);
	}

	
	if (areaCode != "") {

		
		
		// 查询区域数据;
		fnLoadingShowAndClose(true);
		$.ajax({
			type : "POST",
			dataType : "json",
			url : baseUrl + '/business/queryRepBusinessTBBrandChildren/',
			data : {
				TimeStart : dteTimeStart,
				TimeEnd : dteTimeEnd,
				BrandCode : brandCode,
				BrandName : brandName,
				StoreCode : $("#StoreListVal").val(),
				ChannelName : "",
				PageIndex : pageIndex,
				PageSize : pageSzie,
				AreaCode : areaCode,
				TBTimeStart : dteTBTimeStart,
				TBTimeEnd : dteTBTimeEnd
			},
			success : function(data) {
				/*
				 * // 支付方式结构分析 var storePay = data.storePay; var storePayType =
				 * data.storePayType; fnPayModeData(storePay, storePayType);
				 * fnPayModeSubSamountData(storePay, storePayType);
				 */


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
				fnLoadingShowAndClose(false);
			},
			error : function(e) {
				fnLoadingShowAndClose(false);
			}
		});
	} else {

		// 直接查询门店数据

	}
}

var mapNav = new Map();

function fnNavDataClick(index) {
	var navAryL = navAry.length;
	var navAryTemp = new Array();

	for (var i = 0; i < index; i++) {
		navAryTemp[i] = navAry[i];
	}

	var strAll = navAry[index];
	var strPrmAry = strAll.split("$");
	var stype = strPrmAry[0];
	var sName = strPrmAry[1];
	var sPrm = strPrmAry[2];

	navAry = navAryTemp;

	if (stype == "BrandClick") {
		fnEdtBrandClick(sPrm);
	} else if (stype == "AreaClick") {
		fnEdtAreaClick(sPrm);
	} else if (stype == "ALLClick") {
		fnBtnClickSetQuery();
	}

}

// 第二级别区域点击
function fnEdtAreaClick(strPrm) {

	var strPrmAry = strPrm.split("|=|");

	var brandCode = strPrmAry[0];
	var brandName = strPrmAry[1];
	var areaCode = strPrmAry[2];
	var areaName = strPrmAry[3];
	var isStore = strPrmAry[4];

	var navAryL = navAry.length;
	navAry[navAryL] = "AreaClick$" + areaName + "$" + strPrm;
	var strHtml = "";
	for (var i = 0; i < navAry.length; i++) {
		var alltemp = navAry[i];
		var showName = alltemp.split("$")[1];
		strHtml += "<a href='JavaScript:void(0)' onclick=\"JavaScript:fnNavDataClick("
				+ (i) + ")\">" + showName + "</a>>>";
	}

	document.getElementById('DataNav').innerHTML = "";
	$('#DataNav').append(strHtml);

	if (areaCode != "") {

		if (isStore == "N") {

			// 查询区域数据;
			fnLoadingShowAndClose(true);
			$.ajax({
				type : "POST",
				dataType : "json",
				url : baseUrl + '/business/queryRepBusinessTBBrandChildren/',
				data : {
					TimeStart : dteTimeStart,
					TimeEnd : dteTimeEnd,
					BrandCode : brandCode,
					BrandName : brandName,
					StoreCode : $("#StoreListVal").val(),
					ChannelName : "",
					PageIndex : pageIndex,
					PageSize : pageSzie,
					AreaCode : areaCode,
					TBTimeStart : dteTBTimeStart,
					TBTimeEnd : dteTBTimeEnd
				},
				success : function(data) {
					/*
					 * // 支付方式结构分析 var storePay = data.storePay; var
					 * storePayType = data.storePayType; fnPayModeData(storePay,
					 * storePayType); fnPayModeSubSamountData(storePay,
					 * storePayType);
					 */

				

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
					fnLoadingShowAndClose(false);
				},
				error : function(e) {
					fnLoadingShowAndClose(false);
				}
			});
		} else {

			// 查询区域数据;
			fnLoadingShowAndClose(true);
			$.ajax({
				type : "POST",
				dataType : "json",
				url : baseUrl
						+ '/business/queryRepBusinessTBBrandChildrenStore/',
				data : {
					TimeStart : dteTimeStart,
					TimeEnd : dteTimeEnd,
					BrandCode : brandCode,
					BrandName : brandName,
					StoreCode :$("#StoreListVal").val(),
					ChannelName : "",
					PageIndex : pageIndex,
					PageSize : pageSzie,
					AreaCode : areaCode,
					TBTimeStart : dteTBTimeStart,
					TBTimeEnd : dteTBTimeEnd
				},
				success : function(data) {
					/*
					 * // 支付方式结构分析 var storePay = data.storePay; var
					 * storePayType = data.storePayType; fnPayModeData(storePay,
					 * storePayType); fnPayModeSubSamountData(storePay,
					 * storePayType);
					 */

				

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
					fnLoadingShowAndClose(false);
				},
				error : function(e) {
					fnLoadingShowAndClose(false);
				}
			});
		}

	} else {

		// 直接查询门店数据

	}
}

// 查询重置按钮点击执行方法
function fnClickBtnReset() {

	dteTimeStart = initdteTimeStart;
	dteTimeEnd = initdteTimeEnd;

	dteTBTimeStart = initdteTBTimeStart;
	dteTBTimeEnd = initdteTBTimeEnd;
	
	$('#defaultrange').daterangepicker(
			{
				opens : (Metronic.isRTL() ? 'left' : 'right'),
				format : 'MM/DD/YYYY',
				separator : ' to ',
				startDate : moment(),
				endDate : moment(),
				minDate : '01/01/2012',
				maxDate : '12/31/2099',
				dateLimit : {
					days : 0
				},
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

	$('#defaultrangetb').daterangepicker(
			{
				opens : (Metronic.isRTL() ? 'left' : 'right'),
				format : 'MM/DD/YYYY',
				separator : ' to ',
				startDate : moment().subtract('days', 1),
				endDate : moment().subtract('days', 1),
				minDate : '01/01/2012',
				maxDate : '12/31/2099',
				dateLimit : {
					days : 0
				},
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
				$('#defaultrangetb input').val(
						start.format('YYYY-MM-DD') + ' ~ '
								+ end.format('YYYY-MM-DD'));
				dteTBTimeStart = start.format('YYYY-MM-DD');
				dteTBTimeEnd = end.format('YYYY-MM-DD');

			});

	$('#defaultrange input').val(
			moment().format('YYYY-MM-DD') + ' ~ '
					+ moment().format('YYYY-MM-DD'));

	$('#defaultrangetb input').val(
			moment().subtract('days', 1).format('YYYY-MM-DD') + ' ~ '
					+ moment().subtract('days', 1).format('YYYY-MM-DD'));

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


//获取选择的门店类型
function fnGetSelectStoreType() {
	return $("#setStoreType").val();
}


/*
 * 导出Excel文件
 */
function fnExportExcel() {

	/*
	var BrandName = "";
	var StoreCode = $("#StoreListVal").val();
	var ChannelName = "";
	var queryText = encodeURI(encodeURI("查询日期:+" + dteTimeStart + "~"
			+ dteTimeEnd + " \n品牌范围:" + $("#setBrandName").val() + " \n门店范围:"
			+ $("#setStoreListText").val()));

	var rPam = "?TimeStart=" + dteTimeStart + "&TimeEnd=" + dteTimeEnd
			+ "&BrandName=" + BrandName + "&StoreCode=" + StoreCode
			+ "&ChannelName=" + ChannelName + "&queryText=" + queryText;
	var exportIframe = document.getElementById("exportIframe");
	exportIframe.src = baseUrl + '/business/queryRepTrendExportExcel/' + rPam;
*/
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

var repbusinesstb = function() {

	var handleDatePickers = function() {
		if (!jQuery().daterangepicker) {
			return;
		}
		$('#defaultrange').daterangepicker(
				{
					opens : (Metronic.isRTL() ? 'left' : 'right'),
					format : 'MM/DD/YYYY',
					separator : ' to ',
					startDate : moment(),
					endDate : moment(),
					minDate : '01/01/2012',
					maxDate : '12/31/2099',
					dateLimit : {
						days : 0
					},
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
					dteEndStart= end.format('YYYY-MM-DD');
				});

		$('#defaultrangetb').daterangepicker(
				{
					opens : (Metronic.isRTL() ? 'left' : 'right'),
					format : 'MM/DD/YYYY',
					separator : ' to ',
					startDate : moment().subtract('days', 1),
					endDate : moment().subtract('days', 1),
					minDate : '01/01/2012',
					maxDate : '12/31/2099',
					dateLimit : {
						days : 0
					},
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
					$('#defaultrangetb input').val(
							start.format('YYYY-MM-DD') + ' ~ '
									+ end.format('YYYY-MM-DD'));
					
					dteTBTimeStart = start.format('YYYY-MM-DD');
					dteTBEndStart = end.format('YYYY-MM-DD') ;
				
				});

		$('#defaultrange input').val(
				moment().format('YYYY-MM-DD') + ' ~ '
						+ moment().format('YYYY-MM-DD'));

		$('#defaultrangetb input').val(
				moment().subtract('days', 1).format('YYYY-MM-DD') + ' ~ '
						+ moment().subtract('days', 1).format('YYYY-MM-DD'));

		dteTimeStart = moment().format('YYYY-MM-DD');
		dteTimeEnd = moment().format('YYYY-MM-DD');
		
		dteTBTimeStart = moment().subtract('days', 1).format('YYYY-MM-DD');
		dteTBTimeEnd = moment().subtract('days', 1).format('YYYY-MM-DD');

	}

	return {

		init : function() {
			handleDatePickers();
			fnSetStoreType();
			fnGetMenuFunction(baseUrl, funcID);
			fnListeningActQuery();
			fnInitForm();
			

			setTimeout(function() {
				fnQueryData("", "", "", "");
			}, 1000);

			$("#setBrandName").attr("disabled", "disabled");
			$("#setStoreListText").attr("disabled", "disabled");
			$("#setBrandName").val("全部品牌");
			$("#setStoreListText").val("全部门店");

			initdteTimeStart = dteTimeStart;
			initdteTimeEnd = dteTimeEnd;

			initdteTBTimeStart = dteTBTimeStart;
			initdteTBTimeEnd = dteTBTimeEnd;
		}
	};

}();
